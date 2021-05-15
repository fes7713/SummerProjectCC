package PerlinNoise;

import javax.swing.*;
import java.awt.*;

public class Main {
    static int WIDTH = 300;
    static int HEIGHT = 300;
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Perlin Noise");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(WIDTH, HEIGHT));

        JPanel p1 = new JPanel(){
            public void paint(Graphics g)
            {
                super.paint(g);

                Graphics2D g2d = (Graphics2D) g;

                for(int i = 0; i < Main.HEIGHT; i++)
                {
                    for(int j = 0; j < Main.HEIGHT; j++)
                    {
                        double b = ImprovedNoise.noise(i/50.0f,  j/50.0f, 0);
                        g2d.setColor(new Color( (int)(128 + 128*b), (int)(128 + 128*b), (int)(128 + 128*b)));
                        g2d.fillRect(i, j, 1, 1);
                    }
                }
            }
        };

        frame.add(p1);
        frame.show();
    }
}
