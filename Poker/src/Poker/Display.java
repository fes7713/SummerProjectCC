package Poker;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Display extends JPanel {
    static final int SCALE_CONSTANT = 7;
    static final int CARD_WIDTH = 100;
    static final int CARD_HEIGHT = 150;
    Hand hand1;
    Player player1;
    public Display()
    {
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Test Money
//                if(e.getKeyCode() == KeyEvent.VK_UP)
//                    player1.addMoney(10);

                // Test Add Cards
//                player1.pickCard(new Card(43, 0, 0));

                // Test Change Cards
                player1.clearHand();
                Deck d1 = new Deck();
                d1.shuffle();
                player1.pickCards(new Card[]{d1.pop(), d1.pop(), d1.pop(), d1.pop(), d1.pop(), d1.pop(), d1.pop()});
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        addKeyListener(kl);
        setFocusable(true);

        hand1 = new Hand(new int[]{0, 1, 2, 3, 4}, 20, 20);
        player1 = new Player(20000, new int[]{0, 13},20, 180, false);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        hand1.paint(g2d);
        player1.paint(g2d);
    }

}
