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

    Game game;


    public Display(Game game)
    {
        this.game = game;

        row = game.getMAX_ROW();
        col = game.getMAX_COL();
        boxWidth = game.getBOX_SIZE();
        startPos = game.getSTART_POS();

        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                for(Figure fig: game.getFigures())
                {
                    if(fig.isMovable())
                        fig.ketTyped(e);
                }
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                for(Figure fig: game.getFigures())
                {
                    if(fig.isMovable())
                        fig.ketTyped(e);
                }
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
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

        // Draw figures
        for(Figure fig: game.getFigures())
        {
            fig.paint(g2d);
        }

        // Grid Function (Eriya)
        gridDraw(g2d);

        // Pos display
        g2d.drawString("Row :" + String.valueOf(game.getFigures().get(0).getTop()), getWidth()-60, 20);
        g2d.drawString("Column :" + String.valueOf(game.getFigures().get(0).getLeft()), getWidth()-60, 40);
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

