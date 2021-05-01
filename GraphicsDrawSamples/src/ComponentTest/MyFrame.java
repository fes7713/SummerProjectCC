package ComponentTest;

import java.awt.*;
import java.awt.event.*;
public class MyFrame extends Frame implements WindowListener {
    /**コンストラクタ*/
    public MyFrame(String frameName) {
        //フレームのタイトルを設定
        setTitle(frameName);
        //スレームで発生したイベント通知用として登録
        addWindowListener(this);
    }
    /**ウィンドウをクローズした時の処理*/
    public void windowClosing(WindowEvent evt) {
        dispose();	//ウィンドウを閉じる
    }
    /*ウィンドウクローズ後の処理*/
    public void windowClosed(WindowEvent evt) {
        System.exit(0);	//プログラム終了
    }
    /**その他のウィンドウ処理*/
    public void windowDeiconified(WindowEvent evt) { }
    public void windowIconified(WindowEvent evt) { }
    public void windowOpened(WindowEvent evt) { }
    public void windowActivated(WindowEvent evt) { }
    public void windowDeactivated(WindowEvent evt) { }
    /**main()メソッド*/
    public static void main(String[] args) {
        MyFrame frame = new MyFrame("MyFrameTest");
        frame.setSize(200,100);
        frame.setVisible(true);
    }
}
