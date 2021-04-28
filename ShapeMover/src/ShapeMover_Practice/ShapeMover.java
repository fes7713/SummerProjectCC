package ShapeMover_Practice;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ShapeMover {

    public ShapeMover() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Shape Mover");

        initComponents(frame);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String s[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ShapeMover();
            }
        });

    }

    private void initComponents(JFrame frame) {
        frame.getContentPane().add(new DragPanel());
    }
}

class DragPanel extends JPanel {

    Rectangle rect = new Rectangle(0, 0, 100, 50);
    boolean pressIn = false;
    private int preX, preY;
    private Dimension dim = new Dimension(400, 300);

    public DragPanel() {
        setBackground(Color.white);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                if(rect.contains(e.getX(), e.getY()))
                    pressIn = true;

                if(pressIn)
                {
                    preX = e.getX() - (int)rect.getX();
                    preY = e.getY() - (int)rect.getY();
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(!rect.contains(e.getX(), e.getY()))
                    pressIn = false;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                if(pressIn)
                {
                    rect.setLocation(e.getX() - preX, e.getY() - preY);
                    System.out.println(rect.getX());
                }
                repaint();
            }
        };

        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
        rect.setLocation(50, 50);
    }

    @Override
    public Dimension getPreferredSize() {
        return dim;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(rect);
    }
}