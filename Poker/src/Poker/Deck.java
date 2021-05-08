package Poker;

import java.util.*;

public class Deck {
    static final int SIZE = 52;
    Stack<Card> deck;
    static Random rand = new Random(Game.SEED);
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

    public static void shuffle(int[] deck)
    {
        for(int i = deck.length - 1; i > 0; i--){
            int j = rand.nextInt(i + 1);
            if(i != j) {
                int t = deck[i];
                deck[i] = deck[j];
                deck[j] = t;
            }
        }
    }


    public static int[] fill(int[] excludes)
    {
        int [] deck = new int[Deck.SIZE];
        int count = 0;
        for(int i = 0; i < Deck.SIZE;i++)
        {
            if(!contains(excludes, i))
                deck[i - count] = i;
            else
                count++;
        }
        return Arrays.copyOf(deck, deck.length - count);
    }

    public static boolean contains(int[] arr, int num)
    {
        for(int i = 0; i < arr.length; i++)
            if(arr[i] == num)
                return true;
        return false;
    }
}
