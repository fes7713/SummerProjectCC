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
//    private TetrisFigure movingFigure;

    // Done
    public Game()
    {
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
            // Display class is responsible for updating the game display.
            this.display.repaint();
            Thread.sleep(70);
        }
    }

    // WIP
    // Set up display, score board and generate first block.
    private void gameInit()
    {

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

    }

    // Test main
    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();

        //just testing
        List<Figure> list = new ArrayList<>();
        list.add(new TetrisFigure(game, BlockType.I));
        list.add(new TetrisFigure(game, BlockType.J));
        list.add(new TetrisFigure(game, BlockType.L));
        list.add(new TetrisFigure(game, BlockType.S));
        list.add(new TetrisFigure(game, BlockType.Z));
        list.add(new TetrisFigure(game, BlockType.T));
        list.add(new TetrisFigure(game, BlockType.O));

        Figure movingFigure = randFig(list);
        game.add(movingFigure);



        game.run();
    }
}
