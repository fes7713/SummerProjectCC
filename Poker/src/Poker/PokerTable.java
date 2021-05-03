package Poker;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

public class PokerTable extends JPanel {
    static final int STRING_LINE_SHIFT = 20;
    static final int PADDING = 20;
    static final int WIDTH = Math.max(Player.PLAYER_WIDTH * Game.nPlayers, Game.COMMUNITY_CARDS_SIZE * Card.CARD_WIDTH) + PADDING * 2;
    static  final int HEIGHT = PokerTable.STRING_LINE_SHIFT * 5 + Card.CARD_HEIGHT * 2 + PADDING * 2;
    private final Game game;

    public PokerTable(Game g)
    {
        game = g;

        setBackground(new Color(53, 101, 77));
        setPreferredSize(new Dimension(WIDTH,HEIGHT));

        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                    game.next();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        addKeyListener(kl);
        setFocusable(true);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//        hand1.paint(g2d);
//        player1.paint(g2d);
        game.paint(g2d);
    }
}

class Controller extends JPanel implements ActionListener, ChangeListener
{
    private Game game;
    static final int scaleConst = 100;
    private JPanel actionPanel, customBetPanel;
    private JButton foldButton, checkButton, callButton, raiseButton, btnAllIn, addSliderButton, subSliderButton;
    private JLabel betAmountLabel;
    private JFormattedTextField betTextEntry;
    private JSlider betSlider;

    private Money money;
    NumberFormatter numberFormatter;

    public Controller(Game g) {
        game = g;
        money = new Money();
        setBackground(new Color(53, 101, 77));
        setPreferredSize(new Dimension(400,200));
        setLayout(new FlowLayout());


        actionPanel = new JPanel(new GridLayout(1, 5));
        foldButton = new JButton("Fold");
        checkButton = new JButton("Check");
        callButton = new JButton("Call");
        raiseButton = new JButton("Raise");
        btnAllIn = new JButton("All-In");
        // Add buttons
        actionPanel.add(foldButton);
        actionPanel.add(checkButton);
        actionPanel.add(callButton);
        actionPanel.add(raiseButton);
        actionPanel.add(btnAllIn);

        customBetPanel = new JPanel();
        customBetPanel.setPreferredSize(new Dimension(400, 100));
        customBetPanel.setBackground(new Color(53, 101, 77));
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        customBetPanel.setLayout(layout);

        betAmountLabel = new JLabel("Amount");
        betAmountLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(0, 5, 0, 20);
        gbc.anchor = GridBagConstraints.LINE_END;
        layout.setConstraints(betAmountLabel, gbc);

        NumberFormat longFormat = NumberFormat.getIntegerInstance();
        numberFormatter = new NumberFormatter(longFormat);
        numberFormatter.setValueClass(Long.class); //optional, ensures you will always get a long value
        numberFormatter.setAllowsInvalid(false); //this is the key!!
        numberFormatter.setMinimum(0l); //Optional
        betTextEntry = new JFormattedTextField(numberFormatter);
        betTextEntry.setValue(0);
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        layout.setConstraints(betTextEntry, gbc);

        subSliderButton = new JButton("-");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        layout.setConstraints(subSliderButton, gbc);

        betSlider = new JSlider(game.getSmallBlind()/scaleConst,
                game.getCurrentPlayerMoney()/scaleConst,
                game.getSmallBlind()/scaleConst);
        betSlider.setBackground(new Color(53, 101, 77));
        betSlider.setMajorTickSpacing(game.getCurrentPlayerMoney()/scaleConst/5);
        betSlider.setMinorTickSpacing(game.getCurrentPlayerMoney()/scaleConst/10);
        betSlider.setPaintTicks(true);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 8;
        layout.setConstraints(betSlider, gbc);

        addSliderButton = new JButton("+");
        gbc.gridx = 9;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(addSliderButton, gbc);
//
//        // Add components;
        customBetPanel.add(betAmountLabel);
        customBetPanel.add(subSliderButton);
        customBetPanel.add(betSlider);
        customBetPanel.add(addSliderButton);
        customBetPanel.add(betTextEntry);


        add(actionPanel);
        add(customBetPanel);

        foldButton.setActionCommand("Fold");
        checkButton.setActionCommand("Check");
        callButton.setActionCommand("Call");
        raiseButton.setActionCommand("Raise");
        btnAllIn.setActionCommand("All-In");

        foldButton.addActionListener(game);
        checkButton.addActionListener(game);
        callButton.addActionListener(game);
        raiseButton.addActionListener(game);
        btnAllIn.addActionListener(game);
        addSliderButton.addActionListener(this);
        subSliderButton.addActionListener(this);
        betSlider.addChangeListener(this);
        betTextEntry.addActionListener(this);
    }

    public int getBetMoney()
    {
        return money.getAmount();
    }

    public void initBetButton()
    {
        callButton.setText("Bet");
        callButton.setActionCommand("Bet");
    }

    public void initCallButton()
    {
        callButton.setText("Call");
        callButton.setActionCommand("Call");
    }

    public void updatedMoney()
    {
        betTextEntry.setValue(money.getAmount());
        betSlider.setValue(money.getAmount()/scaleConst);
    }

    public void setMinimum(int minimum)
    {
        numberFormatter.setMinimum(minimum);
        betSlider.setMinimum(minimum/scaleConst);
        if(minimum > money.getAmount())
            money.setAmount(minimum);
        updatedMoney();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if(obj.equals(addSliderButton))
        {
            money.add(game.getSmallBlind());
            updatedMoney();
        }
        else if(obj.equals(subSliderButton))
        {
            money.subtract(game.getSmallBlind());
            updatedMoney();
        }
        else if(obj.equals(betTextEntry))
        {
            money.setAmount((long)betTextEntry.getValue());
            updatedMoney();
        }

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object obj = e.getSource();

        if(obj.equals(betSlider))
        {
            money.setAmount(betSlider.getValue()*scaleConst);
            updatedMoney();
        }
    }
}
