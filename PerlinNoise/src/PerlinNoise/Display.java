package PerlinNoise;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Display extends JPanel implements ChangeListener {

    private float scale;
    FastNoise noise;

    public Display()
    {
        scale = 50;
        noise = new FastNoise();
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        for(int i = 0; i < Main.HEIGHT; i++)
        {
            for(int j = 0; j < Main.HEIGHT; j++)
            {
                noise.SetFractalType(FastNoise.FractalType.RigidMulti);
//                double b = ImprovedNoise.noise(i/scale,  j/scale, 0);
                float b = noise.GetPerlinFractal(i*scale,  j*scale);
                g2d.setColor(new Color( (int)(128 + 128*b), (int)(128 + 128*b), (int)(128 + 128*b)));
                g2d.fillRect(i, j, 1, 1);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        setScale((float)((JSlider)e.getSource()).getValue());
        repaint();
    }
}
