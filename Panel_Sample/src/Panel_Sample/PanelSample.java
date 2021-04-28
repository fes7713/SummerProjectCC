package Panel_Sample;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class PanelSample extends JFrame {
    public static void main(String[] arg)
    {
        PanelSample frame = new PanelSample("Title");
        frame.setVisible(true);
    }

    PanelSample(String title)
    {
        setTitle(title);
        setBounds(100, 100, 300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel p1 = new JPanel();
        p1.setBackground(Color.BLUE);
        p1.setPreferredSize(new Dimension(200, 100));
        BevelBorder border = new BevelBorder(BevelBorder.RAISED);
        p1.setBorder(border);

        JPanel p2 = new JPanel();
        p2.setBackground(Color.ORANGE);
        p2.setPreferredSize(new Dimension(200, 100));

        Container contentPane = getContentPane();
        contentPane.add(p1, BorderLayout.NORTH);
        contentPane.add(p2, BorderLayout.SOUTH);
    }

}
