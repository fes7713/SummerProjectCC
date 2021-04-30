package Poker;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player {
    Hand hand;
    Money money;
    Game game;
    Action status;
    boolean control;

    public Player(int money, boolean control)
    {
        hand = new Hand();
        this.money = new Money(money);
        status = Action.CHECK;
        this.control = control;
    }

    public void bet(int amount)
    {
        game.getPot().add(amount);
        money.subtract(amount);
    }

    public void pickCard(Card card)
    {
        hand.addCard(card);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    public void paint(Graphics2D g)
    {

    }
}
