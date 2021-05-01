package Poker;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Player {
    private Hand hand;
    private Money money;
    private Game game;
    private Action status;
    boolean control;
    private int x;
    private int y;

    static final int MONEY_DRAW_SHIFT = 20;

    public Player(int money, boolean control)
    {
        hand = new Hand();
        this.money = new Money(money);
        status = Action.CHECK;
        this.control = control;
    }

    public Player(int money, int x, int y, boolean control)
    {
        this.x = x;
        this.y = y;
        hand = new Hand(x, y + MONEY_DRAW_SHIFT * 2);
        this.money = new Money(money);
        status = Action.CHECK;
        this.control = control;
    }

    public Player(int money, int[] cards, int x, int y, boolean control)
    {
        this.x = x;
        this.y = y;
        hand = new Hand(cards, x, y + MONEY_DRAW_SHIFT * 2);
        this.money = new Money(money);
        status = Action.CHECK;
        this.control = control;
    }

    public PokerHand getStrength()
    {
        return hand.evalHandAccuracy();
    }

    public PokerHand getStrength(Hand commHand)
    {
        ArrayList<Card> cards = new ArrayList<>(commHand.getCards());
        cards.addAll(hand.getCards());
        return new Hand(cards).evalHandAccuracy();
    }

    public void bet(int amount)
    {
        game.getPot().add(amount);
        money.subtract(amount);
    }

    public void take(int amount)
    {
        money.add(game.getPot().subtract(amount));
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

    public void pickCards(Card[] cards)
    {
        for (Card card : cards) {
            hand.addCard(card);
        }
    }

    public void clearHand()
    {
        hand = new Hand(x, y + MONEY_DRAW_SHIFT * 2);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    public void paint(Graphics2D g)
    {
//        money
        g.setFont(new Font("TimesRoman", Font.PLAIN, 16));
        g.drawString("Money :" + money.getAmount(), x, y + 10);
//        player_id
//        strength
        g.drawString("Hand :" + getStrength() + "(" + Card.NUMBERS[hand.getKickers().get(0)] + ")", x, y + 10 + MONEY_DRAW_SHIFT);
        hand.paint(g);
    }
}
