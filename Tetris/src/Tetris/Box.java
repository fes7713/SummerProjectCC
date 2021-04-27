package Tetris;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Box {
    int x;
    int y;
    int width;
    int height;
    Display display;
    boolean active;

    int row;
    int col;

    public Box(Display display, int x, int y, int width, int height)
    {
        this.display = display;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        active = true;
    }

    public Box(Display display, int row, int column)
    {
        this.display = display;
        this.x = column * display.getBOX_SIZE();
        this.y = row * display.getBOX_SIZE();
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
        if(active)
            display.getWidth();
    }

    public void keyPressed(KeyEvent e)
    {

    }

    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            x -= width;
            col--;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            y += height;
            row++;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            x += width;
            col++;
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            y -= height;
            row--;
        }
    }
}
