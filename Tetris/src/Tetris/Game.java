package Tetris;

import javax.swing.*;
import java.util.Scanner;

public class Game {
    private int MAX_ROW = 20;
    private int MAX_COL = 10;
    private final int BOX_SIZE = 30;
    private final int  START_POS = 20;

    private int[][] map;
    Display display;


    public Game()
    {
        display = new Display(this);
        map = new int[MAX_ROW][MAX_COL];
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
        JFrame frame = new JFrame("Simple game");
        Display display = new Display(game);
        frame.add(display);


        frame.setSize(500, 700);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Scanner kb = new Scanner(System.in);
        int row, col;
        while(true)
        {
            display.repaint();
            Thread.sleep(70);
        }
    }
}
