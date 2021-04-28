package CircleX;

import javax.swing.*;
import java.awt.*;


public class MyPanel extends JPanel {

    public void paintComponent(Graphics g) {
        for (int i = 0; i < 255; i += 20) {
            g.setColor(new Color(0, 0, 255, i));
            g.fillOval(i, i, 100, 100);
            g.setColor(new Color(255, 0, 0, i));
            g.fillOval(255 - i, i, 100, 100);
        }
    }
}
