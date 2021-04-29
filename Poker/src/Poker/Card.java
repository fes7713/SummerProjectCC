package Poker;

public class Card {
    private final int id;
    private final int number;
    private final int suit;
    private final int MAX_NUMBER = 13;

    public Card(int id)
    {
        this.id = id;
        number = id % MAX_NUMBER;
        suit = id / MAX_NUMBER;
    }

    public Card(int number, int suit)
    {
        this.number = number;
        this.suit = suit;
        id = suit * MAX_NUMBER + number;
    }
}
