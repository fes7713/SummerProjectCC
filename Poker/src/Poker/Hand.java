package Poker;

import java.util.*;

public class Hand implements Comparable<Hand>{
    private final List<Card> hand;
    private int size;
    private PokerHand strength;
    private final List<Integer>kickers;
    private final int  EVAL_SIZE = 5;
    private final SortOrder sortOrder;

    public Hand()
    {
        this.size = 0;
        hand = new ArrayList<>();
        kickers = new ArrayList<>();
        sortOrder = SortOrder.NUMBER;
        setSortOrder(sortOrder);
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

    public List<Card> getHand() {
        return hand;
    }

    public void drawCards(Deck deck, int size)
    {
        for(int i = 0; i < size; i++)
        {
            hand.add(deck.pop());
        }
        strength = evalHand();
        this.size++;
        strength = evalHand();
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
                    return PokerHand.ACE_HIGH_STRAIGHT;
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
                    return PokerHand.ACE_HIGH_STRAIGHT;
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
                if(count == 2)
                    return PokerHand.THREE_OF_A_KIND;
            }
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
                    return PokerHand.TWO_PAIR;
                }
            }
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
                if(count == 3)
                    return PokerHand.PAIR;
            }
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

    public void addCard(Card card)
    {
        hand.add(card);
        strength = evalHand();
        size++;
    }

    public List<Integer> getKickers()
    {
        return kickers;
    }

    private int compareNumbers(Hand h)
    {
        sort();
        h.sort();
        for(int i = 0; i < size; i++)
        {
            if(hand.get(i).getNumber() == h.get(i).getNumber())
                continue;
            else
            {
                return hand.get(i).getNumber() - h.get(i).getNumber();
            }
        }
        return 0;
    }

    private int getHighestConsecutive(List<Card> hand, int nCons)
    {
        int count = 0;
        for(int i = hand.size() - 1; i > 0; i--)
        {
            if(hand.get(i).getNumber() == hand.get(i - 1).getNumber())
                count++;
            else
                count = 0;
            if(count >= nCons - 1)
            {
                return hand.get(i).getNumber();
            }
        }

        throw new IndexOutOfBoundsException("Error Input");
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

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(hand.get(0));
        for(int i = 1; i < hand.size(); i++)
        {
            sb.append(", ").append(hand.get(i).toString());
        }
        sb.append("]");
        return sb.toString();
    }

    public void testHands()
    {
        Map<PokerHand, Integer> ans7 = new HashMap<>();
        ans7.put(PokerHand.ROYAL_FLUSH, 4324);
        ans7.put(PokerHand.STRAIGHT_FLUSH, 37260);
        ans7.put(PokerHand.FOUR_OF_A_KIND, 224848);
        ans7.put(PokerHand.FULL_HOUSE, 3473184);
        ans7.put(PokerHand.FLUSH, 4047644);
        ans7.put(PokerHand.ACE_HIGH_STRAIGHT, 747980);
        ans7.put(PokerHand.STRAIGHT, 5432040);
        ans7.put(PokerHand.THREE_OF_A_KIND, 6461620);
        ans7.put(PokerHand.TWO_PAIR, 31433400);
        ans7.put(PokerHand.PAIR, 58627800);
        ans7.put(PokerHand.HIGH_CARD, 23294460);

        Map<PokerHand, Integer> frequency = new HashMap<>();
        for(PokerHand strength: PokerHand.values())
        {
            frequency.put(strength, 0);
        }
        Hand hand1;


        for(int i = 0; i < Deck.SIZE; i++)
        {
            System.out.print(i);
            for(int j = 0; j < i; j++)
            {
                for(int k = 0; k < j; k++)
                {
                    for(int p = 0; p < k; p++)
                    {
                        for(int q = 0; q < p; q++)
                        {
                            for(int x = 0; x < q; x++)
                            {
                                for(int y = 0; y < x; y++)
                                {
                                    hand1 = new Hand(new int[]{y, x, q, p, k, j, i});

                                    PokerHand strength1 = hand1.evalHandAccuracy();
//                                    PokerHand strength2 = hand1.evalHand();
                                    frequency.put(strength1, frequency.get(strength1)+1);
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println();
        System.out.println(frequency);

        int count = 0;
        for(Integer n : frequency.values())
        {
            count += n;
        }
        System.out.println(count);
        System.out.println("Ans "  + ans7.equals(frequency));

        for(PokerHand pk : PokerHand.values())
        {
            int nAns = ans7.get(pk);
            int nTest = frequency.get(pk);
            if(nAns != nTest)
            {
                System.out.println(pk);
                System.out.println("Ans :" + nAns + " Test :" + nTest);
            }
        }

    }

    public static void main(String[] args)
    {

        Hand hand1 = new Hand(new int[]{0, 5, 3, 2, 1, 13, 8});
        Hand hand2 = new Hand(new int[]{0, 5, 3, 2, 1, 13, 9});
        System.out.println(hand1 + " :" +  hand1.evalHandAccuracy() + " " + hand1.getKickers());
        System.out.println(hand2 + " :" +  hand2.evalHandAccuracy() + " " + hand2.getKickers());
        if(hand1.compareTo(hand2) > 0)
            System.out.println("Player 1 win");
        else
            System.out.println("Player 2 win");
        System.out.println(hand1.getKickers());
//        hand.testHands();
    }

}
