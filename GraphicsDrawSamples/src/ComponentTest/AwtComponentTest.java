package ComponentTest;

/**
 * 各種コンポネートを配置したサンプル
 */
import java.awt.*;
import java.awt.event.*;
public class AwtComponentTest extends Canvas implements ActionListener, ItemListener {
    private Panel panel, cbPanel, zukeiPanel;
    private Button startButton, clearButton, quitButton;
    private Checkbox drawCheckbox, fillCheckbox;
    private List zukeiList;
    private Choice colorChoice;
    private boolean clearFlag;
    private Color color;
    /** コンストラクタ */
    public AwtComponentTest() {
        setSize(400, 300);
        //パネルにラベルとボタンの配置
        panel = new Panel();
        Label label = new Label("Push Button");
        startButton = new Button("Start");
        clearButton = new Button("Clear");
        quitButton = new Button("Quit");
        panel.add(label);
        panel.add(startButton);
        panel.add(clearButton);
        panel.add(quitButton);
        //チェックボックスをグループ化して、パネルに配置
        CheckboxGroup cbg = new CheckboxGroup();
        cbPanel = new Panel();
        drawCheckbox = new Checkbox("Draw", cbg, true);
        fillCheckbox = new Checkbox("Fill", cbg, false);
        cbPanel.add(drawCheckbox);
        cbPanel.add(fillCheckbox);
        //図形を指定するリストを生成し、パネルに配置
        zukeiPanel = new Panel();
        zukeiList = new List(2);
        zukeiList.add("Oval");
        zukeiList.add("Rect");
        zukeiList.select(0);	//一番の項目（oval）を選択した状態にする
        zukeiPanel.add(zukeiList);
        //色を指定するチョイスを生成
        colorChoice = new Choice();
        colorChoice.add("Blue");
        colorChoice.add("Red");

        clearFlag = false;
        color = Color.blue;
        //イベントの通知先を登録
        startButton.addActionListener(this);
        clearButton.addActionListener(this);
        quitButton.addActionListener(this);
        drawCheckbox.addItemListener(this);
        fillCheckbox.addItemListener(this);
        zukeiList.addItemListener(this);
        colorChoice.addItemListener(this);
        drawCheckbox.addItemListener(this);
    }
    /** 再描画が必要な場合の処理 */
    public  void update(Graphics g) {
        int x, y, width, height;
        // clearFlagがtrueなら設定色でクリア
        if (clearFlag) {
            g.setColor(Color.green);
            g.fillRect(0, 0, getSize().width, getSize().height);
            clearFlag = false;
            return;
        }
        //メニューで設定された色
        g.setColor(color);

        //乱数で図形の位置指定
        x = (int)(Math.random()*400);
        y = (int)(Math.random()*300);
        width = (int)(Math.random()*200) + 10;
        height = (int)(Math.random()*150) + 10;
        // zukeiListの項目文字列で楕円か矩形かをチェック
        // drawCheckboxの状態をチェックして塗りつぶしか否かを決定
        if(zukeiList.getSelectedItem().equals("Oval")) {

            if(drawCheckbox.getState()) {
                g.drawOval(x, y, width, height);
            }
            else {
                g.fillOval(x, y, width, height);
            }
        }
        else if (zukeiList.getSelectedItem().equals("Rect")) {
            if(drawCheckbox.getState()) {
                g.drawRect(x, y, width, height);
            }
            else {
                g.fillRect(x, y, width, height);
            }
        }
    }
    /** プログラム起動時と絵の復活時の処理 */
    public void paint(Graphics g) {
        // paintが呼ばれたときは画面クリア
        g.setColor(Color.green);
        g.fillRect(0, 0, getSize().width, getSize().height);
    }
    /** メニューのイベントを処理 */
    public void actionPerformed(ActionEvent evt) {
        Object obj = evt.getSource();
        if(obj.equals(startButton)) {
            repaint();	//グラフィックス描画
        }
        else if (obj.equals(quitButton)) {
            System.exit(0);		//システム終了
        }
        else if (obj.equals(clearButton)) {
            clearFlag = true;
            repaint();
        }
    }
    /** itemの状態が変更された場合のイベント処理*/
    public void itemStateChanged(ItemEvent evt) {
        Object obj = evt.getSource();
        if (obj.equals(zukeiList)) {
            repaint();
        }
        else if(obj.equals(colorChoice)) {
            //項目番号でチェック
            if (colorChoice.getSelectedIndex() == 0) {
                color = Color.blue;
            }
            else {
                color = Color.red;
            }
        }
    }
    /** main() */
    public static void main(String[] args) {
        AwtComponentTest draw = new AwtComponentTest();
        MyFrame frame = new MyFrame("ComponentTest");
        //フレームにコンポーネント（コンテナ）を配置
        frame.add(draw, "Center");
        frame.add(draw.panel, "North");
        frame.add(draw.cbPanel, "South");
        frame.add(draw.zukeiPanel, "West");
        frame.add(draw.colorChoice, "East");
        frame.pack();
        frame.setVisible(true);
    }
}

