package Tetris;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private int MAX_ROW = 20;
    private int MAX_COL = 10;
    private final int BOX_SIZE = 30;
    private final int  START_POS = 20;

    private int[][] map;
    private Display display;
    private ArrayList<Figure> figures;
    private ScoreBoard score = new ScoreBoard();

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
        frame.setSize(500, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Adding figure to the list
    public void add(Figure fig)
    {
        figures.add(fig);
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

    // Test main
    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();

        // Test game. Use arrow keys to move figures
        game.add(new Figure(game, 1, 1, 1, 1, true));
        game.add(new Figure(game, 4, 5, 3, 4, false));

        boolean[][] testBoxMap = {
                {false, true, true},
                {false, true, false},
                {true, true, false}
        };
        game.add(new Figure(game, testBoxMap, 1, 3, true));

        game.run();
    }
}
