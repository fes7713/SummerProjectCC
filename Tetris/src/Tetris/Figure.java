package Tetris;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Figure {
    private int startLeft;
    private int startTop;
    private int row;
    private int column;
    private Box[][] boxMap;
    private Game game;
    private boolean movable;

    public Figure(Game game)
    {
        this(game, 0, 0, game.getMAX_ROW() , game.getMAX_COL(), false);
    }

    public Figure(Game game, int row, int column, boolean movable)
    {
        this(game, 0, 0, row, column, movable);
    }

    public Figure(Game game, int startLeft, int startTop, int row, int column, boolean movable)
    {
        this.game = game;
        this.row = row;
        this.column = column;
        this.startLeft = startLeft;
        this.startTop = startTop;
        this.movable = movable;
        boxMap = new Box[row][column];
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {
                boxMap[i][j] = new Box(game, i + startTop, j + startLeft);
            }
        }
    }

    public Figure(Game game, boolean[][] mapConfig, int row, int column, boolean movable)
    {
        this(game, row, column, movable);
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

    public void setCoordinates(int startTop, int startLeft)
    {
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {
                boxMap[i][j].setCoordinate(i+startTop, j + startLeft);
            }
        }
    }

    // Not working // Somebody make this
    public void move(){
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {

            }
        }
    }

    // https://strategywiki.org/wiki/Tetris/Rotation_systems
    // Use Super rotation system
    // WIP // Somebody make this
    public void rotate()
    {

    }

    // Somebody make this
    // Use this to check if figures are overlapping at the destination before actually moving the figure.
    public boolean intersect(Figure fig)
    {
        return false;
    }

    // Unchecked intersection or wall bound
    public void ketTyped(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            setCoordinates(startTop, --startLeft);
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            setCoordinates(++startTop, startLeft);
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            setCoordinates(startTop, ++startLeft);
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            setCoordinates(--startTop, startLeft);
        }
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

    public int getLeft() {
        return startLeft;
    }

    public int getTop() {
        return startTop;
    }

    public boolean isMovable()
    {
        return movable;
    }
}
