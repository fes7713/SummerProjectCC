package Poker;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Hand implements Comparable<Hand>, Iterable<Card>{
    private List<Card> hand;
    private int size;
    private PokerHand strength;
    private List<Integer>kickers;
    private final int  EVAL_SIZE = 5;
    private SortOrder sortOrder;
    private int x;
    private int y;

    public Hand()
    {
        this.size = 0;
        hand = new ArrayList<>();
        kickers = new ArrayList<>();
        sortOrder = SortOrder.NUMBER;
        setSortOrder(sortOrder);
    }

    public Hand(List<Card> cards)
    {
        size = cards.size();
        hand = cards;
        kickers = new ArrayList<>();
        sortOrder = SortOrder.NUMBER;
        setSortOrder(sortOrder);
    }

    public Hand(int x, int y)
    {
        this();
        this.x = x;
        this.y = y;
    }

    public Hand(int[] cards)
    {
        this();
        size = cards.length;
        for(int i = 0; i < size; i++)
        {
            hand.add(new Card(cards[i]));
        }
    }

    public Hand(int[] cards, int x, int y)
    {
        this();
        size = 0;
        this.x = x; this.y = y;
        for(int i = 0; i < cards.length; i++)
        {
            hand.add(new Card(cards[i], x + Card.CARD_WIDTH * size, y));
            size++;
        }
    }

    public Hand(int x, int y, int size, String color)
    {
        this(x, y);
        for(int i = 0; i < size; i++)
        {
            addCards(new Card(color));
        }
    }

    public List<Card> reset()
    {
        List<Card> temp = hand;
        this.size = 0;
        hand = new ArrayList<>();
        kickers = new ArrayList<>();
        sortOrder = SortOrder.NUMBER;
        setSortOrder(sortOrder);
        return temp;
    }

    public PokerHand getStrength() {
        return strength;
    }

    public void setStrength(PokerHand strength) {
        this.strength = strength;
    }

    public Card get(int index)
    {
        if(index < 0 || index >= size)
        {
            throw new IndexOutOfBoundsException("Hand List Out of Bound");
        }

        return hand.get(index);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int size()
    {
        return size;
    }

    public List<Card> getCards()
    {
        return hand;
    }

    public List<Integer> getKickers()
    {
        return kickers;
    }

    public void addCard(Card card)
    {
        card.setCoordinate(x + Card.CARD_WIDTH * hand.size(), y);
        hand.add(card);
        size++;
    }

    public void addCards(Card... cards)
    {
        for(Card card: cards)
        {
            card.setCoordinate(x + Card.CARD_WIDTH * hand.size(), y);;
            hand.add(card);
            size++;
        }
    }


    public void sort()
    {
        Collections.sort(hand);
    }

    public void setSortOrder(SortOrder order)
    {
        int n = hand.size();
        for(int i = 0; i < n; i++)
        {
            hand.get(i).setSortOrder(order);
        }
    }

    public boolean contains(Card card)
    {
        for(int i = 0; i < hand.size(); i++)
        {
            if(hand.get(0).equals(card))
            {
                return true;
            }
        }
        return false;
    }

    public PokerHand evalHand()
    {
        kickers.clear();
        int straight = -1;
        int straight_flush = -1;
        int consecutive;


        setSortOrder(SortOrder.ID);
        sort();
        consecutive = 0;
        // Straight Flush
        for(int i = 1; i < hand.size(); i++)
        {
            if(hand.get(i).compareTo(hand.get(i - 1)) == 0)
                continue;
            if(hand.get(i).compareTo(hand.get(i - 1)) == 1 && hand.get(i).getSuit() == hand.get(i - 1).getSuit())
            {
                consecutive++;
                if(consecutive >= EVAL_SIZE -2 && hand.get(i).getNumber() == 12 && hand.contains(new Card(0, hand.get(i).getSuit())))
                {
                    return PokerHand.ROYAL_FLUSH;
                }
                if(consecutive >= EVAL_SIZE - 1)
                {
                    straight_flush = hand.get(i).getNumber();
                }
            }
            else
            {
                consecutive = 0;
            }
        }

        if(straight_flush != -1)
        {
            kickers.add(straight_flush);
            return PokerHand.STRAIGHT_FLUSH;
        }

        setSortOrder(SortOrder.NUMBER);
        sort();
        consecutive = 0;
        int[] numFreq = new int[Card.MAX_NUMBER];
        // Counting number frequency
        for(int i = 0; i < hand.size(); i++)
        {
            numFreq[hand.get(i).getNumber()]++;
        }

        // Counting frequency of number frequency
        int[] freqFreq = new int[Card.MAX_SUIT + 1];
        for(int i = 0; i < Card.MAX_NUMBER; i++)
        {
            try{
                freqFreq[numFreq[i]]++;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

// FOUR_OF_A_KIND
        if(freqFreq[4] >= 1)
            return PokerHand.FOUR_OF_A_KIND;
// FULL_HOUSE
        if((freqFreq[3] >= 1 && freqFreq[2] >= 1) || freqFreq[3] >= 2)
            return PokerHand.FULL_HOUSE;


// Flush
        int[] suitFreq = new int[Card.MAX_SUIT];
        for(int i = 0; i < hand.size(); i++)
        {
            suitFreq[hand.get(i).getSuit()]++;
        }

        int nMaxPosFlush = 0;
        for(int i = 0; i < Card.MAX_SUIT; i++)
        {
            if(suitFreq[i] > suitFreq[nMaxPosFlush])
            {
                nMaxPosFlush = i;
            }
        }
        if(suitFreq[nMaxPosFlush] >= EVAL_SIZE)
        {
            kickers.add(nMaxPosFlush);
            return PokerHand.FLUSH;
        }

// Straight
        for(int i = 1; i < hand.size(); i++)
        {
            if(hand.get(i).compareTo(hand.get(i - 1)) == 0)
                continue;
            if(hand.get(i).compareTo(hand.get(i - 1)) == 1)
            {
                consecutive++;
                if(consecutive >= EVAL_SIZE -2 && hand.get(i).getNumber() == 12 && hand.get(0).getNumber() == 0)
                {
                    return PokerHand.ACE_STRAIGHT;
                }
                if(consecutive >= EVAL_SIZE - 1)
                {
                    straight = hand.get(i).getNumber();
                }
            }
            else
            {
                consecutive = 0;
            }
        }

        if(straight != -1)
        {
            kickers.add(straight);
            return PokerHand.STRAIGHT;
        }

        // Three of a kind
        if(freqFreq[3] == 1)
            return PokerHand.THREE_OF_A_KIND;
        // Two pair
        if(freqFreq[2] > 1)
            return PokerHand.TWO_PAIR;
        // One pair
        if(freqFreq[2] == 1)
            return PokerHand.PAIR;
        // High Card
        return PokerHand.HIGH_CARD;

    }

    public PokerHand evalHandAccuracy()
    {
        kickers.clear();
        int straight = -1;
        int straight_flush = -1;
        int consecutive;

        setSortOrder(SortOrder.ID);
        sort();
        consecutive = 0;
// Straight Flush
        for(int i = 1; i < hand.size(); i++)
        {
            if(hand.get(i).compareTo(hand.get(i - 1)) == 0)
                continue;
            if(hand.get(i).compareTo(hand.get(i - 1)) == 1 && hand.get(i).getSuit() == hand.get(i - 1).getSuit())
            {
                consecutive++;
                if(consecutive >= EVAL_SIZE -2 && hand.get(i).getNumber() == 12 && hand.contains(new Card(0, hand.get(i).getSuit())))
                {
                    return PokerHand.ROYAL_FLUSH;
                }
                if(consecutive >= EVAL_SIZE - 1)
                {
                    straight_flush = hand.get(i).getNumber();
                }
            }
            else
            {
                consecutive = 0;
            }
        }

        if(straight_flush != -1)
        {
            kickers.add(straight_flush);
            return PokerHand.STRAIGHT_FLUSH;
        }

        setSortOrder(SortOrder.NUMBER);
        sort();
        consecutive = 0;

        int[] numFreq = new int[Card.MAX_NUMBER];

        // Counting number frequency
        for(int i = 0; i < hand.size(); i++)
        {
            numFreq[hand.get(i).getNumber()]++;
        }

        // Counting frequency of number frequency

        int[] freqFreq = new int[Card.MAX_SUIT + 1];

        for(int i = 0; i < Card.MAX_NUMBER; i++)
        {
            freqFreq[numFreq[i]]++;
        }

// FOUR_OF_A_KIND
        if(freqFreq[4] >= 1)
        {
            for(int i = Card.MAX_NUMBER - 1; i >= 0; i--)
            {
                if(numFreq[i] == 4)
                {
                    kickers.add(i);
                    break;
                }
            }

            // Ace High
            if(numFreq[0] >= 1)
            {
                kickers.add(0);
                return PokerHand.FOUR_OF_A_KIND;
            }

            for(int i = Card.MAX_NUMBER - 1; i >= 0; i--)
            {
                if(numFreq[i] != 4)
                {
                    kickers.add(i);
                    break;
                }
            }
            return PokerHand.FOUR_OF_A_KIND;
        }

// FULL_HOUSE
        if((freqFreq[3] >= 1 && freqFreq[2] >= 1) || freqFreq[3] >= 2)
        {
            boolean first = true;
            int keep_2 = -1;

            // Ace High
            if(numFreq[0] == 2)
                keep_2 = 0;
            else if(numFreq[0] == 3)
            {
                kickers.add(0);
                first = false;
            }

            for(int i = Card.MAX_NUMBER - 1; i >= 0; i--)
            {
                // 2-2(two pair, second)
                if(numFreq[i] == 3 && keep_2 != -1)
                {
                    kickers.add(i);
                    kickers.add(keep_2);
                    return PokerHand.FULL_HOUSE;
                }

                if(numFreq[i] >= 2)
                {
                    // first time pattern
                    if(first)
                    {
                        // 3-1(three of kind, first)
                        if(numFreq[i] == 3)
                        {
                            kickers.add(i);
                            first = false;
                        }
                        // 2-1(two pair, first)
                        else
                            keep_2 = i;
                    }
                    // 3-2(three of kind, second)
                    else if(keep_2 == -1)
                    {
                        kickers.add(i);
                        return PokerHand.FULL_HOUSE;
                    }
                }
            }
        }


// Flush
        int[] suitFreq = new int[Card.MAX_SUIT];
        for(int i = 0; i < hand.size(); i++)
        {
            suitFreq[hand.get(i).getSuit()]++;
        }

        int nMaxPosFlush = 0;
        for(int i = 0; i < Card.MAX_SUIT; i++)
        {
            if(suitFreq[i] > suitFreq[nMaxPosFlush])
            {
                nMaxPosFlush = i;
            }
        }
        if(suitFreq[nMaxPosFlush] >= EVAL_SIZE)
        {
            // Ace High
            int count = 0;
            if(contains(new Card(0, nMaxPosFlush)))
            {
                kickers.add(0);
                count++;
            }

            for(int i = size - 1; i >= 0; i--)
            {
                if(hand.get(i).getSuit() == nMaxPosFlush)
                {
                    kickers.add(hand.get(i).getNumber());
                    count++;
                }
                if(count == EVAL_SIZE)
                    return PokerHand.FLUSH;
            }
        }


// Straight
        for(int i = 1; i < hand.size(); i++)
        {
            if(hand.get(i).compareTo(hand.get(i - 1)) == 0)
                continue;
            if(hand.get(i).compareTo(hand.get(i - 1)) == 1)
            {
                consecutive++;
                if(consecutive >= EVAL_SIZE -2 && hand.get(i).getNumber() == 12 && hand.get(0).getNumber() == 0)
                {
                    kickers.add(0);
                    return PokerHand.ACE_STRAIGHT;
                }
                if(consecutive >= EVAL_SIZE - 1)
                {
                    straight = hand.get(i).getNumber();
                }
            }
            else
            {
                consecutive = 0;
            }
        }

        if(straight != -1)
        {
            kickers.add(straight);
            return PokerHand.STRAIGHT;
        }

// Three of a kind
        if(freqFreq[3] == 1)
        {
            for(int i = 0; i < Card.MAX_NUMBER; i++)
            {
                if(numFreq[i] == 3)
                {
                    kickers.add(i);
                    break;
                }
            }
            // Ace High
            int count = 0;
            if(numFreq[0] == 1)
            {
                kickers.add(0);
                count++;
            }
            for(int i = Card.MAX_NUMBER - 1; i >= 0; i--) {
                if(numFreq[i] == 1)
                {
                    kickers.add(i);
                    count++;
                }
//                if(count == 2)
//                    return PokerHand.THREE_OF_A_KIND;
                if(count == 2)
                    break;
            }
            return PokerHand.THREE_OF_A_KIND;
        }

// Two pair
        if(freqFreq[2] > 1)
        {
            // Ace High
            int count = 0;
            if(numFreq[0] == 2)
            {
                kickers.add(0);
                count++;
            }

            for(int i = Card.MAX_NUMBER - 1; i >= 0; i--)
            {
                if(numFreq[i] == 2)
                {
                    kickers.add(i);
                    count++;
                }
                if(count == 2)
                    break;
            }
            for(int i = Card.MAX_NUMBER - 1; i >= 0; i--) {
                if(numFreq[i] == 1)
                {
                    kickers.add(i);
//                    return PokerHand.TWO_PAIR;
                    break;
                }
            }
            return PokerHand.TWO_PAIR;
        }

// One pair
        if(freqFreq[2] == 1)
        {
            for(int i = 0; i < Card.MAX_NUMBER; i++)
            {
                if(numFreq[i] == 2)
                {
                    kickers.add(i);
                    break;
                }
            }
            // Ace High
            int count = 0;
            if(numFreq[0] == 1)
            {
                kickers.add(0);
                count++;
            }

            for(int i = Card.MAX_NUMBER - 1; i >= 0; i--) {
                if(numFreq[i] == 1)
                {
                    kickers.add(i);
                    count++;
                }
//                if(count == 3)
//                    return PokerHand.PAIR;
                if(count == 3)
                    break;
            }
            return PokerHand.PAIR;
        }

// High Card
        // Ace High
        int count = 0;
        if(numFreq[0] == 1)
        {
            kickers.add(0);
            count++;
        }
        for(int i = Card.MAX_NUMBER - 1; i >= 0; i--) {
            if(numFreq[i] == 1)
            {
                kickers.add(i);
                count++;
            }
            if(count == 5)
                break;
        }
        return PokerHand.HIGH_CARD;
    }


    @Override
    public int compareTo(Hand h)
    {
        if(strength == null)
            strength = evalHandAccuracy();
        if(h.getStrength() == null)
            h.setStrength(h.evalHandAccuracy());

        if(!strength.equals(h.getStrength()))
            return h.getStrength().getId() - strength.getId();

        for(int i = 0; i < kickers.size() && i < h.getKickers().size(); i++)
        {
            int n1 = kickers.get(i);
            int n2 = h.getKickers().get(i);

            if(n1 != n2)
            {
                if(i <= 1)
                {
                    if(n1 == 0)
                        return 1;
                    else if(n2 == 0)
                        return -1;
                }
                return n1 - n2;
            }

        }
        return 0;
    }

    public Iterator<Card> iterator()
    {
        return hand.listIterator();
    }

    public void paint(Graphics2D g)
    {
        for(Card card :hand)
        {
            card.paint(g);
        }
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if(size == 0)
            return "[]";
        sb.append("[").append(hand.get(0));
        for(int i = 1; i < hand.size(); i++)
        {
            sb.append(", ").append(hand.get(i).toString());
        }
        sb.append("]");
        return sb.toString();
    }
}
