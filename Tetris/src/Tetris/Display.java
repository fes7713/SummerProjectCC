package Tetris;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Display extends JPanel {
    private int row;
    private int col;
    private int MAX_ROW = 20;
    private int MAX_COL = 10;
    private final int BOX_SIZE = 30;
    private final int  START_POS = 20;

    private Box boxTest;


    public Display()
    {
        boxTest = new Box(this, START_POS, START_POS, BOX_SIZE, BOX_SIZE);
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                boxTest.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
                boxTest.keyReleased(e);
            }
        };
        addKeyListener(kl);
        setFocusable(true);
        getWidth();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Grid Function (Eriya)
        gridDraw(g2d);
        // Draw boxes
        boxTest.paint(g2d);

        // Pos display
        g2d.drawString("Row :" + String.valueOf(boxTest.getColumn()), getWidth()-60, 20);
        g2d.drawString("Column :" + String.valueOf(boxTest.getRow()), getWidth()-60, 40);
    }

    public void gridDraw(Graphics2D g)
    {
        for (int i = 0; i <= MAX_COL; i++) {
            //x1, y1, x2, y2
            g.drawLine(START_POS + (BOX_SIZE * i), START_POS, START_POS + (BOX_SIZE * i), START_POS + (BOX_SIZE * MAX_ROW));
        }

        //row
        for (int i = 0; i <= MAX_ROW; i++) {
            g.drawLine(START_POS, START_POS + (BOX_SIZE * i), START_POS + (BOX_SIZE * MAX_COL), START_POS + (BOX_SIZE * i));
        }
    }


    public int getMAX_ROW() {
        return MAX_ROW;
    }

    public int getMAX_COL() {
        return MAX_COL;
    }

    public int getBOX_SIZE() {
        return BOX_SIZE;
    }

    public int getSTART_POS() {
        return START_POS;
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
            Thread.sleep(70);
        }
    }
}

