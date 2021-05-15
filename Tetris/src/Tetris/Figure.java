package Tetris;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

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
        startLeft = 4;
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

    public Figure(Game game, Box[][] newBoxMap, int startLeft, int startTop, boolean movable)
    {
        this(game, startLeft, startTop);
        this.movable = movable;

        row = newBoxMap.length;
        column = newBoxMap[0].length;
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
                boxMap[i][j].setActive(newBoxMap[i][j].getActive());
            }
        }
    }

    public boolean isActiveCell(int row, int col)
    {
        return boxMap[row][col].getActive();
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
    // Done
    public void rotate()
    {
        Box[][] newBoxMap = new Box[boxMap[0].length][boxMap.length];
        for(int i = 0; i < boxMap.length; i++)
        {
            for(int j = 0; j < boxMap[0].length; j++)
            {
                newBoxMap[j][boxMap.length - i - 1] = new Box(game, boxMap[i][j].getX(), boxMap[i][j].getY(), Game.BOX_SIZE, Game.BOX_SIZE);
                newBoxMap[j][boxMap.length - i - 1].setActive(boxMap[i][j].getActive());
            }
        }

        Figure test = new Figure(game, newBoxMap, startLeft, startTop, movable);
        if(!test.collision(this))
        {
            boxMap = newBoxMap;
            row = boxMap.length;
            column = boxMap[0].length;
        }
    }

    // true == yap, intersection
    // false == nope, no intersections
    public boolean intersects(Figure fig)
    {
        for(int i = 0; i < row; i++)
        {
            for(int j = 0; j < column; j++)
            {
                int y = i + startTop - fig.startTop;
                int x = j + startLeft- fig.startLeft;

                if(y < 0 || y >= fig.row || x < 0 || x >= fig.column)
                    continue;
                if(isActiveCell(i, j) && fig.isActiveCell(y, x))
                    return true;
            }
        }
        return false;
    }

    // Done
    public boolean collision(Figure exclude)
    {
        List<Figure> figures = game.getFigures();

        if(intersects(game.getWall()))
            return true;
        for(Figure figure : figures)
        {
            if(intersects(figure) && !exclude.equals(figure))
                return true;
        }
        return false;
    }

    // Need Update
    // Unchecked intersection or wall bound
    // Get key event and update figure positions.
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            Figure newFig = new Figure(game, boxMap, startLeft-1, startTop, movable);
            if(!newFig.collision(this))
                setCoordinates(startTop, --startLeft);
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            Figure newFig = new Figure(game, boxMap, startLeft, startTop+1, movable);
            if(!newFig.collision(this))
                setCoordinates(++startTop, startLeft);
            else
                game.hitBottom();
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            Figure newFig = new Figure(game, boxMap, startLeft+1, startTop, movable);
            if(!newFig.collision(this))
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

    public void setMovable(boolean movable)
    {
        this.movable = movable;
    }



    public static void main(String[] args)
    {
        Figure fig1 = new Figure(null, 0, 0, 2, 2, false);
        Figure fig2 = new Figure(null, 2, 2, 2, 2, false);

        System.out.println(fig2.intersects(fig1));
    }
}
