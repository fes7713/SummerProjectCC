package CircleX;


import java.awt.*;
import javax.swing.*;

public class CircleX extends JFrame {

    public CircleX(){
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyPanel panel = new MyPanel();
        this.add(panel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new CircleX();
    }
}

