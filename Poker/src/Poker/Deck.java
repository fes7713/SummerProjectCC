package Poker;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    static final int SIZE = 52;
    Stack<Card> deck;

    public Deck()
    {
        deck = new Stack<>();
        fill();
    }

    public void fill()
    {
        deck.clear();
        for(int i = 0; i < SIZE; i++)
        {
            deck.push(new Card(i, 0, 0));
        }
    }

    public void shuffle()
    {
        Collections.shuffle(deck);
    }

    public Card pop()
    {
        return deck.pop();
    }
}
