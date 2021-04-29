package Tetris;

// Use BlockType.java and implement all of Tetris block type.
// Use switch statement if you want.
public class TetrisFigure extends Figure
{
    static final boolean[][] figI = {
            {false, true, false, false},
            {false, true, false, false},
            {false, true, false, false},
            {false, true, false, false}
    };

    static final boolean[][] figJ = {
            {false, true, false},
            {false, true, false},
            {true, true, false}
    };

    static final boolean[][] figL = {
            {false, true, false},
            {false, true, false},
            {false, true, true}
    };

    static final boolean[][] figO = {
            {true, true, false},
            {true, true, false},
            {false, false, false}
    };

    static final boolean[][] figS = {
            {false, true, true},
            {true, true, false},
            {false, false, false}
    };

    static final boolean[][] figZ = {
            {true, true, false},
            {false, true, true},
            {false, false, false},
    };

    static final boolean[][] figT = {
            {true, true, true},
            {false, true, false},
            {false, false, false},
    };
    public TetrisFigure(Game game, BlockType blockType)
    {
        super(game, true);
        switch(blockType)
        {
            case I -> setMap(figI);
            case J -> setMap(figJ);
            case L -> setMap(figL);
            case O -> setMap(figO);
            case S -> setMap(figS);
            case T -> setMap(figT);
            case Z -> setMap(figZ);
        }

        
    }
}
