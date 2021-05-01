package Poker;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    static final int HAND_SIZE = 2;
    static final int COMMUNITY_CARDS_SIZE = 5;
    static final int nPlayers = 1;
    private final int INITIAL_MONEY = 100000;
    private List<Player> players;
    private Deck deck;
    private Hand communityCards;
    private Money pot;
    private Money BigBlind;
    private Player startPlayer;
    private Display display;

    public Game()
    {
        players = new ArrayList<>();
        for(int i = 0; i < nPlayers; i++)
            players.add(new Player(INITIAL_MONEY, true));

        deck = new Deck();
        communityCards = new Hand();
        pot = new Money(0);
        BigBlind = new Money(0);
        startPlayer = players.get(0);
        display = new Display();

        gameInit();
    }

    public void gameInit()
    {
        JFrame frame = new JFrame("Simple game");

        frame.add(display);
        frame.setSize(800, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(frame.getContentPane().getWidth());
    }


    public Money getPot()
    {
        return pot;
    }

    public void betting()
    {
        int count = 0;
        while(true)
        {

        }
    }

    public void repaint()
    {
        display.repaint();
    }

    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();

        while(true)
        {
            game.repaint();
            Thread.sleep(10);
        }
    }
}
