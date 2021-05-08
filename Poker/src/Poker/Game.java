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
    static final int nPlayers = 5;
    static final String[] stages = {"Pre-flop", "Flop", "Turn", "River"};
    static final int mainPlayerIndex = 0;
    static final int AI_IQ = 1000;
    static final int SEED = 20000;

    private int stageCount;
    private final int INITIAL_MONEY = 10000;
    private List<Player> players;
    private final Deck deck;
    private final Hand communityCards;
    private final Money pot, smallBlind, callTotal;
    private final PokerTable pokerTable;
    private Controller controller;
    private GameInfoPanel gameInfoPanel;
    private PlayerInfoPanel playerInfoPanel;
    private Recorder recorder;
    private String[] names;
    private int currentPlayerIndex, initialPlayerIndex;
    private Integer[] win;
    private boolean wait;
    private final boolean auto = false;
    private boolean end = false;

    public Game()
    {
        deck = new Deck();
        communityCards = new Hand(PokerTable.PADDING, PokerTable.PADDING * 2);

        initialPlayerIndex = 0;
        currentPlayerIndex = initialPlayerIndex;

        players = new ArrayList<>();
        for(int i = 0; i < nPlayers; i++)
            players.add(new Player(communityCards, INITIAL_MONEY,
                    Player.WIDTH * i + PokerTable.PADDING,
                    Card.CARD_HEIGHT + PokerTable.PADDING * 4, false));

        players.remove(0);
        players.add(0, new Player(communityCards, INITIAL_MONEY,
                PokerTable.PADDING,
                Card.CARD_HEIGHT + PokerTable.PADDING * 4, true));
        players.get(0).setStrategy(Strategy.CALL_MAN);
        players.get(1).setStrategy(Strategy.SIMPLE_RANGE_EXP);
        players.get(2).setStrategy(Strategy.SIMPLE_RANGE_EXP);
        players.get(3).setStrategy(Strategy.EXPECTATION);
        players.get(4).setStrategy(Strategy.EXPECTATION);

        pot = new Money();
        callTotal = new Money();
        smallBlind = new Money(300);
        pokerTable = new PokerTable(this);

        recorder = new Recorder("PokerTest07.csv");
        recorder.setActive(true);
        // First record
        for(Player player:players)
            recorder.write(player.getStrategy() + ",");
        recorder.write("\n");
        for(Player player:players)
            recorder.write(player.getMoney() + ",");
        recorder.write("\n");

        stageCount = 0;
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
        gameInfoPanel = new GameInfoPanel(this);
        playerInfoPanel = new PlayerInfoPanel(players.get(mainPlayerIndex));

        frame.add(pokerTable, BorderLayout.CENTER);
        frame.add(gameInfoPanel, BorderLayout.EAST);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(controller, BorderLayout.CENTER);
        panel.add(playerInfoPanel, BorderLayout.WEST);

        frame.add(panel, BorderLayout.SOUTH);

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
        if(auto)
            return;
        pokerTable.repaint();
        gameInfoPanel.repaint();
    }

    public void paint(Graphics2D g)
    {
        g.drawString(stages[stageCount], 20, PokerTable.PADDING);
        try{
            communityCards.paint(g);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

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
            if(!end)
                g.setColor(PokerTable.SECONDARY_COLOR);
            else
                g.setColor(PokerTable.SECONDARY_COLOR_VARIANT);
            for (Integer integer : win)
                g.fillRoundRect(
                        Player.WIDTH * integer + PokerTable.PADDING - PokerTable.PADDING / 2,
                        Card.CARD_HEIGHT + PokerTable.PADDING * 4 - PokerTable.PADDING / 2, Player.WIDTH,
                        Player.HEIGHT + PokerTable.PADDING, 30, 30);
        }

        g.setColor(Color.white);
        for(Player player :players)
            player.paint(g);
    }

    public void betting() {
        int callCount = 0;
        int checkCount = 0;
        currentPlayerIndex = initialPlayerIndex;

        Player player;

        // Small bet action
        if(players.get(currentPlayerIndex).getStatus() == Action.SB)
        {
            players.get(currentPlayerIndex).AiSB(smallBlind.getAmount(), callTotal, pot);
            callCount++;
            players.get(currentPlayerIndex).turnOnWait();
            currentPlayerIndex = (currentPlayerIndex + 1) % nPlayers;
        }

        while(callCount < nPlayers && checkCount < nPlayers)
        {

            player = players.get(currentPlayerIndex);
            Action action = player.getStatus();

            if(action == Action.BB && callTotal.getAmount() == smallBlind.getAmount())
            {
                // Maybe we dont need this line
                controller.initCallButton();
                player.AiBB(smallBlind.getAmount(), callTotal, pot);
                callCount = 1;
                player.turnOnWait();
                currentPlayerIndex = (currentPlayerIndex + 1) % nPlayers;
                continue;
            }
            if(player.isControl())
                userInput(player);
            else
            {
                if(action == Action.ALL_IN || action == Action.FOLD)
                {
                    // Do nothing
                }
                else
                {
                    int activePlayers = nPlayers;
                    for(Player player1 : players)
                        if(player1.getStatus() == Action.FOLD || player1.getStatus() == Action.ALL_IN)
                            activePlayers--;
                    player.Ai_Strategy(smallBlind.getAmount(), activePlayers, callTotal, pot);
                }

            }


            action = player.getStatus();
            // User input will reject fold and all in inside.

//            System.out.println(action + " 1");
            if(action == Action.FOLD)
                callCount++;
            else if(action == Action.CALL || action == Action.BET)
                callCount++;
            else if(action == Action.ALL_IN)
            {
                if(player.getBetTotal() > callTotal.getAmount())
                    callCount = 1;
                else
                    callCount++;
            }
            else if(action == Action.CHECK)
                checkCount++;
                // if player's money was 0, then he must be in all in call
            else
                callCount = 1;

            if(!auto)
                repaint();

            // Update player
            currentPlayerIndex = (currentPlayerIndex + 1) % nPlayers;
        }
        repaint();
    }

    private void userInput(Player player) {
        Action action = player.getStatus();
        if(action == Action.FOLD || action == Action.ALL_IN)
            return;

        if(action == Action.CALL && player.getMoney() == 0)
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
        currentPlayerIndex = initialPlayerIndex;
        controller.initBetButton();
        callTotal.setAmount(0);
        Action action;
        for(Player player :players)
        {
            action = player.getStatus();
            if(action == Action.FOLD || (action == Action.CALL && player.getMoney() == 0))
                continue;
            player.clearBet();
            // If all in, clear bet but not status.
            if(action == Action.ALL_IN)
                continue;
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

        // SB BB, too complicated
        int index = initialPlayerIndex;
        // Exclude folds
        while(players.get(index).getStatus() == Action.FOLD)
        {
            index = (index + 1) % nPlayers;
        }
        players.get(index).setStatus(Action.SB);
        players.get(index).setWait(true);
        int SBIndex = index;
        // Search for player who has more money than BB
        index = (index+1) % nPlayers;
        Player player = players.get(index);
        while(player.getMoney() < smallBlind.getAmount() * 2)
        {
            player.setStatus(Action.FOLD);
            index = (index+1) % nPlayers;
            player = players.get(index);
            if(SBIndex == index)
                break;
        }
        player.setStatus(Action.BB);

        if(!auto)
            playerInfoPanel.repaint();
        betting();
    }

    public void flop() {
        stageCount = (stageCount + 1) % 4;
        stageInit();
        communityCards.addCards(deck.pop(), deck.pop(), deck.pop());
        if(!auto)
            playerInfoPanel.repaint();
        betting();
    }

    public void turn() {
        stageCount = (stageCount + 1) % 4;
        stageInit();
        communityCards.addCard(deck.pop());
        if(!auto)
            playerInfoPanel.repaint();
        betting();
    }

    public void river() {
        stageCount = (stageCount + 1) % 4;
        stageInit();
        communityCards.addCard(deck.pop());
        playerInfoPanel.repaint();
        betting();
    }

    public int[] PaymentStage()
    {
        Money mainPot = new Money();
        Money sidePot = new Money();
        List<Player> sortedPlayers = new ArrayList<>();

        // Remove folds and add money to main pot
        for (int i = 0; i < players.size(); i++)
        {
            if (players.get(i).getStatus() != Action.FOLD)
            {
                sortedPlayers.add(players.get(i));

            }
            else
                mainPot.add(players.get(i).getPayTotal());
        }

        // Sort players by money
        Collections.sort(sortedPlayers, Comparator.comparingInt(Player::getPayTotal));

        List<List<Player>> sidePotWinners = new ArrayList<>();
        List<Integer> sidePotsAmount = new ArrayList<>();

        while (sortedPlayers.size() > 0) {
            int amount = sortedPlayers.get(0).getPayTotal();

            // Add money to side pot
            // Take money from all players in sorted list
            for (Player player : sortedPlayers) {
                sidePot.add(player.givePayTotal(amount));
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
            for (Player player : sortedPlayers)
                if (winner.compareTo(player) == 0 && !winner.equals(player))
                    tiedPlayers.add(player);

            // Lastly remove players who dont have money
            sortedPlayers.remove(0);
            while (sortedPlayers.size() > 0 && sortedPlayers.get(0).getPayTotal() == 0)
                sortedPlayers.remove(0);

            sidePotWinners.add(tiedPlayers);
        }

        // Pay money
        for (Player player : sidePotWinners.get(0))
            player.takesMoney(mainPot.getAmount() / sidePotWinners.get(0).size());


        for (int i = 0; i < sidePotWinners.size(); i++) {
            for (Player player : sidePotWinners.get(i))
                player.takesMoney(sidePotsAmount.get(i) / sidePotWinners.get(i).size());
        }

        // Retrieve index of winners
        int[] win = new int[sidePotWinners.get(0).size()];
        for(int i = 0; i < sidePotWinners.get(0).size(); i++)
        {
            for(int j = 0; j < nPlayers; j++)
            {
                if(sidePotWinners.get(0).get(i).equals(players.get(j)))
                    win[i] = j;
            }
        }
        return win;
    }

    public boolean gameEnd(){
        // ** Check who is winner

        Player wonPlayer = null;
        ArrayList<Integer> winPlayerList = new ArrayList<>(nPlayers);
        for(int i = 0, compNum; i < nPlayers; i++)
        {
            // get compare number and keep it
            if(players.get(i).getStatus() == Action.FOLD)
                continue;

            players.get(i).evalHandAccuracy();
            if(wonPlayer == null)
            {
                winPlayerList.add(i);
                wonPlayer = players.get(i);
                continue;
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

        // show cards
        for(Player player: players)
        {
            player.showdown();
//            System.out.println(player);
        }

        while(wait && !auto)
            repaint();

// Pay pot to winner
        PaymentStage();


        // Record
        for(Player player:players)
            recorder.write(player.getMoney() + ",");
        recorder.write("\n");

        pot.clear();

        // Check if whole game ends by having many folds
        int foldCount = 0;
        for(Player player : players)
            if(player.getMoney() <= smallBlind.getAmount())
                foldCount++;
        if(foldCount >= nPlayers - 1)
        {
            end = true;
            repaint();
            return false;
        }
        return true;
    }

    public void gameReset()
    {
        // After game end.
        // Take cards from players and put them in deck.
        for(Player player : players)
            deck.addAll(player.reset(smallBlind.getAmount()));
        deck.addAll(communityCards.reset());
        win = null;
        stageCount = 0;
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
            if(!gameEnd())
            {
                recorder.fileClose();
                break;
            }

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
        // ** Need SB and BB fix for AIs
        if(!players.get(currentPlayerIndex).isControl())
            if(players.get(currentPlayerIndex).getStatus() != Action.BB && players.get(currentPlayerIndex).getStatus() != Action.SB)
                return;
        String command = e.getActionCommand();
        switch(command)
        {
            case "Fold" -> players.get(currentPlayerIndex).AiFold();

            case "Check" -> players.get(currentPlayerIndex).AiCheck(callTotal);

            case "Bet" -> {
                if(controller.getBetMoney() <= smallBlind.getAmount())
                    players.get(currentPlayerIndex).AiCall(smallBlind.getAmount(),callTotal , pot);
                else
                    players.get(currentPlayerIndex).AiRaise(controller.getBetMoney(), smallBlind.getAmount(), callTotal, pot);
            }
            case "Call" -> players.get(currentPlayerIndex).AiCall(smallBlind.getAmount(),callTotal , pot);

            case "Raise" -> players.get(currentPlayerIndex).AiRaise(controller.getBetMoney(), smallBlind.getAmount(), callTotal, pot);

            case "All-In" -> players.get(currentPlayerIndex).AiAllIn(callTotal, pot);

        }
        repaint();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.run();
        while(true)
            game.repaint();
    }
}

