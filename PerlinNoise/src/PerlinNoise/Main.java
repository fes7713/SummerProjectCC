package PerlinNoise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main {
    static int WIDTH = 500;
    static int HEIGHT = 500;
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Perlin Noise");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(WIDTH, HEIGHT));


        Display display = new Display();


        frame.add(display);
        frame.show();

        JFrame controller = new JFrame("Controller");
        controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controller.setBounds(300, 100, 200, 100);


        JSlider slider = new JSlider(0, 30);
        slider.addChangeListener(display);
        controller.add(slider);
        controller.show();
    }
}
