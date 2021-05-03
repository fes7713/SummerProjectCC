package Poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Game implements ActionListener {
    static final int HAND_SIZE = 2;
    static final int COMMUNITY_CARDS_SIZE = 5;
    static final int nPlayers = 3;
    static final String[] stages = {"Pre-flop", "Flop", "Turn", "River"};

    private int stageCount;
    private final int INITIAL_MONEY = 100000;
    private List<Player> players;
    private Deck deck;
    private Hand communityCards;
    private Money pot, smallBlind, callTotal;
    private PokerTable pokerTable;
    private Controller controller;
    private GameInfoPanel infoPanel;
    private String[] names;
    private int mainPlayerIndex;
    private int currentPlayerIndex;
    private int initialPlayerIndex;

    public Game()
    {
        deck = new Deck();
        communityCards = new Hand(PokerTable.PADDING, PokerTable.STRING_LINE_SHIFT + PokerTable.PADDING);

        mainPlayerIndex = 0;
        initialPlayerIndex = 0;
        currentPlayerIndex = initialPlayerIndex;

        players = new ArrayList<>();
//        players.add(new Player(communityCards, INITIAL_MONEY, PokerTable.PADDING, PokerTable.STRING_LINE_SHIFT * 2 +
//                Card.CARD_HEIGHT + PokerTable.PADDING, true));
        for(int i = 0; i < nPlayers; i++)
            players.add(new Player(communityCards, INITIAL_MONEY,
                    Player.PLAYER_WIDTH * i + PokerTable.PADDING, PokerTable.STRING_LINE_SHIFT * 2 +
                    Card.CARD_HEIGHT + PokerTable.PADDING, true));

        pot = new Money();
        callTotal = new Money();
        smallBlind = new Money(300);
        pokerTable = new PokerTable(this);
        stageCount = 0;

        gameInit();
        renamePLayers();
    }

    public void gameInit()
    {
        JFrame frame = new JFrame("Simple game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 1000);
        frame.setLayout(new FlowLayout());

        controller = new Controller(this);
        infoPanel = new GameInfoPanel(this);


        frame.add(pokerTable);
        frame.add(infoPanel);
        frame.add(controller);

        frame.setVisible(true);
    }

    public void renamePLayers()
    {
        names = new String[nPlayers];
        int playerCount = 0, botCount = 0;
        for(Player player:players)
        {
            if(player.isControl())
                player.rename("Player" + playerCount++);
            else
                player.rename("Bot" + botCount++);
        }

        for(int i = 0; i < nPlayers; i++)
            names[i] = players.get(i).name();
    }

    public String[] playerNames()
    {
        return names;
    }

    public Action[] playerStatuses()
    {
        Action[] actions = new Action[nPlayers];

        for(int i = 0; i < nPlayers; i++)
            actions[i] = players.get(i).getStatus();
        return actions;
    }

    public int getSmallBlind()
    {
        return smallBlind.getAmount();
    }

    public Hand getCommunityCards()
    {
        return communityCards;
    }

    public int getMainPlayerIndex()
    {
        return mainPlayerIndex;
    }

    public int getPlayerMoney(int index)
    {
        return players.get(index).getMoney();
    }

    public int getCurrentPlayerMoney()
    {
        return players.get(currentPlayerIndex).getMoney();
    }

    public int getCallTotal()
    {
        return callTotal.getAmount();
    }

    public int getPotAmount()
    {
        return pot.getAmount();
    }

    public String getStage()
    {
        return stages[stageCount];
    }
    // Change it later with getBetTotal(int playerIndex)
    public int getPlayerBetTotal(int index)
    {
        return players.get(index).getBetTotal();
    }

    public int getCurrentPlayerBetTotal()
    {
        return players.get(currentPlayerIndex).getBetTotal();
    }

    public void repaint()
    {
        pokerTable.repaint();
        infoPanel.repaint();
    }

    public void paint(Graphics2D g)
    {
        g.drawString(stages[stageCount], 20, PokerTable.STRING_LINE_SHIFT);
        communityCards.paint(g);
        g.drawRoundRect(communityCards.getX() - PokerTable.PADDING / 2,
                communityCards.getY() - PokerTable.PADDING / 2,
                COMMUNITY_CARDS_SIZE * Card.CARD_WIDTH + PokerTable.PADDING,
                Card.CARD_HEIGHT + PokerTable.PADDING,
                30,
                30);

        for(Player player :players)
            player.paint(g);

    }

    public int countFolds()
    {
        int count = 0;

        for(Player player :players)
        {
            if(player.getStatus() == Action.FOLD)
            {
                count++;
            }
        }
        return count;
    }

    public void betting() {
        int callCount = countFolds();
        int checkCount = 0;
        currentPlayerIndex = initialPlayerIndex;

        Player player;
        while(callCount < nPlayers && checkCount < nPlayers)
        {
            player = players.get(currentPlayerIndex);
            userInput(player);
            Action action = player.getStatus();
            System.out.println("Yo");
            System.out.println(action);
            if(action == Action.CALL || action == Action.FOLD || action == Action.BET)
                callCount++;
            else if(action == Action.CHECK)
                checkCount++;
            else
                callCount = 1;
            repaint();
            currentPlayerIndex = (currentPlayerIndex + 1) % nPlayers;
        }
        repaint();

    }

    private void userInput(Player player) {
        // Auto AI Calc later
        if(!player.isControl())
            return;
        if(player.getStatus() == Action.FOLD)
            return;

        // Initialization
        if(callTotal.getAmount() == 0)
            controller.initBetButton();
        else
            controller.initCallButton();
        controller.initController();

        while(true)
        {
            if(player.isWait()) {
                try
                {
                    repaint();
                    Thread.sleep(10);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                player.turnOnWait();
                return;
            }
        }
    }

    public void stageInit()
    {
        currentPlayerIndex = 0;
        controller.initBetButton();
        callTotal.setAmount(0);
        Action action;
        for(Player player :players)
        {
            action = player.getStatus();
            if(action == Action.FOLD || action == Action.ALL_IN)
                continue;
            player.clearBet();
            player.clearStatus();
            repaint();
        }
    }

    public void preFlop() {
        deck.shuffle();

        // Drawing cards
        for(int i = 0; i < HAND_SIZE; i++)
        {
            for(Player player: players)
            {
                player.pickCards(deck.pop());
            }
        }


        stageInit();
        // SB BB take (WIP)
        callTotal.add(smallBlind.getAmount() * 2);
        // Set Call button not bet because callTotal is not 0
        controller.initCallButton();

        betting();
    }

    public void flop() {
        stageCount = (stageCount + 1) % 4;
        stageInit();
        communityCards.addCards(deck.pop(), deck.pop(), deck.pop());
        betting();

    }

    public void turn() {
        stageCount = (stageCount + 1) % 4;
        stageInit();
        communityCards.addCards(deck.pop());
        betting();
    }

    public void river() {
        stageCount = (stageCount + 1) % 4;
        stageInit();
        communityCards.addCards(deck.pop());
        betting();
    }

    public void gameEnd()
    {

    }

    public void gameReset()
    {

    }

    public void run() {
        preFlop();
        flop();
        turn();
        river();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!players.get(currentPlayerIndex).isControl())
            return;
        String command = e.getActionCommand();
        int pay = -2;
        switch(command)
        {
            case "Fold" -> pay = -1;

            case "Check" -> pay = getCallTotal() == 0 ? 0:-2;

            case "Bet" -> {
                int betInputted = controller.getBetMoney();
                if(betInputted < smallBlind.getAmount())
                    pay = -2;
                else
                    pay = controller.getBetMoney();
            }
            case "Call" -> {
                // bet amount is within your money, then bet
                if (callTotal.getAmount() - getCurrentPlayerBetTotal() < getCurrentPlayerMoney())
                    pay = callTotal.getAmount() - getCurrentPlayerBetTotal();
                    // if not all in
                else
                    pay = getCurrentPlayerMoney();
            }
            case "Raise" -> pay = controller.getBetMoney() - getCurrentPlayerBetTotal();

            case "ALL-In" -> pay = getCurrentPlayerMoney();
        }
        System.out.println(command);
        System.out.println(pay);

        players.get(currentPlayerIndex).setCallValue(getCallTotal());
        callTotal.setAmount(players.get(currentPlayerIndex).proceedActionCommand(pay));

        // if not fold.
        if(pay > 0)
            pot.add(pay);

        repaint();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
//        while(true)
//            game.repaint();
//        while(true)
//        {
//            game.repaint();
//            Thread.sleep(10);
//        }
    }


}

