package Poker;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private Hand hand, communityCards;
    private List<Integer> kickers;
    private Money money, bet, callValue;
    private Game game;
    private Action status;
    private String name;
    private boolean control;
    private int x;
    private int y;
    private boolean wait;

    static final int PLAYER_HEIGHT = PokerTable.STRING_LINE_SHIFT * 3 + Card.CARD_HEIGHT;
    static final int PLAYER_WIDTH = Card.CARD_WIDTH * Game.HAND_SIZE + PokerTable.PADDING;

    public Player(Hand commCards, int money, boolean control)
    {
        communityCards = commCards;
        this.money = new Money(money);
        bet = new Money();
        callValue = new Money();
        status = Action.CHECK;
        wait = true;
        this.control = control;
        name = "Player";
    }

    public Player(Hand commCards, int money, int x, int y, boolean control)
    {
        this(commCards, money, control);
        this.x = x;
        this.y = y;
        hand = new Hand(x, y + PokerTable.STRING_LINE_SHIFT * 3);
    }

    public Player(Hand commCards, int money, int[] cards, int x, int y, boolean control)
    {
        this(commCards, money, control);
        this.x = x;
        this.y = y;
        hand = new Hand(cards, x, y + PokerTable.STRING_LINE_SHIFT * 3);
    }

    public PokerHand getStrength()
    {
        return hand.evalHandAccuracy();
    }

    public PokerHand getStrength(Hand commHand)
    {
        ArrayList<Card> cards = new ArrayList<>(commHand.getCards());
        cards.addAll(hand.getCards());
        Hand h = new Hand(cards);
        PokerHand pk = h.evalHandAccuracy();
        kickers = h.getKickers();
        return pk;
    }

    public void rename(String name)
    {
        this.name = name;
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

    public void addBet(int amount)
    {
        bet.add(amount);
    }

    public void subtractBet(int amount)
    {
        bet.subtract(amount);
    }

    public void clearBet()
    {
        bet.setAmount(0);
    }

    public void clearStatus()
    {
        status = Action.CHECK;
    }

    public void addMoney(int amount)
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
            wait = true;
        else if(betAmount == -1)
        {
            status = Action.FOLD;
        }
        else if(betAmount == 0)
        {
            status = Action.CHECK;
        }
        // Invalid input so get next input callValue > betAmount
        // Later you want to change this to
//        else if(betAmount < callValue.getAmount())
//        {
//            wait = true;
//        }
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
        hand = new Hand(x, y + PokerTable.STRING_LINE_SHIFT * 2);
    }


    public void paint(Graphics2D g)
    {
        g.drawString("Money :" + money.getAmount(), x, y + 10);
        g.drawString(status + "    Bet :" + getBetTotal(), x, y + 10 + PokerTable.STRING_LINE_SHIFT);
        g.drawString("Hand :" + getStrength(communityCards) + "(" + Card.NUMBERS[kickers.get(0)] + ")", x, y + 10 + PokerTable.STRING_LINE_SHIFT * 2);
        hand.paint(g);
    }
}
