package LayoutSamples;
import java.awt.* ;
import javax.swing.* ;


// 住所・氏名・所属 の表示（その２）

public class MyProfile_FlowLayout extends JFrame {

  public MyProfile_FlowLayout() {
        super("My Profile");
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = getContentPane();
                pane.setLayout( new GridLayout(3, 1) );

                JPanel pan1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                pan1.add( new JLabel("氏名：") );
                pan1.add( new JLabel("藤村　光") );
                pane.add( pan1 );

               JPanel pan2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
               pan2.add( new JLabel("住所：") );
               JPanel pan3 = new JPanel();
                pan3.setLayout( new GridLayout(2, 1) );
                pan3.add( new JLabel("相模原市御園") );
                pan3.add( new JLabel("４丁目６－８") );
                pan2.add( pan3 );
                pane.add( pan2 );

                JPanel pan4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                pan4.add( new JLabel("所属：") );
                pan4.add( new JLabel("情報処理教育室") );
                pane.add( pan4 );

                pack();
                setVisible(true);
                }

          public static void main(String[] args) {
                new MyProfile_FlowLayout();
            }
}