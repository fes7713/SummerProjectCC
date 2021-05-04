package Poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Game implements ActionListener {
    static final int HAND_SIZE = 2;
    static final int COMMUNITY_CARDS_SIZE = 5;
    static final int nPlayers = 3;
    static final String[] stages = {"Pre-flop", "Flop", "Turn", "River"};

    private int stageCount, foldAllinCount;
    private final int INITIAL_MONEY = 100000;
    private List<Player> players;
    private final Deck deck;
    private final Hand communityCards;
    private final Money pot, smallBlind, callTotal;
    private final PokerTable pokerTable;
    private Controller controller;
    private GameInfoPanel infoPanel;
    private String[] names;
    private int currentPlayerIndex, initialPlayerIndex;
    private Integer[] win;
    private boolean wait;
    private boolean auto = false;
    private boolean allIn = false;

    public Game()
    {
        deck = new Deck();
        communityCards = new Hand(PokerTable.PADDING, PokerTable.PADDING * 2);

        initialPlayerIndex = 0;
        currentPlayerIndex = initialPlayerIndex;

        players = new ArrayList<>();
//        players.add(new Player(communityCards, INITIAL_MONEY, PokerTable.PADDING, PokerTable.STRING_LINE_SHIFT * 2 +
//                Card.CARD_HEIGHT + PokerTable.PADDING, true));
        for(int i = 0; i < nPlayers; i++)
            players.add(new Player(communityCards, INITIAL_MONEY,
                    Player.WIDTH * i + PokerTable.PADDING,
                    Card.CARD_HEIGHT + PokerTable.PADDING * 4, true));

        pot = new Money();
        callTotal = new Money();
        smallBlind = new Money(300);
        pokerTable = new PokerTable(this);
        stageCount = 0;
        foldAllinCount = 0;
        win = null;
        wait = false;
        gameInit();
        renamePLayers();
    }

    public void gameInit()
    {
        JFrame frame = new JFrame("Simple game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(780, 800);
        frame.setLayout(new BorderLayout());

        controller = new Controller(this);
        infoPanel = new GameInfoPanel(this);

        frame.add(pokerTable, BorderLayout.WEST);
        frame.add(infoPanel, BorderLayout.EAST);
        frame.add(controller, BorderLayout.SOUTH);

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
        g.drawString(stages[stageCount], 20, PokerTable.PADDING);
        communityCards.paint(g);
        g.drawRoundRect(communityCards.getX() - PokerTable.PADDING / 2,
                communityCards.getY() - PokerTable.PADDING / 2,
                COMMUNITY_CARDS_SIZE * Card.CARD_WIDTH + PokerTable.PADDING,
                Card.CARD_HEIGHT + PokerTable.PADDING,
                30,
                30);


        // Player Coloring
        if(win == null)
        {
            g.setColor(PokerTable.PRIMARY_COLOR_VARIANT);
            g.fillRoundRect(
                    Player.WIDTH * currentPlayerIndex + PokerTable.PADDING - PokerTable.PADDING / 2,
                    Card.CARD_HEIGHT + PokerTable.PADDING * 4 - PokerTable.PADDING / 2, Player.WIDTH,
                    Player.HEIGHT + PokerTable.PADDING, 30, 30);
        }
        else
        {
            g.setColor(PokerTable.SECONDARY_COLOR);
            for(int i = 0; i < win.length; i++)
            g.fillRoundRect(
                    Player.WIDTH * win[i] + PokerTable.PADDING - PokerTable.PADDING / 2,
                    Card.CARD_HEIGHT + PokerTable.PADDING * 4 - PokerTable.PADDING / 2, Player.WIDTH,
                    Player.HEIGHT + PokerTable.PADDING, 30, 30);
        }

        g.setColor(Color.white);
        for(Player player :players)
            player.paint(g);
    }

    public void betting() {
        int callCount = foldAllinCount;
        int checkCount = 0;
        currentPlayerIndex = initialPlayerIndex;

        Player player;
        while(callCount < nPlayers && checkCount < nPlayers)
        {
            player = players.get(currentPlayerIndex);
            // User input will reject fold and all in inside.
            userInput(player);
            Action action = player.getStatus();
            System.out.println(action);
            if(action == Action.FOLD || action == Action.ALL_IN)
            {
                foldAllinCount++;
                callCount++;
            }
            else if(action == Action.CALL || action == Action.BET)
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

        if(player.getStatus() == Action.FOLD || player.getStatus() == Action.ALL_IN)
            return;

        // Initialization
        if(callTotal.getAmount() == 0)
            controller.initBetButton();
        else
            controller.initCallButton();
        controller.initController();

        if(auto)
            controller.doCall();

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

    public void AllinBattle()
    {
        Money sidePot = new Money();
        List<Player> sortedPlayers = new ArrayList<>(players);
        // Remove folds
        for(int i = 0; i < sortedPlayers.size(); i++)
            if(sortedPlayers.get(i).getStatus() == Action.FOLD)
                sortedPlayers.remove(i);

        Collections.sort(sortedPlayers, Comparator.comparingInt(Player::getMoney));

        List<List<Player>> sidePotWinners = new ArrayList<>();
        List<Integer> sidePotsAmount = new ArrayList<>();

        while(sortedPlayers.size() > 1)
        {
            int amount = sortedPlayers.get(0).getMoney();

            // Add money to side pot
            // Take money from all players in sorted list
            for(Player player : sortedPlayers)
            {
                sidePot.add(player.givesMoney(amount));
            }


            List<Player> tiedPlayers = new ArrayList<>();
            // Find strongest one and put him in list.
            Player winner =
                    Collections.max(sortedPlayers, new java.util.Comparator<Player>() {
                        @Override
                        public int compare(Player o1, Player o2) {
                            return o1.compareTo(o2);
                        }
                    });

            sidePotsAmount.add(sidePot.clear());

            // Firstly, add first guy in the sorted list because he has 0 money for sure
            tiedPlayers.add(winner);

            // Next, find tied players.
            for(Player player : sortedPlayers)
                if(winner.compareTo(player) == 0 && !winner.equals(player))
                    tiedPlayers.add(player);

            // Lastly remove players who dont have money
            sortedPlayers.remove(0);
            while(sortedPlayers.size() > 0 && sortedPlayers.get(0).getMoney() == 0)
                sortedPlayers.remove(0);

            sidePotWinners.add(tiedPlayers);
        }

        // Pay money
        for(int i = 0; i < sidePotWinners.size(); i++)
        {
            for(Player player: sidePotWinners.get(i))
                player.takesMoney(sidePotsAmount.get(i)/sidePotWinners.get(i).size());
        }


    }

    public void gameEnd(){
        System.out.println("End");

        // ** Check who is winner

        Player wonPlayer = null;
        ArrayList<Integer> winPlayerList = new ArrayList<>(nPlayers);
        for(int i = 0, compNum; i < nPlayers; i++)
        {
            // get compare number and keep it
            if(players.get(i).getStatus() == Action.FOLD)
                continue;

            if(wonPlayer == null)
            {
                winPlayerList.add(i);
                wonPlayer = players.get(i);
            }

            compNum = wonPlayer.compareTo(players.get(i));

            // Now evaluate compNum and replace winPlayer if necessary
            if(compNum < 0)
            {
                winPlayerList.clear();
                winPlayerList.add(i);
                wonPlayer = players.get(i);
            }
            else if(compNum == 0)
                winPlayerList.add(i);
        }

        win = winPlayerList.toArray(new Integer[winPlayerList.size()]);

        wait = true;
//        if(auto)
//            controller.doCall();
        while(wait)
            repaint();

        // Pay pot to winner
        for(Integer integer :win)
            players.get(integer).takesMoney(pot.getAmount()/win.length);
        pot.clear();
        win = null;
    }

    public void gameReset()
    {
        // After game end.
        // Take cards from players and put them in deck.
        for(Player player : players)
            deck.addAll(player.reset());
        deck.addAll(communityCards.reset());
        System.out.println(deck.size());

//        smallBlind,
        stageCount = 0;
        foldAllinCount = 0;
        callTotal.clear();
        initialPlayerIndex = (initialPlayerIndex + 1) % nPlayers;
        currentPlayerIndex = initialPlayerIndex;


    }



    public void run() {
        while(true)
        {
            preFlop();
            flop();
            turn();
            river();
            gameEnd();
            gameReset();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(wait)
        {
            wait = false;
            return;
        }
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
                    pay = betInputted;
            }
            case "Call" -> {
                // bet amount is within your money, then bet
                if (callTotal.getAmount() - getCurrentPlayerBetTotal() < getCurrentPlayerMoney())
                    pay = callTotal.getAmount() - getCurrentPlayerBetTotal();
                    // if not all in
                else
                {
                    allIn = true;
                    pay = getCurrentPlayerMoney();
                }
            }
            case "Raise" -> pay = controller.getBetMoney() - getCurrentPlayerBetTotal();

            case "ALL-In" -> {
                allIn = true;
                pay = getCurrentPlayerMoney();
            }
        }
        System.out.println(command);
        System.out.println(pay);

        players.get(currentPlayerIndex).setCallValue(getCallTotal());
        callTotal.setAmount(players.get(currentPlayerIndex).proceedActionCommand(pay));

        // if not fol
        if(pay > 0)
            pot.add(pay);

        repaint();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();


    }


}

