package Tetris;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Display extends JPanel {
    private final int row;
    private final int col;
    private final int boxWidth;
    // Start Pos defines top and left margin.
    private final int startPos;

    Game game;

    public Display(Game game)
    {
        this.game = game;

        row = game.getMAX_ROW();
        col = game.getMAX_COL();
        boxWidth = game.getBOX_SIZE();
        startPos = game.getSTART_POS();


        // Adding key listener to the display.
        // When arrow keys are pressed then update figures' coordinates.
        // Add anything that you want to update when key is pressed.
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // When key is pressed, update all of figures.
                for(Figure fig: game.getFigures())
                {
                    if(fig.isMovable())
                        fig.ketTyped(e);
                }
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

        // Pos display // Test String Draw, delete in the release edition.
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
}

