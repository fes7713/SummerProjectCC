package Tetris;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Display extends JPanel {
    private int row;
    private int col;
    private int INITIAL_ROW = 20;
    private int INITIAL_COL = 10;

    private Box boxTest;


    public Display()
    {
        boxTest = new Box(20, 20, 100, 100);
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        addKeyListener(kl);
        setFocusable(true);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Grid Function (Eriya)

        // Draw boxes
        boxTest.paint(g2d);
    }


    public int[] getGridSize()
    {
        return new int[]{row, col};
    }


    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Simple game");
        Display game = new Display();
        frame.add(game);


        frame.setSize(500, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while(true)
        {
            game.repaint();
            Thread.sleep(10);
        }
    }
}

