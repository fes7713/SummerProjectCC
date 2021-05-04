package Tetris;

import java.awt.*;

// WIP
public class ScoreBoard 
{
    private static int score = 0;
    Game game;
    
    public void paint(Graphics2D g)
    {   
        g.drawRect(400, 50, 150, 200);
        g.drawString("Score : " + score, 410, 70);
    }
    
}
