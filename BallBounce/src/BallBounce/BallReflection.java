package BallBounce;

//Original work
//http://www.groovy-number.com/java/sample/BallReflection.html
import java.awt.Color;
import java.lang.String;
import javax.swing.JFrame;
import javax.swing.Timer;

public class BallReflection extends JFrame {
    final static int fps = 30;

    public BallReflection() {
        DrawPanel panel = new DrawPanel();
        add(panel);

        new Timer(fps, panel).start();
    }

    public static void main(String[] A00) {
        JFrame frame = new BallReflection();
        frame.setTitle("反射するボール");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setBackground(Color.white);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}