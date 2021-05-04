package Poker;

import java.awt.*;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Player implements Comparable<Player>{
    private Hand hand, communityCards;
    private List<Integer> kickers;
    private Money money, startMoney, bet, callValue;
    private Action status;
    private String name;
    private PokerHand strength;
    private final boolean control;
    private int x;
    private int y;
    private boolean wait;

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
    }

    public Player(Hand commCards, int money, boolean control)
    {
        communityCards = commCards;
        this.money = new Money(money);
        startMoney = new Money(money);
        bet = new Money();
        callValue = new Money();
        status = Action.WAIT;
        wait = true;
        this.control = control;
        name = "Player";
    }

    public Player(Hand commCards, int money, int x, int y, boolean control)
    {
        this(commCards, money, control);
        this.x = x;
        this.y = y;
        hand = new Hand(x, y + PokerTable.PADDING * 3);
    }

    public Player(Hand commCards, int money, int[] cards, int x, int y, boolean control)
    {
        this(commCards, money, control);
        this.x = x;
        this.y = y;
        hand = new Hand(cards, x, y + PokerTable.PADDING * 3);
    }

    public void evalHandAccuracy()
    {
        ArrayList<Card> cards = new ArrayList<>(communityCards.getCards());
        cards.addAll(hand.getCards());
        Hand h = new Hand(cards);
        strength = h.evalHandAccuracy();
        kickers = h.getKickers();
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

    public void setCallValue(int value) {
        callValue.setAmount(value);
    }

    public boolean isWait() {
        return wait;
    }

    public boolean isControl()
    {
        return control;
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


    // Return pay amount to the pot
    public int proceedActionCommand(int betAmount)
    {
        wait = false;
        if(status == Action.FOLD || status == Action.ALL_IN)
            return 0;
        if(betAmount == -2)
        {
            wait = true;
            return callValue.getAmount();
        }
        else if(betAmount == -1)
        {
            status = Action.FOLD;
        }
        else if(betAmount == 0)
        {
            status = Action.CHECK;
        }
        else if(callValue.getAmount() == 0)
        {
            status = Action.BET;
            bet.add(money.subtract(betAmount));
        }
        else if(betAmount + bet.getAmount() == callValue.getAmount())
        {
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
            status = Action.RAISE;
            bet.add(money.subtract(betAmount));
        }
        return bet.getAmount();
    }

    public void clearHand()
    {
        hand = new Hand(x, y + PokerTable.PADDING * 2);
    }

    // Return hand cards
    public List<Card> reset()
    {
        kickers =  new ArrayList<>();
        bet.clear();
        callValue.clear();
        status = Action.WAIT;
        strength = null;
        wait = true;
        return hand.reset();

    }

    public void paint(Graphics2D g)
    {
        evalHandAccuracy();
        g.drawString("Money :" + money.getAmount(), x, y + 10);
        g.drawString(status + "    Bet :" + getBetTotal(), x, y + 10 + PokerTable.PADDING);

        String kickerStr = kickers.size() == 0 ? "" : Card.NUMBERS[kickers.get(0)];
        g.drawString("Hand :" + getStrength() + "(" + kickerStr + ")", x, y + 10 + PokerTable.PADDING * 2);
        hand.paint(g);
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
        Hand commHand = new Hand(new int[]{2, 6, 20, 35, 40});

        Player p1 = new Player(commHand);
        p1.takesMoney(2000);
        p1.pickCards(new Card(9) , new Card(12)); // Two pair // Pair
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

        Money sidePot = new Money();

        List<Player> sortedPlayers = new ArrayList<>(players);
        // Remove folds
        for(int i = 0; i < sortedPlayers.size(); i++)
            if(sortedPlayers.get(i).getStatus() == Action.FOLD)
                sortedPlayers.remove(i);

        Collections.sort(sortedPlayers, Comparator.comparingInt(Player::getMoney));

        List<List<Player>> sidePotWinners = new ArrayList<>();
        List<Integer> sidePotsAmount = new ArrayList<>();

        while(sortedPlayers.size() > 1)
        {
            int amount = sortedPlayers.get(0).getMoney();

            // Add money to side pot
            // Take money from all players in sorted list
            for(Player player : sortedPlayers)
            {
                sidePot.add(player.givesMoney(amount));
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
            for(Player player : sortedPlayers)
                if(winner.compareTo(player) == 0 && !winner.equals(player))
                    tiedPlayers.add(player);

            // Lastly remove players who dont have money
            sortedPlayers.remove(0);
            while(sortedPlayers.size() > 0 && sortedPlayers.get(0).getMoney() == 0)
                sortedPlayers.remove(0);

            sidePotWinners.add(tiedPlayers);
        }

        // Pay money
        for(int i = 0; i < sidePotWinners.size(); i++)
        {
            for(Player player: sidePotWinners.get(i))
                player.takesMoney(sidePotsAmount.get(i)/sidePotWinners.get(i).size());
        }
    }


}
