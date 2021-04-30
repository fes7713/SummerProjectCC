package Poker;

public class Money {
    private int money;

    public Money(int amount)
    {
        this.money = amount;
    }

    public int getAmount()
    {
        return money;
    }

    public void add(int amount)
    {
        money += amount;
    }

    public void add(Money bet)
    {
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
}
