package Poker;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private Hand hand;
    private Hand communityCards;
    private List<Integer> kickers;
    private Money money;
    private Game game;
    private Action status;
    boolean control;
    private int x;
    private int y;

    static final int PLAYER_HEIGHT = Display.STRING_LINE_SHIFT * 3 + Card.CARD_HEIGHT;
    static final int PLAYER_WIDTH = Card.CARD_WIDTH * Game.HAND_SIZE + Display.PADDING;

    public Player(Hand commCards, int money, boolean control)
    {
        communityCards = commCards;
        hand = new Hand();
        this.money = new Money(money);
        status = Action.CHECK;
        this.control = control;
    }

    public Player(Hand commCards, int money, int x, int y, boolean control)
    {
        communityCards = commCards;
        this.x = x;
        this.y = y;
        hand = new Hand(x, y + Display.STRING_LINE_SHIFT * 2);
        this.money = new Money(money);
        status = Action.CHECK;
        this.control = control;
    }

    public Player(Hand commCards, int money, int[] cards, int x, int y, boolean control)
    {
        communityCards = commCards;
        this.x = x;
        this.y = y;
        hand = new Hand(cards, x, y + Display.STRING_LINE_SHIFT * 2);
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
        Hand h = new Hand(cards);
        PokerHand pk = h.evalHandAccuracy();
        kickers = h.getKickers();
        return pk;
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
        hand = new Hand(x, y + Display.STRING_LINE_SHIFT * 2);
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

        g.drawString("Money :" + money.getAmount(), x, y + 10);
//        player_id
//        strength

        g.drawString("Hand :" + getStrength(communityCards) + "(" + Card.NUMBERS[kickers.get(0)] + ")", x, y + 10 + Display.STRING_LINE_SHIFT);
        hand.paint(g);
    }
}
