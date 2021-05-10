package Poker;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.Arrays;

public class PokerTable extends JPanel {
    static final int PADDING = 20;
    static final int WIDTH = Math.max(Player.WIDTH * Game.nPlayers, Game.COMMUNITY_CARDS_SIZE * Card.CARD_WIDTH) + PADDING * 2;
    static  final int HEIGHT = Card.CARD_HEIGHT + PADDING * 5 + Player.HEIGHT;
//    static final Color PRIMARY_COLOR = new Color(109, 166, 68);
//    static final Color PRIMARY_COLOR_VARIANT = new Color(127, 191, 80);
//    static final Color SECONDARY_COLOR = new Color(242, 183, 5);
//    static final Color SECONDARY_COLOR_VARIANT = new Color(217, 164, 4);
    static final Color PRIMARY_COLOR = new Color(0, 140, 112);
    static final Color PRIMARY_COLOR_VARIANT = new Color(20, 217, 177);
    static final Color SECONDARY_COLOR = new Color(255, 85, 23);
    static final Color SECONDARY_COLOR_VARIANT = new Color( 191, 52, 2);
    private final Game game;

    public PokerTable(Game g)
    {
        game = g;
        setBackground(PRIMARY_COLOR);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        game.paint(g2d);
    }
}


class Controller extends JPanel implements ActionListener, ChangeListener
{
    private Game game;
    static final int scaleConst = 100;
    private JPanel actionPanel, customBetPanel;
    private JButton foldButton, checkButton, callButton, btnAllIn, addSliderButton, subSliderButton;
    private JLabel betAmountLabel;
    private JFormattedTextField betTextEntry;
    private JSlider betSlider;

    private Money money;
    NumberFormatter numberFormatter;

    public Controller(Game g) {
        game = g;
        money = new Money();
        setBackground(PokerTable.PRIMARY_COLOR);
        setPreferredSize(new Dimension(400,200));
        setLayout(new FlowLayout());


        actionPanel = new JPanel(new GridLayout(1, 4));
        foldButton = new JButton("Fold");
        checkButton = new JButton("Check");
        callButton = new JButton("Call");
        btnAllIn = new JButton("All-In");
        // Add buttons
        actionPanel.add(foldButton);
        actionPanel.add(checkButton);
        actionPanel.add(callButton);
        actionPanel.add(btnAllIn);

        customBetPanel = new JPanel();
        customBetPanel.setPreferredSize(new Dimension(400, 100));
        customBetPanel.setBackground(PokerTable.PRIMARY_COLOR);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        customBetPanel.setLayout(layout);

        betAmountLabel = new JLabel("To Call");
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
        betSlider.setBackground(PokerTable.PRIMARY_COLOR);
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
        btnAllIn.setActionCommand("All-In");

        foldButton.addActionListener(game);
        checkButton.addActionListener(game);
        callButton.addActionListener(game);
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

    public void initNextButton()
    {
        callButton.setText("Next");
        callButton.setActionCommand("Next");
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

    public void initController()
    {
        int min, max, value;
        max = game.getCurrentPlayerMoney();
        if(game.getCallTotal() > game.getCurrentPlayerMoney() || game.getSmallBlind() > game.getCurrentPlayerMoney())
            min = game.getCurrentPlayerMoney();
        else if(game.getCallTotal() != 0)
            min = game.getCallTotal() - game.getCurrentPlayerBetTotal();
        else
            min = game.getSmallBlind();
        value = min;

        betSlider.setMinimum(min/scaleConst);
        numberFormatter.setMinimum(min);
        betSlider.setMaximum(max/scaleConst);
        numberFormatter.setMaximum(max);
        betSlider.setValue(value/scaleConst);
        betTextEntry.setValue(value);
        money.setAmount(value);
    }

    public void doCall()
    {
        callButton.doClick();
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
            if(betSlider.getValue() == betSlider.getMinimum())
            {
                if(game.getCallTotal() == 0)
                {
                    callButton.setText("Bet");
                    callButton.setActionCommand("Bet");
                }
                else
                {
                    callButton.setText("Call");
                    callButton.setActionCommand("Call");
                }
            }
            else
            {
                callButton.setText("Raise");
                callButton.setActionCommand("Raise");
            }
            money.setAmount(betSlider.getValue()*scaleConst);
            updatedMoney();
        }
    }
}


class GameInfoPanel extends JPanel
{
    private Game game;
    static final int PADDING = 20;
    static final int WIDTH = Math.max(Player.WIDTH * Game.nPlayers, Game.COMMUNITY_CARDS_SIZE * Card.CARD_WIDTH) + PADDING * 2;
    static  final int HEIGHT = Card.CARD_HEIGHT * 2 + PADDING * 8;


