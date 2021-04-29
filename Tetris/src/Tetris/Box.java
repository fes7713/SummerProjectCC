package Tetris;

import java.awt.*;
import java.awt.event.KeyEvent;

// Done
public class Box {
    int x;
    int y;
    int width;
    int height;
    Game game;
    boolean active;

    int row;
    int col;

    public Box(Game game, int x, int y, int width, int height)
    {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        active = true;
    }

    public Box(Game game, int row, int column)
    {
        this.game = game;
        width = game.getBOX_SIZE();
        height = game.getBOX_SIZE();
        setCoordinate(row, column);
        active = true;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return col;
    }

    public void setCoordinate(int row, int col)
    {
        x = col * game.getBOX_SIZE() + game.getSTART_POS();
        y = row * game.getBOX_SIZE() + game.getSTART_POS();
    }
    public void setActive(boolean b)
    {
        active = b;
    }

    public boolean getActive()
    {
        return active;
    }
    public void paint(Graphics2D g)
    {
        g.fillRect(x, y, width, height);
    }

    public void move()
    {

    }

    public void ketTyped(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            setCoordinate(row, --col);
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            setCoordinate(++row, col);
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            setCoordinate(row, ++col);
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            setCoordinate(--row, col);
        }
    }
}
