package Poker;

import java.util.Collections;
import java.util.List;
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

    public int size()
    {
        return deck.size();
    }

    public void shuffle()
    {
        Collections.shuffle(deck);
    }

    public Card pop()
    {
        return deck.pop();
    }

    public void add(Card card)
    {
        deck.add(card);
    }

    public void addAll(List<Card> cards)
    {
        deck.addAll(cards);
    }

    public void addAll(List<Card>... cardsList)
    {
        for(List<Card> cards:cardsList)
        {
            deck.addAll(cards);
        }
    }
}
