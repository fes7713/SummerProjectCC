package Poker;

import java.util.*;

public class Hand implements Comparable<Hand>{
    private List<Card> hand;
    private int size;
    private PokerHand strength;
    private List<Integer>kickers;
    private final int  EVAL_SIZE = 5;
    private SortOrder sortOrder;

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

    public int getSize()
    {
        return size;
    }

    public void drawCards(Deck deck, int size)
    {
        for(int i = 0; i < size; i++)
        {
            hand.add(deck.pop());
        }
        strength = evalHand();
        this.size++;
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
//        for(Card card: hand)
//        {
//            card.setSortOrder(order);
//        }
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
        boolean straight = false;
        boolean flush = false;
        int straight_flush = -1;
        int consecutive = 0;

        // Flush
        setSortOrder(SortOrder.SUIT);
        sort();
        for(int i = 0; i < hand.size() - 1; i++)
        {
            if(hand.get(i).compareTo(hand.get(i + 1)) == 0)
            {
                consecutive++;
            }
            else
            {
                if(consecutive >= EVAL_SIZE - 1)
                {
                    flush = true;
                }
                consecutive = 0;
            }
        }
        if(consecutive >= EVAL_SIZE - 1)
        {
            flush = true;
        }

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
                if(consecutive >= 3 && hand.get(i).getNumber() == 12 && hand.contains(new Card(0, hand.get(i).getSuit())))
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
        for(int i = 0; i < Card.MAX_NUMBER; i++)
        {
            numFreq[i] = 0;
        }

        // Counting number frequency
        for(int i = 0; i < hand.size() - 1; i++)
        {
            if(hand.get(i).getNumber() == hand.get(i + 1).getNumber()) {
                int num = hand.get(i).getNumber();
                numFreq[num]++;
            }
        }

        // Counting frequency of number frequency

        int[] freqFreq = new int[Card.MAX_SUIT];
        for(int i = 0; i < Card.MAX_SUIT; i++)
        {
            freqFreq[i] = 0;
        }

        for(int i = 0; i < Card.MAX_NUMBER; i++)
        {
            freqFreq[numFreq[i]]++;
        }

        if(freqFreq[3] >= 1)
            return PokerHand.FOUR_OF_A_KIND;
        if((freqFreq[2] >= 1 && freqFreq[1] >= 1) || freqFreq[2] >= 2)
            return PokerHand.FULL_HOUSE;
        if(flush)
            return PokerHand.FLUSH;

// Straight
        for(int i = 0; i < hand.size() - 1; i++)
        {
            if(hand.get(i).compareTo(hand.get(i + 1)) == 0)
                continue;
            if(hand.get(i).compareTo(hand.get(i + 1)) == -1)
            {
                consecutive++;
                if(hand.get(i).getNumber() == 11 &&
                        hand.get(0).getNumber() == 0)
                    consecutive++;
            }
            else
            {
                if(consecutive >= EVAL_SIZE - 1)
                {
                    straight = true;
                }
                consecutive = 0;
            }
        }

        if(consecutive >= EVAL_SIZE - 1 || straight)
        {
            return PokerHand.STRAIGHT;
        }

        if(freqFreq[2] == 1)
            return PokerHand.THREE_OF_A_KIND;
        if(freqFreq[1] > 1)
            return PokerHand.TWO_PAIR;
        if(freqFreq[1] == 1)
            return PokerHand.PAIR;

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

    @Override
    public int compareTo(Hand h)
    {
        List<Integer> kickersOpp = h.getKickers();
        ListIterator<Integer> iter = kickers.listIterator();
        ListIterator<Integer> iterOpp = kickersOpp.listIterator();

        while(iter.hasNext() && iterOpp.hasNext())
        {
            int diff = iter.next() - iterOpp.next();

            if(diff != 0)
                return diff;
        }
        return 0;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(hand.get(0));
        for(int i = 1; i < hand.size(); i++)
        {
            sb.append(", ");
            sb.append(hand.get(i).toString());
        }
        sb.append("]");
        return sb.toString();
    }

    public void testHands()
    {
        Map<PokerHand, Integer> ans7 = new HashMap();
        ans7.put(PokerHand.ROYAL_FLUSH, 4324);
        ans7.put(PokerHand.STRAIGHT_FLUSH, 37260);
        ans7.put(PokerHand.FOUR_OF_A_KIND, 224848);
        ans7.put(PokerHand.FULL_HOUSE, 3473184);
        ans7.put(PokerHand.FLUSH, 4047644);
        ans7.put(PokerHand.STRAIGHT, 6180020);
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
        int stop;
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
//                            hand1 = new Hand(new int[]{q, p, k, j, i});
//                            PokerHand strength = hand1.evalHand();
//                            if(strength.equals(PokerHand.ROYAL_FLUSH))
//                                stop = 1;
//                            frequency.put(strength, frequency.get(strength)+1);

                            for(int x = 0; x < q; x++)
                            {
//                                hand1 = new Hand(new int[]{x, q, p, k, j, i});
//                                PokerHand strength = hand1.evalHand();
//                                frequency.put(strength, frequency.get(strength)+1);
                                for(int y = 0; y < x; y++)
                                {
                                    hand1 = new Hand(new int[]{y, x, q, p, k, j, i});
                                    PokerHand strength = hand1.evalHand();
                                    frequency.put(strength, frequency.get(strength)+1);
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
        System.out.println("Ans "  + String.valueOf(ans7.equals(frequency)));
    }

    public static void main(String[] args)
    {

        Hand hand = new Hand(new int[]{24, 10, 23, 35, 8, 7});


        System.out.println(hand);
        System.out.println(hand.evalHand());
        hand.testHands();
    }

}
