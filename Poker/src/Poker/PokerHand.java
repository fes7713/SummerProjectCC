package Poker;

public enum PokerHand {
    ROYAL_STRAIGHT_FLUSH(0),
    STRAIGHT_FLUSH(1),
    FOUR_OF_A_KIND(2),
    FULL_HOUSE(3),
    FLUSH(4),
    ACE_HIGH_STRAIGHT(5),
    STRAIGHT(6),
    THREE_OF_A_KIND(7),
    TWO_PAIR(8),
    PAIR(9),
    HIGH_CARD(10);

    private int id;

    private PokerHand(int id) {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public static void main(String[] args)
    {
        PokerHand pk = PokerHand.HIGH_CARD;
        System.out.println(pk.getId());
    }
}
