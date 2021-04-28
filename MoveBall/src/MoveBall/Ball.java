package MoveBall;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Ball {
    private int x;
    private int y;
    private int radius;
    private int xSpeed;
    private int ySpeed;
    private Display display;

    public Ball(Display display, int m_x, int m_y, int m_radius, int m_xSpeed, int m_ySpeed)
    {
        this.display = display;
        x = m_x;
        y = m_y;
        radius = m_radius;
        xSpeed = m_xSpeed;
        ySpeed = m_ySpeed;
    }

    public void move()
    {
        if(x + xSpeed >= 0 && x + xSpeed <= display.getWidth() - radius)
            x += xSpeed;
        if(y + ySpeed >= 0 && y + ySpeed <= display.getHeight() - radius)
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

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void paint(Graphics2D g)
    {
        g.fillOval(x, y, radius, radius);
    }


}
