package BallBounce;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

public class DrawPanel extends JPanel implements ActionListener {
    static final int width = 300;
    static final int height = 200;

    Ball ball = new Ball(50);

    public DrawPanel() {
        setBackground(Color.white);
        setPreferredSize(new Dimension(width, height));
    }

    public void actionPerformed(ActionEvent event) {
        ball.move();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphic = (Graphics2D)g;
        graphic.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        ball.draw(graphic);
    }
}