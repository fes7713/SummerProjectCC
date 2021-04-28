package ImageDisplay;

import javax.swing.*;
import java.awt.*;

class MyPanel extends JPanel {
    Image image;

    public MyPanel(){
        image = Toolkit.getDefaultToolkit().getImage("assets/2C.png");
    }

    public void paintComponent(Graphics g){
        if (image != null){

            int w = image.getWidth(this);
            int h = image.getHeight(this);
            g.drawImage(image, 0, 0, w/5, h/5, this);
        }
    }
}