    public GameInfoPanel(Game g) {
        game = g;
        setBackground(PokerTable.PRIMARY_COLOR);
        setPreferredSize(new Dimension(200, HEIGHT));
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 14));
        g2d.setColor(Color.WHITE);

        g2d.drawString(game.getStage(), PADDING, PADDING);
        String[] playerNames = game.playerNames();
        Action[] playerStatuses = game.playerStatuses();

        g2d.drawString("Pot : " + game.getPotAmount(), PADDING * 5, PADDING);
        g2d.drawString("Call Value : " + game.getCallTotal(), PADDING, PADDING*3);
        for(int i = 0; i < Game.nPlayers; i++)
        {
            g2d.drawString(playerNames[i] + ": " + playerStatuses[i], PADDING, PADDING*5+ PADDING * i * 2);
        }
    }
}

class PlayerInfoPanel extends JPanel
{
    static final int PADDING = 22;
    static final int BAR_LENGTH = 130;
    static final int BAR_HEIGHT = 16;
    static final int ARC_RADIUS = 10;
    private Player player;

    static final int trials = 10000;

    public PlayerInfoPanel(Player player) {
        this.player = player;
        setPreferredSize(new Dimension(400, 360));
        setBackground(PokerTable.PRIMARY_COLOR);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        double[] statsHand = player.handStrengthPrediction(trials);
        double  max = Arrays.stream(statsHand).max().getAsDouble();
        PokerHand[] handTypes = PokerHand.values();
        g2d.setColor(Color.WHITE);

        g2d.setFont(new Font("TimesRoman", Font.BOLD, 18));
        float winRate = 0;
        if(Game.ACTIVE_PLAYER != 0)
            winRate = player.winRatePrediction(Game.ACTIVE_PLAYER, 10000);
        g2d.drawString(String.format("Win Rate : %.2f", winRate), 40, 40);

        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 14));

        for(int i = 0; i < statsHand.length; i++)
        {
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.format("%.0f%%", statsHand[i]), 50, 80 + PADDING*i);
            g2d.drawString(String.format("%s", handTypes[i].toString()), 100 + BAR_LENGTH, 80 + PADDING*i);
            g2d.setColor(new Color(180, 180, 180));
            g2d.fillRoundRect(90, 75 - BAR_HEIGHT / 2 + PADDING * i, BAR_LENGTH, BAR_HEIGHT, ARC_RADIUS, ARC_RADIUS);
            g2d.setColor(Color.getHSBColor((float)(statsHand[i]/max*0.4), 0.7f, 1f));
            g2d.fillRoundRect(90 + BAR_LENGTH - (int)(statsHand[i]/max*BAR_LENGTH),
                    75 - BAR_HEIGHT / 2 + PADDING * i,
                    (int)(statsHand[i]/max*BAR_LENGTH), // Width
                    BAR_HEIGHT,
                    ARC_RADIUS,
                    ARC_RADIUS);
        }
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(20, 50, 350, PADDING * 11 + PADDING, ARC_RADIUS * 3, ARC_RADIUS * 3);


    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(410, 400);
        Hand commHand1 = new Hand(new int[]{});
        Player p1 = new Player(commHand1);
        p1.takesMoney(2000);
        p1.pickCards(new Card(9) , new Card(22));

        PlayerInfoPanel infoPanel = new PlayerInfoPanel(p1);
        frame.add(infoPanel);
        frame.setVisible(true);
    }
}
