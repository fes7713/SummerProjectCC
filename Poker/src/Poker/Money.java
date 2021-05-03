package Poker;

public class Money implements Comparable<Money>{
    private int money;
    private Integer maximum;

    public Money()
    {
        money = 0;
        maximum = null;
    }

    public Money(int amount)
    {
        this.money = amount;
        maximum = null;
    }

    public Money(Money amount)
    {
        this.money = amount.getAmount();
        maximum = null;
    }

    public int getAmount()
    {
        return money;
    }

    public void setAmount(int amount)
    {
        if(maximum != null && amount > maximum)
            money = maximum;
        else
            money = amount;
    }

    public void setAmount(long amount)
    {
        if(maximum != null && amount > maximum)
            money = maximum;
        else
            money = (int)amount;
    }

    public void setMaximum(int max)
    {
        maximum = max;
    }

    public void add(int amount)
    {
        if(maximum != null && money + amount > maximum)
            money = maximum;
        else
            money += amount;
    }

    public void add(Money bet)
    {
        if(maximum != null && money + bet.getAmount() > maximum)
            money = maximum;
        else
            money += bet.getAmount();

    }

    public int subtract(int amount)
    {
        if(amount > money)
            return clear();
        else
        {
            money -= amount;
            return amount;
        }
    }

    public int subtract(Money bet)
    {
        if(bet.getAmount() > money)
            return clear();
        else
        {
            money -= bet.getAmount();
            return bet.getAmount();
        }
    }

    public int clear()
    {
        int temp = money;
        money = 0;
        return temp;
    }

    public void takePot(Money pot)
    {
        add(pot.clear());
    }

    public String toString()
    {
        return String.valueOf(money);
    }

    @Override
    public int compareTo(Money o) {
        return money - o.getAmount();
    }
}
