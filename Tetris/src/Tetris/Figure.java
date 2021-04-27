package Tetris;

import java.awt.*;

public class Figure {
    private int startLeft;
    private int startTop;
    private int row;
    private int column;
    private Box[][] boxMap;
    private Game game;

    public Figure(Game game)
    {
        this(game, 0, 0, game.getMAX_ROW() , game.getMAX_COL());
    }

    public Figure(Game game, int row, int column)
    {
        this(game, 0, 0, row, column);
    }

    public Figure(Game game, int startLeft, int startTop, int row, int column)
    {
        this.game = game;
        this.row = row;
        this.column = column;
        this.startLeft = startLeft;
        this.startTop = startTop;
        boxMap = new Box[row][column];
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {
                boxMap[i][j] = new Box(game, i + startTop, j + startLeft);
            }
        }
    }

    public Figure(Game game, boolean[][] mapConfig, int row, int column)
    {
        this(game, row, column);
        setMap(mapConfig);
    }

    public void setMap(boolean[][] mapConfig)
    {
        if(mapConfig.length != boxMap.length && mapConfig[0].length != boxMap[0].length)
        {
            throw new IndexOutOfBoundsException("Map Dimension does not match");
        }
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {
                boxMap[i][j].setActive(mapConfig[i][j]);
            }
        }
    }

    // Not working
    public void move() throws Exception {
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {
//                if(boxMap[i][j] == 1 && game.getMap()[i][j] != 0)
//                    throw new Exception("Box Overlay");
            }
        }
    }

    // WIP
    public void rotate()
    {

    }

    public void paint(Graphics2D g)
    {
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {
                if(boxMap[i][j].getActive())
                    boxMap[i][j].paint(g);
            }
        }
    }
}
