package Poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Game implements ActionListener {
    static final int HAND_SIZE = 2;
    static final int COMMUNITY_CARDS_SIZE = 5;
    static final int nPlayers = 3;
    static final String[] stages = {"`Pre-flop", "Flop", "Turn", "River"};


    private int stageCount;
    private final int INITIAL_MONEY = 100000;
    private Player mainPlayer;
    private List<Player> bots;
    private Deck deck;
    private Hand communityCards;
    private Money pot, smallBlind, callTotal;
    private PokerTable pokerTable;
    private Queue<Action> actions;
    private Controller controller;

    public Game()
    {
        bots = new ArrayList<>();
        deck = new Deck();
        communityCards = new Hand(PokerTable.PADDING, PokerTable.STRING_LINE_SHIFT + PokerTable.PADDING);
        for(int i = 1; i < nPlayers; i++)
            bots.add(new Player(communityCards, INITIAL_MONEY,
                    Player.PLAYER_WIDTH * i + PokerTable.PADDING, PokerTable.STRING_LINE_SHIFT * 2 +
                    Card.CARD_HEIGHT + PokerTable.PADDING, false));
//            players.add(new Player(communityCards, INITIAL_MONEY, 0, Display.STRING_LINE_SHIFT * 2 +
//                                                Card.CARD_HEIGHT + Player.PLAYER_HEIGHT * i , true));

        pot = new Money();
        callTotal = new Money();
        smallBlind = new Money(300);
        mainPlayer = new Player(communityCards, INITIAL_MONEY, PokerTable.PADDING, PokerTable.STRING_LINE_SHIFT * 2 +
                Card.CARD_HEIGHT + PokerTable.PADDING, true);
        pokerTable = new PokerTable(this);
        stageCount = 0;
        gameInit();
    }

    public void gameInit()
    {
        JFrame frame = new JFrame("Simple game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 1000);
        frame.setLayout(new FlowLayout());

        controller = new Controller(this);



        frame.add(pokerTable);
        frame.add(controller);
        frame.setVisible(true);
    }

    public int getSmallBlind()
    {
        return smallBlind.getAmount();
    }

    public Hand getCommunityCards()
    {
        return communityCards;
    }

    public int getCurrentPlayerMoney()
    {
        return mainPlayer.getMoney();
    }

    public int getCallTotal()
    {
        return callTotal.getAmount();
    }

    // Change it later with getBetTotal(int playerIndex)
    public int getBetTotalPlayer()
    {
        return mainPlayer.getBetTotal();
    }


    public void next()
    {
        stageCount = (stageCount + 1) % 4;
    }

    public void repaint()
    {
        pokerTable.repaint();
    }

    public void paint(Graphics2D g)
    {
        g.drawString(stages[stageCount], 0, PokerTable.STRING_LINE_SHIFT);
        communityCards.paint(g);
        g.drawRoundRect(communityCards.getX() - PokerTable.PADDING / 2,
                communityCards.getY() - PokerTable.PADDING / 2,
                COMMUNITY_CARDS_SIZE * Card.CARD_WIDTH + PokerTable.PADDING,
                Card.CARD_HEIGHT + PokerTable.PADDING,
                30,
                30);

        mainPlayer.paint(g);
        for(Player player :bots)
            player.paint(g);

    }

    public int countFolds()
    {
        int count = 0;
        if(mainPlayer.getStatus() == Action.FOLD)
            count++;

        for(Player player :bots)
        {
            if(player.getStatus() == Action.FOLD)
            {
                count++;
            }
        }
        return count;
    }

    public void betting() {
        int callCount = 0;
        userInput();
        for(Player bot : bots)
        {


            repaint();
        }

    }

    private void userInput() {
        if(mainPlayer.getStatus() == Action.FOLD)
            return;
        while(true)
        {
            if(mainPlayer.isWait()) {
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
                mainPlayer.turnOnWait();
                return;
            }
        }
    }

    public void stageInit()
    {
        controller.setMinimum(smallBlind.getAmount());
        controller.initBetButton();
        callTotal.setAmount(0);
        mainPlayer.clearBet();
        mainPlayer.clearStatus();
        for(Player bot :bots)
        {
            bot.clearBet();
            bot.clearStatus();
            repaint();
        }
    }

    public void preFlop() {
        deck.shuffle();

        // Drawing cards
        mainPlayer.pickCards(deck.pop(), deck.pop());
        for(Player bot: bots)
        {
            bot.pickCards(deck.pop(), deck.pop());
        }

        stageInit();
        // SB BB take (WIP)
        callTotal.add(smallBlind.getAmount() * 2);
        // Set Call button not bet because callTotal is not 0
        controller.initCallButton();

        betting();
    }

    public void flop() {
        stageInit();
        communityCards.addCards(deck.pop(), deck.pop(), deck.pop());
        betting();

    }

    public void turn() {
        stageInit();
        communityCards.addCards(deck.pop());
        betting();
    }

    public void river() {
        stageInit();
        communityCards.addCards(deck.pop());
        betting();
    }

    public void run() {
        preFlop();
        flop();
        turn();
        river();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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
                if (callTotal.getAmount() - getBetTotalPlayer() < getCurrentPlayerMoney())
                    pay = callTotal.getAmount() - getBetTotalPlayer();
                    // if not all in
                else
                    pay = getCurrentPlayerMoney();
            }
            case "Raise" -> pay = controller.getBetMoney();

            case "ALL-In" -> pay = getCurrentPlayerMoney();
        }
        System.out.println(command);
        System.out.println(pay);

        mainPlayer.setCallValue(pay);
        callTotal.setAmount(mainPlayer.proceedActionCommand(pay));

        pot.add(pay);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
//        while(true)
//        {
//            game.repaint();
//            Thread.sleep(10);
//        }
    }


}

