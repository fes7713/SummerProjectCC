package Tetris;

// Use BlockType.java and implement all of Tetris block type.
// Use switch statement if you want.
public class TetrisFigure extends Figure
{
    public TetrisFigure(Game game) 
    {
        super(game);
        
    }
    
    public void figI(Game game)
    {
        boolean[][] fig = {
                {false, true, false},
                {false, true, false},
                {false, true, false},
                {false, true, false}
        };
        game.add(new Figure(game, fig, 4, 0, true));
    }
    
    public void figJ(Game game)
    {
        boolean[][] fig = {
                {false, true, false},
                {false, true, false},
                {true, true, false}
        };
        game.add(new Figure(game, fig, 4, 0, true));
    }
    
    public void figL(Game game)
    {
        boolean[][] fig = {
                {false, true, false},
                {false, true, false},
                {false, true, true}
        };
        game.add(new Figure(game, fig, 4, 0, true));
    }
    
    public void figS(Game game)
    {
        boolean[][] fig = {
                {false, true, true},
                {true, true, false},
        };
        game.add(new Figure(game, fig, 4, 0, true));
    }
    
    public void figZ(Game game)
    {
        boolean[][] fig = {
                {true, true, false},
                {false, true, true},
        };
        game.add(new Figure(game, fig, 4, 0, true));
    }
    
    public void figT(Game game)
    {
        boolean[][] fig = {
                {true, true, true},
                {false, true, false},
        };
        game.add(new Figure(game, fig, 4, 0, true));
    }
    
    public void figO(Game game)
    {
        boolean[][] fig = {
                {true, true, false},
                {true, true, false},
        };
        game.add(new Figure(game, fig, 4, 0, true));
    }
}
