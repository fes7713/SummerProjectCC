package Poker;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player implements Comparable<Player>{
    private Hand hand, communityCards;
    private List<Integer> kickers;
    private Money money, bet, callValue;
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
        control = true;
    }

    public Player(Hand commCards, int money, boolean control)
    {
        communityCards = commCards;
        this.money = new Money(money);
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
        return hand.getKickers();
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

    public void takeMoney(int amount)
    {
        money.add(amount);
    }

    public void pickCard(Card card)
    {
        hand.addCard(card);
    }

    public void pickCards(int[] cards)
    {
        for (int card : cards) {
            hand.addCard(new Card(card));
        }
    }

    public void pickCards(Card... cards)
    {
        for (Card card : cards) {
            hand.addCard(card);
        }
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
        evalHandAccuracy();
        return name + ": " + getStrength().name() + hand.toString();
    }
}
