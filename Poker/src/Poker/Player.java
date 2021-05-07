package Poker;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Player implements Comparable<Player>{
    private Hand hand;
    private final Hand communityCards;
    private Hand hiddenHand;
    private List<Integer> kickers;
    private Money money, startMoney, bet;
    private Action status;
    private String name;
    private PokerHand strength;
    private final boolean control;
    private int x;
    private int y;
    private boolean wait;
    private boolean showdown;

    static final int HEIGHT = PokerTable.PADDING * 3 + Card.CARD_HEIGHT;
    static final int WIDTH = Card.CARD_WIDTH * Game.HAND_SIZE + PokerTable.PADDING;


    public Player(Hand commCards)
    {
        communityCards = commCards;
        hand = new Hand();
        kickers = new ArrayList<>();
        name = "Player";
        money = new Money();
        startMoney = new Money();
        control = true;
        showdown = false;
    }

    public Player(Hand commCards, int money, boolean control)
    {
        communityCards = commCards;
        this.money = new Money(money);
        startMoney = new Money(money);
        bet = new Money();
        status = Action.WAIT;
        wait = true;
        this.control = control;
        name = "Player";
        showdown = false;

    }

    public Player(Hand commCards, int money, int x, int y, boolean control)
    {
        this(commCards, money, control);
        this.x = x;
        this.y = y;
        hand = new Hand(x, y + PokerTable.PADDING * 3);
        hiddenHand = new Hand(x, y + PokerTable.PADDING * 3, Game.HAND_SIZE, "Blue");
    }

    public Player(Hand commCards, int money, int[] cards, int x, int y, boolean control)
    {
        this(commCards, money, control);
        this.x = x;
        this.y = y;
        hand = new Hand(cards, x, y + PokerTable.PADDING * 3);
        hiddenHand = new Hand(x, y + PokerTable.PADDING * 3, Game.HAND_SIZE, "Blue");
    }

    public void rename(String name)
    {
        this.name = name;
    }

    public PokerHand getStrength()
    {
        return strength;
    }

    public List<Integer> getKickers()
    {
        return kickers;
    }

    public String name()
    {
        return name;
    }

    public int getMoney()
    {
        return money.getAmount();
    }

    public int getBetTotal()
    {
        return bet.getAmount();
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait)
    {
        this.wait = wait;
    }

    public boolean isControl()
    {
        return control;
    }

    public void showdown()
    {
        showdown = true;
    }

    public void turnOnWait() {
        wait = true;
    }

    public void clearBet()
    {
        bet.setAmount(0);
    }

    public void clearStatus()
    {
        status = Action.WAIT;
    }

    public void setStatus(Action action) {
        status = action;
    }

    public void takesMoney(int amount)
    {
        money.add(amount);
    }

    public int givesMoney(int amount)
    {
        return money.subtract(amount);
    }

    public void pickCard(Card card)
    {
        hand.addCard(card);
        evalHandAccuracy();
    }

    public void pickCards(int[] cards)
    {
        for (int card : cards) {
            hand.addCard(new Card(card));
        }
        evalHandAccuracy();
    }

    public void pickCards(Card... cards)
    {
        for (Card card : cards) {
            hand.addCard(card);
        }
        evalHandAccuracy();
    }

    public Action getStatus()
    {
        return status;
    }

    public int getPayTotal()
    {
        return startMoney.getAmount() - money.getAmount();
    }

    public int givePayTotal(int amount)
    {
        return startMoney.subtract(amount);
    }

    public void evalHandAccuracy()
    {
        ArrayList<Card> cards = new ArrayList<>(communityCards.getCards());
        cards.addAll(hand.getCards());
        Hand h = new Hand(cards);
        strength = h.evalHandAccuracy();
        kickers = h.getKickers();
    }

    public double[] handStrengthPrediction(int nTrials)
    {
        double[] handStrength = new double[PokerHand.values().length];

        // Copy hand
        ArrayList<Card> cards = new ArrayList<>(communityCards.getCards());
        int handSize = hand.size() + communityCards.size();
        cards.addAll(hand.getCards());
        Hand startHand = new Hand(cards);
        int[] cardIds = new int[Game.COMMUNITY_CARDS_SIZE + Game.HAND_SIZE];
        int[] excludes = new int[startHand.size()];
        for(int i = 0; i < startHand.size(); i++)
        {
            excludes[i] = startHand.get(i).getId();
            cardIds[i] = startHand.get(i).getId();
        }

        // Prepare deck
        int[] deck = Deck.fill(excludes);
        // Simulate nTrial different hands
        for(int i = 0; i < nTrials; i++)
        {
            Deck.shuffle(deck);
            for(int j = 0; j < Game.COMMUNITY_CARDS_SIZE - communityCards.size(); j++)
                cardIds[handSize + j] = deck[j];
            Hand h = new Hand(cardIds);
            try{
                handStrength[h.evalHand().getId()]++;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }

        for(int i = 0; i < handStrength.length; i++)
        {
            handStrength[i] = handStrength[i] / nTrials * 100;
        }

        return handStrength;
    }

    public float winRatePrediction(int nPlayers, int nTrials)
    {
        // Copy hand
        List<Card> commCards = communityCards.getCards();
        int commSize = communityCards.size();
        int[] excludes = new int[commSize + Game.HAND_SIZE];

        // Copy community cards to players array
        int[][] botsCards = new int[nPlayers - 1][Game.COMMUNITY_CARDS_SIZE + Game.HAND_SIZE];
        int[] playerCards = new int[Game.COMMUNITY_CARDS_SIZE + Game.HAND_SIZE];

        for(int i = 0; i < commSize; i++)
        {
            int cardId = commCards.get(i).getId();
            for(int j = 0; j < nPlayers - 1; j++)
            {
                botsCards[j][i] = cardId;
            }
            excludes[i] = cardId;
            playerCards[i] = cardId;
        }

        for(int i = 0; i < Game.HAND_SIZE; i++)
        {
            excludes[commSize + i] = hand.get(i).getId();
            playerCards[commSize + i] = hand.get(i).getId();
        }


        // Prepare deck
        int[] deck = Deck.fill(excludes);
        int winCount = 0;
        for(int i = 0; i < nTrials; i++)
        {
            Deck.shuffle(deck);
            int deckShift = 0;
            // Bot hands
            for(int j = 0; j < nPlayers - 1; j++)
            {
                for(int k = 0; k < Game.HAND_SIZE; k++)
                {
                    botsCards[j][k + commSize] = deck[deckShift++];
                }
            }

            // CommCards add to players
            int commCardNeed = Game.COMMUNITY_CARDS_SIZE - communityCards.size();
            for(int j = 0; j < commCardNeed; j++)
            {
                try{
                    int num = deck[j + deckShift];
                    playerCards[Game.HAND_SIZE + commSize + j] = num;
                }
                catch (Exception e)
                {
                    System.out.println("j : " + j);
                    e.printStackTrace();
                }
                for(int k = 0; k < nPlayers - 1; k++)
                    botsCards[k][Game.HAND_SIZE + commSize + j] = deck[j + deckShift];
            }

            Hand playerHand = new Hand(playerCards);
            Hand[] botsHand = new Hand[nPlayers - 1];
            for(int j = 0; j < nPlayers - 1; j++)
                botsHand[j]  = new Hand(botsCards[j]);

            List<Hand> hands = new ArrayList<>();
            hands.add(playerHand);
            hands.addAll(Arrays.asList(botsHand).subList(0, nPlayers - 1));

            Hand win = Collections.max(hands);
            if(win.equals(playerHand))
                winCount++;
        }
        return winCount/(float)nTrials;
    }


    public void AiCall(int SBValue, Money callTotal, Money payDest)
    {
        int pay = 0;

        if(status == Action.FOLD) {
            System.out.println("Call : Fold");
            return;
        }

        if(status == Action.ALL_IN)
        {
            System.out.println("Cannot call during all in status");
            return;
        }

        // Bet
        if(callTotal.getAmount() == 0)
        {
            // Pay SB
            if(SBValue < money.getAmount())
            {
                pay = SBValue;
                bet.add(money.subtract(pay));
                status = Action.BET;
            }
            // All in
            else
            {
                AiAllIn(callTotal, payDest);
                return;
            }
        }
        else{
            // Difference between callTotal and bet is what you need to pay to move on
            if (callTotal.getAmount() - bet.getAmount() < money.getAmount())
            {
                pay = callTotal.getAmount() - bet.getAmount();
                bet.add(money.subtract(pay));
                status = Action.CALL;
            }
            else
            {
                AiAllIn(callTotal, payDest);
                return;
            }
        }
        wait = false;
        payDest.add(pay);
        callTotal.setAmount(bet.getAmount());
    }

    // Dont update call total like AiCall
    public void AiFold()
    {
        status = Action.FOLD;
        wait = false;
    }

    public boolean AiCheck(Money callTotal)
    {
        if(status == Action.FOLD || status == Action.ALL_IN) {
            System.out.println("Check: Error All in (Fold, Allin)");
            return false;
        }
        if(callTotal.getAmount() == 0)
        {
            status = Action.CHECK;
            wait = false;
            return true;
        }
        System.out.println("Check option is not available");
        return false;
    }

    public void AiAllIn(Money callTotal, Money payDest)
    {
        int pay = money.getAmount();;

        if(status == Action.FOLD || status == Action.ALL_IN) {
            System.out.println("All in: Error All in (Fold, Allin)");
            return;
        }

        if(pay + bet.getAmount() == callTotal.getAmount())
        {
            status = Action.CALL;
            bet.add(money.subtract(pay));
        }
        // May need fix
        else if(callTotal.getAmount() == 0)
        {
            status = Action.ALL_IN;
            bet.add(money.subtract(pay));
        }
        else
        {
            status = Action.ALL_IN;
            bet.add(money.subtract(pay));
        }

        wait = false;
        payDest.add(pay);
        // Update callTotal if bet amount was more than callTotal
        if (callTotal.getAmount() <= bet.getAmount())
            callTotal.setAmount(bet.getAmount());

    }

    public void AiRaise(int raiseAmount, int SBValue, Money callTotal, Money payDest)
    {
        int pay = raiseAmount;

        if(status == Action.FOLD || status == Action.ALL_IN)
            return;
        if(callTotal.getAmount() == 0)
        {
            // First bet, bet action
            if(raiseAmount >= money.getAmount())
            {
                AiAllIn(callTotal, payDest);
                return;
            }
            else
            {
                status = Action.BET;
                bet.add(money.subtract(pay));
            }
        }
        else if(callTotal.getAmount() < raiseAmount + bet.getAmount())
        {
            status = Action.RAISE;
            bet.add(money.subtract(pay));
        }
        else if(raiseAmount >= money.getAmount())
        {
            System.out.println("Over budget, went all in option");
            AiAllIn(callTotal, payDest);
            return;
        }
        else
        {
            // May not even reach here
            System.out.println("Ai Raise Error : Went call option");
            AiCall(SBValue, callTotal, payDest);
            return;
        }

        wait = false;
        payDest.add(pay);
        callTotal.setAmount(bet.getAmount());
    }

    // Update
    public void AiSB(int SBValue, Money callTotal, Money payDest)
    {
        if(status != Action.SB || callTotal.getAmount() != 0)
        {
            System.out.println("SB bet error");
            return;
        }

        int pay = SBValue;

        if(pay > money.getAmount())
        {
            System.out.println("SB is over budget : Went fold");
            AiFold();
            return;
        }

        wait = true;
        bet.add(money.subtract(pay));
        callTotal.setAmount(SBValue);
        payDest.add(pay);
    }

    public void AiBB(int SBValue, Money callTotal, Money payDest)
    {
        if(status != Action.BB || callTotal.getAmount() != SBValue)
        {
            System.out.println("BB bet error");
            return;
        }

        int pay = SBValue * 2;

        if(pay > money.getAmount())
        {
            System.out.println("BB is over budget : Went fold");
            AiFold();
            return;
        }

        wait = true;
        bet.add(money.subtract(pay));
        callTotal.setAmount(SBValue*2);
        payDest.add(pay);
    }

    // Return hand cards
    public List<Card> reset(int smallBlind)
    {
        kickers =  new ArrayList<>();
        bet.clear();
        startMoney.setAmount(money.getAmount());
        if(money.getAmount() < smallBlind)
            status = Action.FOLD;
        else
            status = Action.WAIT;
        strength = null;
        wait = true;
        showdown = false;
        return hand.reset();

    }

    public void paint(Graphics2D g)
    {
        evalHandAccuracy();
        g.drawString("Money :" + money.getAmount(), x, y + 10);
        g.drawString(status + "    Bet :" + getBetTotal(), x, y + 10 + PokerTable.PADDING);

        String kickerStr = kickers.size() == 0 ? "" : Card.NUMBERS[kickers.get(0)];
        if(control || showdown)
        {
            g.drawString("Hand :" + getStrength() + "(" + kickerStr + ")", x, y + 10 + PokerTable.PADDING * 2);
            hand.paint(g);
        }
        else
            hiddenHand.paint(g);
    }

    @Override
    public int compareTo(Player h) {
        if(!strength.equals(h.strength))
            return h.strength.getId() - strength.getId();

        for(int i = 0; i < kickers.size() && i < h.getKickers().size(); i++)
        {
            int n1 = kickers.get(i);
            int n2 = h.getKickers().get(i);

            if(n1 != n2)
            {
                if(i <= 1)
                {
                    if(n1 == 0)
                        return 1;
                    else if(n2 == 0)
                        return -1;
                }
                return n1 - n2;
            }
        }
        return 0;
    }

    public String toString()
    {
        if(strength == null)
            evalHandAccuracy();
        return name + ": " + getStrength().name() + hand.toString();
    }

    public static void main(String[] args) {
        Hand commHand = new Hand(new int[]{2, 6, 20, 35, 40});
        Hand commHand1 = new Hand(new int[]{});
        Player p1 = new Player(commHand1);
        p1.money.setAmount(2000);
        p1.pickCards(new Card(0), new Card(13)); // Two pair // Pair
        p1.startMoney.setAmount(4000);
        Player p2 = new Player(commHand);
        p2.money.setAmount(1500);
        p2.pickCards(new Card(22), new Card(25)); //Pair
        p2.startMoney.setAmount(3500);
        Player p3 = new Player(commHand);
        p3.money.setAmount(2500);
        p3.pickCards(new Card(18), new Card(26)); // High card
        p3.startMoney.setAmount(5500);
        p3.status = Action.FOLD;
        Player p4 = new Player(commHand);
        p4.money.setAmount(0);
        p4.pickCards(new Card(14), new Card(1)); // Three card
        p4.startMoney.setAmount(1000);



        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        System.out.println(players);

        Money mainPot = new Money();
        Money sidePot = new Money();
        List<Player> sortedPlayers = new ArrayList<>(players);

        // Remove folds and add money to main pot
        for (int i = 0; i < sortedPlayers.size(); i++)
        {
            if (sortedPlayers.get(i).getStatus() == Action.FOLD)
            {
                Player removedPlayer = sortedPlayers.remove(i);
                mainPot.add(removedPlayer.getPayTotal());
            }
        }

        // Sort players by money
        Collections.sort(sortedPlayers, Comparator.comparingInt(Player::getPayTotal));

        List<List<Player>> sidePotWinners = new ArrayList<>();
        List<Integer> sidePotsAmount = new ArrayList<>();

        while (sortedPlayers.size() > 0) {
            int amount = sortedPlayers.get(0).getPayTotal();

            // Add money to side pot
            // Take money from all players in sorted list
            for (Player player : sortedPlayers) {
                sidePot.add(player.givePayTotal(amount));
            }


            List<Player> tiedPlayers = new ArrayList<>();
            // Find strongest one and put him in list.
            Player winner =
                    Collections.max(sortedPlayers, new java.util.Comparator<Player>() {
                        @Override
                        public int compare(Player o1, Player o2) {
                            return o1.compareTo(o2);
                        }
                    });

            sidePotsAmount.add(sidePot.clear());

            // Firstly, add first guy in the sorted list because he has 0 money for sure
            tiedPlayers.add(winner);

            // Next, find tied players.
            for (Player player : sortedPlayers)
                if (winner.compareTo(player) == 0 && !winner.equals(player))
                    tiedPlayers.add(player);

            // Lastly remove players who dont have money
            sortedPlayers.remove(0);
            while (sortedPlayers.size() > 0 && sortedPlayers.get(0).getPayTotal() == 0)
                sortedPlayers.remove(0);

            sidePotWinners.add(tiedPlayers);
        }

        // Pay money
        for (Player player : sidePotWinners.get(0))
            player.takesMoney(mainPot.getAmount() / sidePotWinners.get(0).size());
        for (int i = 0; i < sidePotWinners.size(); i++) {
            for (Player player : sidePotWinners.get(i))
                player.takesMoney(sidePotsAmount.get(i) / sidePotWinners.get(i).size());
        }

        for(int i = 0; i < 4; i++)
        {
            System.out.println(players.get(i) + ": " + players.get(i).getMoney());
        }
    }
}
