package BallBounce;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.Math;

public class Ball {
    int x;
    int y;
    int radius;
    int speedX;
    int speedY;

    Ball(int A00) {
        this.radius = A00;

        x = (int)(Math.random() * (DrawPanel.width - radius));
        y = (int)(Math.random() * (DrawPanel.height - radius));

        // 進行方向はランダム
        if (Math.random() > 0.5) {
            speedX = 1;
        } else {
            speedX = -1;
        }
        if (Math.random() > 0.5) {
            speedY = 1;
        } else {
            speedY = -1;
        }
    }

    void move() {
        x += speedX;
        y += speedY;

        // 壁に衝突すれば反射
        if (x >= (DrawPanel.width - radius) || x <= 0) {
            speedX = -speedX;
        }
        if (y >= (DrawPanel.height - radius) || y <= 0) {
            speedY = -speedY;
        }
    }

    void draw(Graphics g) {
        g.setColor(Color.orange);
        g.fillOval(x, y, radius, radius);
    }
}