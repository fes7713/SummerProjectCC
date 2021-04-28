package Panel_Sample;

import javax.swing.*;
import java.awt.*;

public class PanelSample2 extends JFrame {
    public static void main(String[] args)
    {
        JFrame frame = new PanelSample2("Title");
        frame.setVisible(true);
    }

    public PanelSample2(String title)
    {
        setTitle(title);
        setBounds(100, 100, 300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new FlowLayout());
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(200, 100));
        p.setBackground(Color.ORANGE);

        JLabel label = new JLabel("Number of Entry");
        JTextField text = new JTextField(5);

        p.add(label);
        p.add(text);

        Container contentPane = getContentPane();
        contentPane.add(p);
    }
}
