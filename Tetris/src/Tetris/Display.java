package Tetris;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Display extends JPanel {
    private int row;
    private int col;
    private int boxWidth;
    private int startPos;

    private Box boxTest;
    private Figure figureTest;

    Game game;


    public Display(Game game)
    {
        this.game = game;
        boxTest = new Box(game, game.getSTART_POS(), game.getSTART_POS(), game.getBOX_SIZE(), game.getBOX_SIZE());
        figureTest = new Figure(game, 4, 4);

        row = game.getMAX_ROW();
        col = game.getMAX_COL();
        boxWidth = game.getBOX_SIZE();
        startPos = game.getSTART_POS();

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

    public Box getBoxTest()
    {
        return boxTest;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Grid Function (Eriya)
        gridDraw(g2d);
        // Draw boxes
        boxTest.paint(g2d);
        figureTest.paint(g2d);

        // Pos display
        g2d.drawString("Row :" + String.valueOf(boxTest.getRow()), getWidth()-60, 20);
        g2d.drawString("Column :" + String.valueOf(boxTest.getColumn()), getWidth()-60, 40);
    }

    public void gridDraw(Graphics2D g)
    {
        for (int i = 0; i <= col; i++) {
            //x1, y1, x2, y2
            g.drawLine(startPos + (boxWidth * i), startPos, startPos + (boxWidth * i), startPos + (boxWidth * row));
        }

        //row
        for (int i = 0; i <= row; i++) {
            g.drawLine(startPos, startPos + (boxWidth * i), startPos + (boxWidth * col), startPos + (boxWidth * i));
        }
    }



//    public static void main(String[] args) throws InterruptedException {
//        JFrame frame = new JFrame("Simple game");
//        Display game = new Display();
//        frame.add(game);
//
//
//        frame.setSize(500, 700);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        while(true)
//        {
//            game.repaint();
//            Thread.sleep(70);
//        }
//    }
}

