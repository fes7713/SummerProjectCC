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



    // Set up box map, When true, show box. When false, hide box.
    // In the constructor, default is true so everything is shown.
    // But by using this function, you can update the box map configuration.
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

    // Not working // Somebody make this
    // Maybe we dont even need this one.
//    public void move(){
//        for(int i = 0; i < row; i++)
//        {
//            for(int j = 0; j < column; j++)
//            {
//
//            }
//        }
//    }



    // https://strategywiki.org/wiki/Tetris/Rotation_systems
    // Use Super rotation system
    // WIP // Somebody make this
    public void rotate()
    {

    }

    // Somebody make this
    // Use this to check if figures are overlapping at the destination before actually moving the figure in keyTyped func.
    public boolean intersect(Figure fig)
    {
        return false;
    }


    // Combine two figure and create one bif figure.
    // It is used to make ground block. Like when figure hits ground, ground gets bigger and become one block.
    // It is better way of managing figure because we dont need to keep many figures.
    public void combine(Figure fig)
    {

    }

    // Unchecked intersection or wall bound
    // Get key event and update figure positions.
    public void keyPressed(KeyEvent e)
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
