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
    private final List<Figure> tetrisFigures = new ArrayList<>();
    Figure movingFigure;
    private boolean hit;

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

    public static Figure randFig(List<Figure> list)
    {
        int rand = (int)(Math.random() * list.size());
        Figure randomFig = list.get(rand);

        return randomFig;
    }

    // WIP
    // Main run function
    public void run() throws InterruptedException {
        gameInit();
        while(true)
        {
            if(hit)
            {
                movingFigure.setMovable(false);
                movingFigure = randFig(tetrisFigures);
                add(movingFigure);
                hit = false;
            }
            System.out.println(hit);
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

    // Test main
    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();

        game.run();
    }
}
