package CircleTrace;

import java.awt.*;
import java.util.Stack;

public class Ball extends Figure{
    int radius;
    final int INITIAL_RADIUS = 20;


    public Ball() {
        this.radius = INITIAL_RADIUS;
    }

    public Ball(Display display, int x, int y, int xSpeed, int ySpeed, int radius) {
        super(display, x, y, xSpeed, ySpeed);
        this.radius = radius;
    }

    public Ball(Display display, int x, int y, int radius)
    {
        super(display, x, y);
        this.radius = radius;
    }

    public void move()
    {
        if(x + xSpeed >= 0 && x + xSpeed <= display.getWidth() - radius)
            x += xSpeed;
        if(y + ySpeed >= 0 && y + ySpeed <= display.getHeight() - radius)
            y += ySpeed;
    }

    public void paint(Graphics2D g)
    {
        g.fillOval(x, y, radius, radius);
    }
}
