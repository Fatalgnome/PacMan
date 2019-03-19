package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Random;

/**
 *
 * This class should contain all your game logic
 */

public class Game {

    private Context context;
    private int points = 0;

    private Bitmap pacBitmap;
    private Bitmap coinBitMap;
    private Bitmap enemyBitMap;

    private TextView pointsView;
    private TextView timeView;
    private int pacx, pacy;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen
    private int amountofCoins = 10;
    private int coinsLeft;
    private boolean gameOver = false;
    private Random randy = new Random();
    private boolean coinsInit = false;
    private boolean enemiesInit = false;
    private boolean running = false;
    private boolean isPaused = false;

    private int up = 0;
    private int left = 1;
    private int down = 2;
    private int right = 3;
    private int curDir;
    private int enemyDir;
    private int time;

    public Game(Context context, TextView view, TextView timeView)
    {
        this.context = context;
        this.pointsView = view;
        this.timeView = timeView;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.golden);
        enemyBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    //TODO initialize goldcoins also here
    public void newGame()
    {
        pacx = 50;
        pacy = 400; //just some starting coordinates
        //reset the points
        points = 0;
        running = false;
        isPaused = false;
        time = 30;
        timeView.setText(context.getResources().getString(R.string.time) + " " + time);
        pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
        coinsInit = false;
        enemiesInit = false;
        gameOver = false;
        coinsLeft = amountofCoins;

        for (GoldCoin coin : coins)
        {
            coin.SetIsTaken(true);
        }
        for (Enemy enemy : enemies)
        {
            enemy.SetIsAlive(false);
        }

        gameView.invalidate(); //redraw screen
    }

    public void initCoins()
    {
        for (int i = 0; i < amountofCoins; i++)
        {
            coins.add(new GoldCoin(
                    randy.nextInt(gameView.w - coinBitMap.getWidth()),
                    randy.nextInt(gameView.h - coinBitMap.getHeight())));
        }
    }

    public void initEnemies()
    {
        for (int i = 0; i < 2; i++)
        {
            enemies.add(new Enemy(
                    randy.nextInt(gameView.w - enemyBitMap.getWidth()),
                    randy.nextInt(gameView.h - enemyBitMap.getHeight())));
        }
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void movePacmanRight()
    {
        curDir = right;
    }
    public void movePacmanLeft()
    {
        curDir = left;
    }
    public void movePacmanUp()
    {
        curDir = up;
    }
    public void movePacmanDown() {curDir = down;}


    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public void doCollisionCheck()
    {
        for (GoldCoin coin : coins)
        {
            if (Math.sqrt(((coin.getCoinx() - pacx) * (coin.getCoinx()- pacx))
                    + ((coin.getCoiny() - pacy) * (coin.getCoiny() - pacy))) <= 150
                    && !coin.isTaken() || Math.sqrt((-(coin.getCoinx() - pacx) * -(coin.getCoinx() - pacx))
                    + (-(coin.getCoiny() - pacy) * -(coin.getCoiny() - pacy))) <= 150
                    && !coin.isTaken() )
            {
                coin.SetIsTaken(true);
                points += coin.getValue();
                pointsView.setText(context.getText(R.string.points) +" "+ points);
                coinsLeft--;
                if (coinsLeft == 0)
                {
                    gameOver = true;
                }
            }
        }

    }
    public void enemyCollision()
    {
        for (Enemy enemy : enemies)
        {
            if (Math.sqrt(((enemy.getEnemyX() - pacx) * (enemy.getEnemyX() - pacx))
                    + ((enemy.getEnemyY() - pacy) * (enemy.getEnemyY() - pacy))) <= 100
                    && enemy.isAlive() || Math.sqrt((-(enemy.getEnemyX() - pacx) * -(enemy.getEnemyX() - pacx))
                    + (-(enemy.getEnemyY() - pacy) * -(enemy.getEnemyY() - pacy))) <= 100
                    && enemy.isAlive())
            {
                gameOver = true;
            }
        }
    }

    public int getPacx()
    {
        return pacx;
    }
    public int setPacx(int value){return pacx = value;}
    public int getPacy()
    {
        return pacy;
    }
    public int setPacy(int value){return pacy = value;}

    public int getCurDir(){return curDir;}
    public int getCurEnemyDir(){return enemyDir;}
    public int setEnemyDir(int value){return enemyDir = value;}
    public int getTime(){return time;}
    public int setTime(int value){return time = value;}

    public boolean isRunning(){return running;}
    public boolean setRunning(boolean value) {return running = value;}
    public boolean getIsPaused(){return isPaused;}
    public boolean setIsPaused(boolean value) {return isPaused = value;}

    public int getPoints()
    {
        return points;
    }

    public boolean getGameOver() {return gameOver;}
    public boolean setGameOver(boolean value) {return gameOver = value;}

    public boolean CoinsInit() { return coinsInit; }
    public boolean setCoinsInit(boolean value) {return coinsInit = value;}
    public boolean EnemiesInit() { return enemiesInit; }
    public boolean setEnemiesInit(boolean value) {return enemiesInit = value;}


    public ArrayList<GoldCoin> getCoins(){return coins;}
    public ArrayList<Enemy> getEnemies(){return enemies;}

    public Bitmap getPacBitmap() {return pacBitmap;}
    public Bitmap getCoinBitMap(){return coinBitMap;}
    public Bitmap getEnemyBitMap(){return enemyBitMap;}


}
