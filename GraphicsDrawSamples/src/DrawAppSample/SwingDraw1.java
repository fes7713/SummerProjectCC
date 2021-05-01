package DrawAppSample;

/**
 *JPanelに楕円を表示する。SwingDraw.java
 */
import java.awt.*;
import javax.swing.*;
public class SwingDraw1 extends JPanel {
    private final int WIDTH = 400; //JPanelの幅と高さを定義
    private final int HEIGHT = 200;
    private int x=0; //楕円のx座標は0から
    /**コンストラクタ*/
    public SwingDraw1() {
        super();    //JPanelのコンストラクタを呼び出す
        setBackground(Color.white); //JPanelの背景色は白
        //JPanelの幅と高さを設定
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }
    /**グラフィックス描画処理*/
    public void paintComponent(Graphics g) {
        //コンポーネント全体を白で描画
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //200ミリ秒休止
        try {
            Thread.sleep(200);
        }
        catch(InterruptedException ex) {
            System.err.println(ex);
        }
        //x座標を進めて楕円を描画
        x+=10;
        g.setColor(Color.red);
        g.fillOval(x, 0, 30, 30);
        g.setColor(Color.green);
        g.fillOval(x, 50, 30, 30);
        g.setColor(Color.blue);
        g.fillOval(x, 100, 30, 30);
        g.setColor(Color.pink);
        g.fillOval(x, 150, 30, 30);
    }
    /**main()*/
    public static void main(String[] args) {
        SwingDraw1 sample = new SwingDraw1();
        JFrame frame = new JFrame("SwingDraw1");
        //「閉じる」ボタンが押された場合はフレームを閉じる
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //コンテンツ・ペインにJPanelをadd
        frame.getContentPane().add(sample, "Center");
        frame.pack();
        frame.setVisible(true);
        //右端に行くまでpaintComponent()を呼び出す。
        while(sample.x < sample.WIDTH) {
            sample.repaint();
        }
    }
}
