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

    // When it is true, you can move figure by pressing arrow keys.
    private boolean movable;


    // Constructors with different signatures.
    public Figure(Game game, boolean movable)
    {
        this.game = game;
        startLeft = 0;
        startTop = 0;
        this.movable = movable;
    }

    public Figure(Game game, int startLeft, int startTop)
    {
        this.game = game;
        this.startLeft = startLeft;
        this.startTop = startTop;
        movable = false;
    }

    public Figure(Game game, int row, int column, boolean movable)
    {
        this(game, 0, 0, row, column, movable);
    }

    public Figure(Game game, int startLeft, int startTop, int row, int column, boolean movable)
    {
        this(game, startLeft, startTop);
        this.row = row;
        this.column = column;
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

    public Figure(Game game, boolean[][] mapConfig, int startLeft, int startTop, boolean movable)
    {
        this(game, startLeft, startTop);
        this.movable = movable;
        setMap(mapConfig);
    }


    // Done
    // Set up box map, When true, show box. When false, hide box.
    // In the constructor, default is true so everything is shown.
    // But by using this function, you can update the box map configuration.
    public void setMap(boolean[][] mapConfig)
    {
        row = mapConfig.length;
        column = mapConfig[0].length;
        boxMap = new Box[row][column];

        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {
                boxMap[i][j] = new Box(game, i + startTop, j + startLeft);
            }
        }

        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {
                boxMap[i][j].setActive(mapConfig[i][j]);
            }
        }
    }

    // Done
    // Update all of box coordinates.
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


    // https://strategywiki.org/wiki/Tetris/Rotation_systems
    // Use Super rotation system
    // WIP // Somebody make this
    public void rotate()
    {
        Box[][] newBoxMap = new Box[boxMap[0].length][boxMap.length];
        for(int i = 0; i < boxMap.length; i++)
        {
            for(int j = 0; j < boxMap[0].length; j++)
            {
                newBoxMap[j][boxMap.length - i - 1] = boxMap[i][j];
            }
        }
        boxMap = newBoxMap;
        row = boxMap.length;
        column = boxMap[0].length;
    }

    // WIP
    // Somebody make this
    // Use this to check if figures are overlapping at the destination before actually moving the figure in keyTyped func.
    public boolean intersect(int addX, int addY)
    {
        Rectangle rect = new Rectangle(startLeft + addX, startTop + addY, column, row);
        for(Figure fig : game.getFigures())
        {
            Rectangle rect1 = new Rectangle(fig.getLeft(), fig.getTop(), fig.getColumn(), fig.getRow());
            if(rect.intersects(rect1))
            {
                return true;
            }
        }
        return false;
    }


    // WIP
    // Combine two figure and create one bif figure.
    // It is used to make ground block. Like when figure hits ground, ground gets bigger and become one block.
    // It is better way of managing figure because we dont need to keep many figures.
    public void combine(Figure fig)
    {
        int left, top;
        if(startLeft < fig.getLeft())
            left = startLeft;
        else
            left = fig.getLeft();

        if(startTop < fig.getTop())
            top = startTop;
        else
            top = fig.getTop();

//        Box[][]newBoxMap = new Box[][];
    }

    // Need Update
    // Unchecked intersection or wall bound
    // Get key event and update figure positions.
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
//            if(!intersect(-1, 0))
                setCoordinates(startTop, --startLeft);
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
//            if(!intersect(-1, 0))
                setCoordinates(++startTop, startLeft);
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            setCoordinates(startTop, ++startLeft);
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            rotate();
            setCoordinates(startTop, ++startLeft);
            setCoordinates(startTop, --startLeft);
        }
    }

    // Done
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

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isMovable()
    {
        return movable;
    }
}
