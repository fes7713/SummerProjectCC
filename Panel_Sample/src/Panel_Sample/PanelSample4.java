package Panel_Sample;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PanelSample4 extends JFrame {
    public static void main(String[] args)
    {
        PanelSample4 frame = new PanelSample4("Title");
        frame.setVisible(true);
    }

    public PanelSample4(String title)
    {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
        setLayout(new FlowLayout());
        setBounds(20, 20, 300, 200);
        JLabel lb = new JLabel("This is Label");
        JTextField tf = new JTextField("This is Text Field");

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.setLocation(10,10);
//        p1.setSize(250, 100);
        p1.setPreferredSize(new Dimension(200, 100));
        p1.setOpaque(true);
        p1.setBackground(Color.CYAN);
        p1.getGraphics().drawRect(10, 10, 100, 50);
//        p1.add(lb, BorderLayout.NORTH);
//        p1.add(tf, BorderLayout.SOUTH);
        add(p1);

        JLabel lb2 = new JLabel("This is Label");
        JTextField tf2 = new JTextField("This is Text Field");

        JPanel p2 = new JPanel();
        p2.setLayout(new FlowLayout());
        p2.setLocation(50,10);
//        p1.setSize(250, 100);
        p2.setPreferredSize(new Dimension(200, 100));
        p2.setOpaque(true);
        p2.setBackground(Color.CYAN);
        p2.add(lb2, BorderLayout.NORTH);
        p2.add(tf2, BorderLayout.SOUTH);
        add(p2);
    }
}
