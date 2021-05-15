package Tetris;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {
    static final int MAX_ROW = 20;
    static final int MAX_COL = 10;
    static final int BOX_SIZE = 30;
    static final int  START_POS = 50;

    private int[][] map;
    private Display display;
    private ArrayList<Figure> figures;
    private ScoreBoard score = new ScoreBoard();
    private final List<TetrisFigure> tetrisFigures = new ArrayList<>();
    Figure movingFigure;
    private boolean hit;
    private Figure wall;
//    private TetrisFigure movingFigure;

    // Done
    public Game()
    {
        // Collision
        hit = false;
        // If you want u can use 2D map to represent the tetris table
        map = new int[MAX_ROW][MAX_COL];

        // This is figure list and all of things in this list will be displayed on the display.
        figures = new ArrayList<>();

        drawWall();

        // This part creates the window.
        // No need to edit this part and just use this as it is.
        JFrame frame = new JFrame("Simple game");
        display = new Display(this);
        frame.add(display);
        frame.setSize(600, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    // Adding figure to the list
    public void add(Figure fig)
    {
        figures.add(fig);
    }

    public TetrisFigure randFig(List<TetrisFigure> list)
    {
        int rand = (int)(Math.random() * list.size());
        TetrisFigure randomFig = new TetrisFigure(this, list.get(rand).getBlockType());

        return randomFig;
    }

    public void drawWall()
    {
        boolean[][] map = new boolean[MAX_ROW + 1][MAX_COL + 2];
        for(int i = 0; i < MAX_ROW + 1; i++)
        {
            for(int j = 0; j < MAX_COL + 2; j++)
            {
                if(i == MAX_ROW || j == 0 || j == MAX_COL + 1)
                    map[i][j] = true;
                else
                    map[i][j] = false;

            }
        }
        wall = new Figure(this, map, -1, 0, false);
    }

    public Figure getWall()
    {
        return wall;
    }
    // WIP
    // Main run function
    public void run() throws InterruptedException {
        gameInit();
        while(true)
        {

            if(hit)
            {
                System.out.println(hit);
                movingFigure.setMovable(false);
                movingFigure = randFig(tetrisFigures);
                add(movingFigure);
                hit = false;
            }
            System.out.println(checkLineFilled());

            // Display class is responsible for updating the game display.
            this.display.repaint();
//            Thread.sleep(70);
        }
    }

    // WIP
    // Set up display, score board and generate first block.
    private void gameInit()
    {
        tetrisFigures.add(new TetrisFigure(this, BlockType.I));
        tetrisFigures.add(new TetrisFigure(this, BlockType.J));
        tetrisFigures.add(new TetrisFigure(this, BlockType.L));
        tetrisFigures.add(new TetrisFigure(this, BlockType.S));
        tetrisFigures.add(new TetrisFigure(this, BlockType.Z));
        tetrisFigures.add(new TetrisFigure(this, BlockType.T));
        tetrisFigures.add(new TetrisFigure(this, BlockType.O));
        movingFigure = randFig(tetrisFigures);
        add(movingFigure);
    }

    // Done
    public List<Figure> getFigures()
    {
        return figures;
    }

    // Done
    public int getMAX_ROW() {
        return MAX_ROW;
    }

    public int getMAX_COL() {
        return MAX_COL;
    }

    public int getBOX_SIZE() {
        return BOX_SIZE;
    }

    public int getSTART_POS() {
        return START_POS;
    }

    public ScoreBoard getScoreBoard()
    {
        return score;
    }

    // Maybe Delete
    public int[][] getMap() {
        return map;
    }

    public void hitBottom()
    {
        hit = true;
    }

    public boolean checkLineFilled()
    {
        int[][] map = new int[MAX_ROW][MAX_COL];

        for(Figure fig : figures)
        {
            if(!fig.isMovable())
            {
                int left = fig.getLeft();
                int top = fig.getTop();
                for(int i = 0; i < fig.getRow(); i++)
                {
                    for(int j = 0; j < fig.getColumn(); j++)
                    {
                        boolean a = i + top < 0 || i + top > MAX_ROW - 1;
                        boolean b = j + left < 0 || j + left > MAX_COL - 1;

                        if((a || b) && fig.isActiveCell(i, j))
                            System.out.println("Error Collision failed");
                        else if((a || b) && !fig.isActiveCell(i, j))
                            System.out.println("Collision Working");
                        else
                        {
                            map[i + top][j + left] = fig.isActiveCell(i, j) ? 1 : 0;
                        }


                    }
                }
            }
        }
        boolean flag = false;
        for(int i = 0; i < MAX_ROW; i++)
        {
            for(int j = 0; j < MAX_COL; j++)
            {
                if(map[i][j] != 1)
                    break;
                else
                {
                    flag = true;
                    if(j == MAX_COL - 1)
                    {
                        // Delete line
                        if(i > 0)
                        {
                            for(int p = i - 1; p >= 0; p--)
                            {
                                for(int q = 0; q < MAX_COL; q++)
                                {
                                    map[p + 1][q] = map[p][q];
                                }
                            }
                        }

                        for(int p = 0; p < MAX_COL; p++)
                        {
                            map[0][p] = 0;
                        }
                    }
                }
            }
        }
        return flag;
    }

    // Test main
    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();

        game.run();
    }
}
