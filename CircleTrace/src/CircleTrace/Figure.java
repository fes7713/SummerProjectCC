package CircleTrace;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Figure {
    int x;
    int y;
    int xSpeed;
    int ySpeed;
    Display display;

    public Figure()
    {
        display = null;
        x = 0;
        y = 0;
        xSpeed = 0;
        ySpeed = 0;
    }

    public Figure(Display display, int x, int y, int xSpeed, int ySpeed) {
        this.display = display;
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public Figure(Display display, int x, int y) {
        this.display = display;
        this.x = x;
        this.y = y;
        this.xSpeed = 0;
        this.ySpeed = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move()
    {
        if(x + xSpeed >= 0 && x + xSpeed <= display.getWidth())
            x += xSpeed;
        if(y + ySpeed >= 0 && y + ySpeed <= display.getHeight())
            y += ySpeed;
    }

    public void keyReleased(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            xSpeed = 0;
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            xSpeed = 0;
        else if(e.getKeyCode() == KeyEvent.VK_UP)
            ySpeed = 0;
        else if(e.getKeyCode() == KeyEvent.VK_DOWN)
            ySpeed = 0;
    }

    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            xSpeed = -5;
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            xSpeed = 5;
        else if(e.getKeyCode() == KeyEvent.VK_UP)
            ySpeed = -5;
        else if(e.getKeyCode() == KeyEvent.VK_DOWN)
            ySpeed = 5;
    }

    public void paint(Graphics g)
    {

    }

}
