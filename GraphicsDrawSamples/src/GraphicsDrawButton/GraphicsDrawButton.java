package GraphicsDrawButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

abstract class Figure {
    protected int x, y, width, height;
    protected Color color;

    public Figure(int x, int y, int w, int h, Color c) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        color = c;
    }

    public void setSize(int w, int h) {
        width = w;
        height = h;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    abstract public void reshape(int x1, int y1, int x2, int y2);

    abstract public void paint(Graphics g);
}

class LineFigure extends Figure {
    public LineFigure(int x, int y, int w, int h, Color c) {
        super(x, y, w, h, c);
    }

    public void reshape(int x1, int y1, int x2, int y2) {
        setLocation(x1, y1);
        setSize(x2, y2);
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.drawLine(x, y, width, height);
    }
}

class CircleFigure extends Figure {
    public CircleFigure(int x, int y, int w, int h, Color c) {
        super(x, y, w, h, c);
    }

    public void reshape(int x1, int y1, int x2, int y2) {
        int newx = Math.min(x1, x2);
        int newy = Math.min(y1, y2);
        int neww = Math.abs(x1 - x2);
        int newh = Math.abs(y1 - y2);
        setLocation(newx, newy);
        setSize(neww, newh);
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.drawOval(x, y, width, height);
    }
}

class RectangleFigure extends Figure {
    public RectangleFigure(int x, int y, int w, int h, Color c) {
        super(x, y, w, h, c);
    }

    public void reshape(int x1, int y1, int x2, int y2) {
        int newx = Math.min(x1, x2);
        int newy = Math.min(y1, y2);
        int neww = Math.abs(x1 - x2);
        int newh = Math.abs(y1 - y2);
        setLocation(newx, newy);
        setSize(neww, newh);
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }
}

class DrawApplication {
    protected Vector<Figure> figures;
    protected Figure drawingFigure;
    protected String figurelabel;
    protected Color currentColor;
    protected DrawPanel drawPanel;

    public DrawApplication() {
        figures = new Vector<Figure>();
        drawingFigure = null;
        currentColor = Color.red;
        figurelabel = "rect";
    }

    public void setDrawPanel(DrawPanel c) {
        drawPanel = c;
    }

    public int getNumberOfFigures() {
        return figures.size();
    }

    public Figure getFigure(int index) {
        return (Figure) figures.elementAt(index);
    }

    public void createFigure(int x, int y) {
        Figure f = null;
        ;
        if (figurelabel == "rect") f = new RectangleFigure(x, y, 0, 0, currentColor);
        else if (figurelabel == "circ") f = new CircleFigure(x, y, 0, 0, currentColor);
        else if (figurelabel == "line") f = new LineFigure(x, y, x, y, currentColor);

        figures.addElement(f);
        drawingFigure = f;
        drawPanel.repaint();
    }

    public void reshapeFigure(int x1, int y1, int x2, int y2) {
        if (drawingFigure != null) {
            drawingFigure.reshape(x1, y1, x2, y2);
            drawPanel.repaint();
        }
    }

    public void changecolor(Color c) {
        currentColor = c;
    }

    public void changefigure(String s) {
        figurelabel = s;
    }

    public void undo() {
        figures.remove(figures.size() - 1);
        drawPanel.repaint();
    }
}


class DrawPanel extends JPanel {
    protected DrawApplication drawApplication;

    public DrawPanel(DrawApplication app) {
        setBackground(Color.white);
        drawApplication = app;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //[すべてのFigureをpaintする]
        for (int i = 0; i < drawApplication.getNumberOfFigures(); i++) {
            Figure f = drawApplication.getFigure(i);
            f.paint(g);
        }
    }
}

class DrawMouseListener implements MouseListener, MouseMotionListener {
    protected DrawApplication drawApplication;
    protected int dragStartX, dragStartY;

    public DrawMouseListener(DrawApplication a) {
        drawApplication = a;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
        if (SwingUtilities.isRightMouseButton(e) == true)
            drawApplication.undo();
        else if (SwingUtilities.isLeftMouseButton(e) == true)
            drawApplication.createFigure(dragStartX, dragStartY);
    }

    public void mouseReleased(MouseEvent e) {
        drawApplication.reshapeFigure(dragStartX, dragStartY, e.getX(), e.getY());
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        drawApplication.reshapeFigure(dragStartX, dragStartY, e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
    }
}

class Select implements ActionListener {
    DrawApplication a;

    Select(DrawApplication ap) {
        a = ap;
    }

    public void actionPerformed(ActionEvent e) { //設定の変更
        String es = e.getActionCommand();
        if (es.equals("red")) a.changecolor(Color.red);
        if (es.equals("green")) a.changecolor(Color.green);
        if (es.equals("blue")) a.changecolor(Color.blue);
        if (es.equals("rect")) a.changefigure("rect");
        if (es.equals("circ")) a.changefigure("circ");
        if (es.equals("line")) a.changefigure("line");

    }
}


class DrawMain {
    public static void main(String argv[]) {
        JFrame f = new JFrame("Draw");
        JPanel pc = new JPanel();
        JPanel pf = new JPanel();
        pc.setLayout(new GridLayout(1, 3));
        pf.setLayout(new GridLayout(1, 3));
        JButton r = new JButton("c.red");
        JButton g = new JButton("c.green");
        JButton b = new JButton("c.blue");

        JButton rect = new JButton("□");
        JButton circ = new JButton("○");
        JButton line = new JButton("─");

        r.setActionCommand("red");
        g.setActionCommand("green");
        b.setActionCommand("blue");

        rect.setActionCommand("rect");
        circ.setActionCommand("circ");
        line.setActionCommand("line");

        DrawApplication a = new DrawApplication();
        DrawPanel dp = new DrawPanel(a);
        a.setDrawPanel(dp);
        DrawMouseListener ml = new DrawMouseListener(a);
        dp.addMouseListener(ml);
        dp.addMouseMotionListener(ml);

        pc.add(r);
        pc.add(g);
        pc.add(b);

        pf.add(rect);
        pf.add(circ);
        pf.add(line);

        b.addActionListener(new Select(a));
        g.addActionListener(new Select(a));
        r.addActionListener(new Select(a));

        rect.addActionListener(new Select(a));
        circ.addActionListener(new Select(a));
        line.addActionListener(new Select(a));

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(dp, BorderLayout.CENTER);
        f.getContentPane().add(pc, BorderLayout.SOUTH);
        f.getContentPane().add(pf, BorderLayout.NORTH);

        f.setSize(400, 300);
        f.setVisible(true);
    }
}
