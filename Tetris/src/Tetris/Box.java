package Tetris;

import java.awt.*;

public class Box {
    int x;
    int y;
    int width;
    int height;

    public Box(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void paint(Graphics2D g)
    {
        g.fillRect(x, y, width, height);
    }

    public void move()
    {

    }

}
