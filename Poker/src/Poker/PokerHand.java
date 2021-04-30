package Poker;

public enum PokerHand {
    ROYAL_FLUSH(0),
    STRAIGHT_FLUSH(1),
    FOUR_OF_A_KIND(2),
    FULL_HOUSE(3),
    FLUSH(4),
    STRAIGHT(5),
    THREE_OF_A_KIND(6),
    TWO_PAIR(7),
    PAIR(8),
    HIGH_CARD(9);

    private int id;

    // コンストラクタの定義
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
