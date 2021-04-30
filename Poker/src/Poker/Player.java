package Poker;

public class Player {
    Hand hand;
    Money money;
    Game game;
    Action status;

    public Player(int money)
    {
        hand = new Hand();
        this.money = new Money(money);
        status = Action.CHECK;
    }

    public void bet(int amount)
    {
        game.getPot().add(amount);
        money.subtract(amount);
    }

    public void pickCard(Card card)
    {
        hand.addCard(card);
    }
}
