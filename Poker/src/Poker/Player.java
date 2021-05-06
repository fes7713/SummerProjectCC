package Poker;

import java.awt.*;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Player implements Comparable<Player>{
    private Hand hand, communityCards;
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

    public void evalHandAccuracy()
    {
        ArrayList<Card> cards = new ArrayList<>(communityCards.getCards());
        cards.addAll(hand.getCards());
        Hand h = new Hand(cards);
        strength = h.evalHandAccuracy();
        kickers = h.getKickers();
    }

    public float[] handStrengthPredict(int nTrials)
    {
        float[] handStrength = new float[PokerHand.values().length];

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
            handStrength[h.evalHand().getId()]++;
        }

        for(int i = 0; i < handStrength.length; i++)
        {
            handStrength[i] = handStrength[i] / nTrials * 100;
        }

        return handStrength;
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

    public int AiCall(int callTotal, int SBValue, Money payDest)
    {
        int pay = 0;

        // Bet
        if(callTotal == 0)
        {
            // Pay SB
            if(SBValue <= money.getAmount())
            {
                pay = SBValue;
                bet.add(money.subtract(pay));
                status = Action.CALL;
            }
            // Fold
            else
            {
                status = Action.FOLD;
                return callTotal;
            }
        }
        else{
            // Difference between callTotal and bet is what you need to pay to move on
            if (callTotal - bet.getAmount() <= money.getAmount())
            {
                pay = callTotal - bet.getAmount();
                bet.add(money.subtract(pay));
                status = Action.CALL;
            }
            else
            {
                pay = bet.getAmount();
                status = Action.ALL_IN;
                bet.add(money.clear());
            }
        }
        payDest.add(pay);
        return bet.getAmount();
    }

    // Return pay amount to the pot
    public int proceedActionCommand(int callTotal, int betAmount)
    {
//        wait = false;
        if(status == Action.FOLD || status == Action.ALL_IN)
            return 0;
        if(betAmount == -2)
            return 0;
        else if(betAmount == -1)
        {
            status = Action.FOLD;
            return callTotal;
        }
        else if(betAmount == 0)
        {
            status = Action.CHECK;
        }
        else if(callTotal == 0)
        {
            // Improve maybe??
            if(status == Action.SB) {
            }
            else if(betAmount == money.getAmount())
                status = Action.ALL_IN;
            else
                status = Action.BET;
            bet.add(money.subtract(betAmount));
        }
        else if(betAmount + bet.getAmount() == callTotal)
        {
            System.out.println(bet.getAmount());
            status = Action.CALL;
            bet.add(money.subtract(betAmount));
        }
        else if(betAmount == money.getAmount())
        {
            status = Action.ALL_IN;
            bet.add(money.subtract(betAmount));
        }
        else
        {
            // Improve maybe??
            if(status == Action.BB) {
            }
            else
                status = Action.RAISE;
            bet.add(money.subtract(betAmount));
        }
        return bet.getAmount();
    }


    // Return hand cards
    public List<Card> reset(int smallBlind)
    {
        kickers =  new ArrayList<>();
        bet.clear();
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

    public static void main(String[] args)
    {
        Hand commHand = new Hand(new int[]{2, 6, 20, 35});
        Hand commHand1 = new Hand(new int[]{});
        Player p1 = new Player(commHand);
        p1.takesMoney(2000);
        p1.pickCards(new Card(9) , new Card(22)); // Two pair // Pair
        Player p2 = new Player(commHand);
        p2.takesMoney(1500);
        p2.pickCards(new Card(22), new Card(25)); //Pair
        Player p3 = new Player(commHand);
        p3.takesMoney(3500);
        p3.pickCards(new Card(18), new Card(26)); // High card
        Player p4 = new Player(commHand);
        p4.takesMoney(1000);
        p4.pickCards(new Card(14), new Card(1)); // Three card


        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        System.out.println(players);

        float[] handStrength = p1.handStrengthPredict(100000);

        for(int i = 0; i < handStrength.length; i++)
        {
            System.out.print(PokerHand.values()[i] + " :");
            System.out.println(handStrength[i]);
        }
        commHand.addCards(new Card(40));

        handStrength = p1.handStrengthPredict(100000);

        for(int i = 0; i < handStrength.length; i++)
        {
            System.out.print(PokerHand.values()[i] + " :");
            System.out.println(handStrength[i]);
        }
    }


}
