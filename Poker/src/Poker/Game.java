package Poker;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    static final int HAND_SIZE = 2;
    static final int COMMUNITY_CARDS_SIZE = 5;
    static final int nPlayers = 3;
    static final String[] stages = {"`Pre-flop", "Flop", "Turn", "River"};


    private int stageCount;
    private final int INITIAL_MONEY = 100000;
    private List<Player> players;
    private Deck deck;
    private Hand communityCards;
    private Money pot;
    private Money SmallBlind;
    private Player startPlayer;
    private Display display;

    public Game()
    {
        players = new ArrayList<>();
        deck = new Deck();
        communityCards = new Hand(0, Display.STRING_LINE_SHIFT);
        for(int i = 0; i < nPlayers; i++)
          players.add(new Player(communityCards, INITIAL_MONEY,
                  Player.PLAYER_WIDTH * i, Display.STRING_LINE_SHIFT * 2 +
                    Card.CARD_HEIGHT , true));
//            players.add(new Player(communityCards, INITIAL_MONEY, 0, Display.STRING_LINE_SHIFT * 2 +
//                                                Card.CARD_HEIGHT + Player.PLAYER_HEIGHT * i , true));

        pot = new Money(0);
        SmallBlind = new Money(300);
        startPlayer = players.get(0);
        display = new Display(this);
        stageCount = 0;
        gameInit();
    }

    public void gameInit()
    {
        JFrame frame = new JFrame("Simple game");

        frame.add(display);
        frame.setSize(800, 1000);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(frame.getContentPane().getWidth());
    }


    public Money getPot()
    {
        return pot;
    }

    public Hand getCommunityCards()
    {
        return communityCards;
    }
    public void betting()
    {
        int count = 0;
//        while(true)
//        {
//
//        }
    }

    public void next()
    {
        stageCount = (stageCount + 1) % 4;
    }

    public void repaint()
    {
        display.repaint();
    }

    public void paint(Graphics2D g)
    {
        g.drawString(stages[stageCount], 0, Display.STRING_LINE_SHIFT);
        communityCards.paint(g);
        for(Player player :players)
            player.paint(g);

    }

    public void preFlop() throws InterruptedException {
        deck.shuffle();
        for(Player player:players)
        {
            for(int i = 0; i < HAND_SIZE; i++)
            {
                player.pickCard(deck.pop());
            }
        }
        betting();

        while(true)
        {
            if(stageCount != 0)
                return;
            this.repaint();
            Thread.sleep(10);
        }
    }

    public void flop() throws InterruptedException {
        communityCards.addCards(deck.pop(), deck.pop(), deck.pop());
        betting();

        while(true)
        {
            System.out.println(stageCount);
            if(stageCount != 1)
                return;
            this.repaint();
            Thread.sleep(10);
        }
    }

    public void turn() throws InterruptedException {
        communityCards.addCards(deck.pop());
        betting();

        while(true)
        {
            if(stageCount != 2)
                return;
            this.repaint();
            Thread.sleep(10);
        }
    }

    public void river() throws InterruptedException {
        communityCards.addCards(deck.pop());

        while(true)
        {
            if(stageCount != 3)
                return;
            this.repaint();
            Thread.sleep(10);
        }
    }

    public void run() throws InterruptedException {
        preFlop();
        flop();
        turn();
        river();
    }


    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();
        game.run();
//        while(true)
//        {
//            game.repaint();
//            Thread.sleep(10);
//        }
    }
}
