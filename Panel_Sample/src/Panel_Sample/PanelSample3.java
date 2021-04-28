package Panel_Sample;

import javax.swing.*;
import java.awt.*;

public class PanelSample3 extends JFrame {
    public static void main(String args[]){
        PanelSample3 frame = new PanelSample3("title");
        frame.setVisible(true);
    }

    public PanelSample3(String title)
    {
        setTitle(title);
        setBounds(100, 100, 300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new FlowLayout());

        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(200, 100));
        p.setBackground(Color.ORANGE);
        p.setLayout(new BorderLayout());

        JButton btn1 = new JButton("NORTH");
        JButton btn2 = new JButton("South");
        p.add(btn1,BorderLayout.NORTH);
        p.add(btn2, BorderLayout.CENTER);

        Container contentPane = getContentPane();
        contentPane.add(p);

    }
}
