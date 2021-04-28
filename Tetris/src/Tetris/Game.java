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

    public Game()
    {
        map = new int[MAX_ROW][MAX_COL];
        figures = new ArrayList<>();

        JFrame frame = new JFrame("Simple game");
        display = new Display(this);
        frame.add(display);


        frame.setSize(500, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void add(Figure fig)
    {
        figures.add(fig);
    }

    public List<Figure> getFigures()
    {
        return figures;
    }

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

    public int[][] getMap() {
        return map;
    }

    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();
        game.add(new Figure(game, 1, 1, 1, 1, true));
        game.add(new Figure(game, 4, 5, 3, 4, true));

        while(true)
        {
            game.display.repaint();
            Thread.sleep(70);
        }
    }
}
