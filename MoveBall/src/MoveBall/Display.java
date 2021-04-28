package MoveBall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Display extends JPanel {
    private Ball ball = new Ball(this, 10, 10, 30, 0, 0);


    public Display()
    {
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                ball.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                ball.keyReleased(e);
            }
        };
        addKeyListener(kl);
        setFocusable(true);
    }

    public void animate()
    {
        ball.move();
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ball.paint(g2d);
        g2d.drawString("X :" + String.valueOf(ball.getX()), getWidth() - 40, 20);
        g2d.drawString("Y :" + String.valueOf(ball.getY()), getWidth() - 40, 40);
    }


    public static void main(String[] args)  throws InterruptedException
    {

        JFrame frame = new JFrame("Simple game");
        Display game = new Display();
        frame.add(game);
        frame.setSize(500, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while(true)
        {
            game.animate();
            game.repaint();
            Thread.sleep(10);
        }

    }
}
