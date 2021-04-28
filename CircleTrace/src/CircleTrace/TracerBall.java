package CircleTrace;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.ListIterator;

public class TracerBall extends Ball{
    ArrayDeque<Ball> tracerBalls;
    int capacity;

    public TracerBall(int capacity) {
        this.tracerBalls = new ArrayDeque<>();
        this.capacity = capacity;
    }

    public TracerBall(Display display, int x, int y, int xSpeed, int ySpeed, int radius, int capacity) {
        super(display, x, y, xSpeed, ySpeed, radius);
        this.tracerBalls = new ArrayDeque<>();
        this.capacity = capacity;
    }

    public void addBall(int x, int y)
    {
        if(tracerBalls.size() >= capacity)
            tracerBalls.pollLast();
        tracerBalls.addFirst(new Ball(display, x, y, radius));
    }

    public void move()
    {
        if(xSpeed != 0 || ySpeed != 0)
            addBall(x, y);
        else
            tracerBalls.pollLast();

        super.move();
    }

    public void paint(Graphics2D g)
    {

        Iterator<Ball> iter = tracerBalls.iterator();
        int i = 0;
        int size = tracerBalls.size();
        for(Ball b :tracerBalls)
        {
            g.setColor(new Color(255, 0, 0, (int)(((size - i)/(float)size*255)*0.5)));
            i++;
            b.paint(g);
        }
        g.setColor(new Color(0, 0, 0));
        super.paint(g);
    }
}
