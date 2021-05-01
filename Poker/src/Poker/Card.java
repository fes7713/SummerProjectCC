package Poker;

import javax.swing.*;
import java.awt.*;

public class Card implements Comparable<Card>{
    static final int SCALE_CONSTANT = 7;
    static final int CARD_WIDTH = 80;
    static final int CARD_HEIGHT = 120;
    private final int id;
    private final int number;
    private final int suit;
    private int x;
    private int y;

    static final int MAX_NUMBER = 13;
    static final int MAX_SUIT = 4;
    static final String[] SUITS = {"S", "H", "C", "D"};
    static final String[] NUMBERS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    private SortOrder sortOrder;
    private Image image;

    public Card(int id)
    {
        this.id = id;
        number = id % MAX_NUMBER;
        suit = id / MAX_NUMBER;
        sortOrder = SortOrder.NUMBER;
    }

    public Card(int number, int suit)
    {
        this.number = number;
        this.suit = suit;
        id = suit * MAX_NUMBER + number;
        sortOrder = SortOrder.NUMBER;
    }

    public Card(int id, int x, int y)
    {
        this(id);
        setCoordinate(x, y);
        image = Toolkit.getDefaultToolkit().getImage("assets/" + NUMBERS[number] + SUITS[suit] + ".png");
    }

    public Card(int number, int suit, int x, int y)
    {
        this(number, suit);
        setCoordinate(x, y);
        image = Toolkit.getDefaultToolkit().getImage("assets/" + NUMBERS[number] + SUITS[suit] + ".png");
    }

    public void setSortOrder(SortOrder order)
    {
        sortOrder = order;
    }

    public void setCoordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public int getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Card)
        {
            Card c = (Card)obj;
            if(id == c.getId())
                return true;
        }
        return false;
    }

    @Override
    public int compareTo(Card card) {
        switch (sortOrder) {
            case NUMBER -> {
                return number - card.getNumber();
            }
            case SUIT -> {
                return suit - card.getSuit();
            }
            case ID -> {
                return id - card.getId();
            }
        }
        return 0;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(SUITS[suit]).append(NUMBERS[number]);

        return sb.toString();
    }

    public void paint(Graphics2D g)
    {
        g.drawImage(image, x, y, Card.CARD_WIDTH, Card.CARD_HEIGHT, null);
    }
}
