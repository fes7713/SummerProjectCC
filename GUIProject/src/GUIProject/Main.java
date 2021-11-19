






























package GUIProject;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] arvs)
    {
        System.out.println("Working ");

        JFrame frame = new JFrame("GUI Practice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, 300, 500);
        frame.setLayout(new FlowLayout());

        // Panel 1
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(300, 100));
        p1.setBackground(Color.GRAY);
        frame.add(p1);

        // Panel 2
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(300, 100));
        p2.setBackground(Color.GREEN);
        frame.add(p2);

        JButton submitButton = new JButton("OK");
        JLabel firstNameLabel = new JLabel("First Name");
        JTextField firstNameEntry = new JTextField();
        firstNameEntry.setColumns(20);
        JLabel lastNameLabel = new JLabel("Last Name");
        JTextField lastNameEntry = new JTextField();
        lastNameEntry.setColumns(20);

        p1.add(firstNameLabel);
        p1.add(firstNameEntry);
        p1.add(lastNameLabel);
        p1.add(lastNameEntry);
        p1.add(submitButton);

        frame.setVisible(true); 
    }
}
