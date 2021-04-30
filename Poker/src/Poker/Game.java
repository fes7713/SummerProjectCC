package Poker;

import java.util.ArrayList;
import java.util.List;

public class Game {
    static final int HAND_SIZE = 2;
    static final int COMMUNITY_CARDS_SIZE = 5;
    static final int nPlayers = 3;
    private final int INITIAL_MONEY = 100000;
    private List<Player> players;
    private Deck deck;
    private Hand communityCards;
    private Money pot;
    private Money BigBlind;
    private Player startPlayer;

    public Game()
    {
        players = new ArrayList<>();
        for(int i = 0; i < nPlayers; i++)
            players.add(new Player(INITIAL_MONEY));

        deck = new Deck();
        communityCards = new Hand();
        pot = new Money(0);
        BigBlind = new Money(0);
        startPlayer = players.get(0);
    }
    public Money getPot()
    {
        return pot;
    }

    public void betting()
    {

    }
}
