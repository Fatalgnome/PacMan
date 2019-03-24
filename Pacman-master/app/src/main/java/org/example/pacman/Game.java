package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;


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
    private Bitmap blueBitMap;

    private TextView pointsView;
    private TextView timeView;
    private TextView blueCoinActive;
    private int pacx, pacy;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<BlueCoin> blueCoins = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen
    private int amountofCoins = 10;
    private int coinsLeft;
    private boolean gameOver = false;
    private Random randy = new Random();
    private boolean coinsInit = false;
    private boolean blueInit = false;
    private boolean bluePickup = false;
    private boolean enemiesInit = false;
    private boolean enemySpawned = false;
    private boolean running = false;
    private boolean isPaused = false;

    private int up = 0;
    private int left = 1;
    private int down = 2;
    private int right = 3;
    private int curDir;
    private int time;

    public Game(Context context, TextView view, TextView timeView, TextView blueView)
    {
        this.context = context;
        this.pointsView = view;
        this.timeView = timeView;
        this.blueCoinActive = blueView;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.golden);
        enemyBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        blueBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coinblue);
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void newGame()
    {
        pacx = 50;
        pacy = 400; //just some starting coordinates
        //reset the points
        points = 0;
        running = false;
        isPaused = false;
        time = 30;
        coinsInit = false;
        enemiesInit = false;
        gameOver = false;
        bluePickup = false;
        coinsLeft = amountofCoins;
        timeView.setText(context.getResources().getString(R.string.time) + " " + time);
        pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
        blueCoinActive.setText(context.getResources().getString(R.string.blueCoin) + " " + bluePickup);


        for (GoldCoin coin : coins)
        {
            coin.SetIsTaken(true);
        }
        for (Enemy enemy : enemies)
        {
            enemy.SetIsAlive(false);
        }
        for (BlueCoin blue : blueCoins)
        {
            blue.SetisTaken(true);
            blueCoins.remove(blue);
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
        int enemyX;
        int enemyY;
        for (int i = 0; i < 3; i++)
        {
            enemySpawned = false;
            while(!enemySpawned)
            {
                enemyX = randy.nextInt(gameView.w - enemyBitMap.getWidth());
                enemyY = randy.nextInt(gameView.h - enemyBitMap.getHeight());

                if (enemyX >= pacx + 100 && enemyY >= pacy + 150)
                {
                    enemies.add(new Enemy(enemyX, enemyY));
                    enemySpawned = true;
                }

            }

        }
    }

    public void initBlueCoin()
    {
        blueCoins.add(new BlueCoin(
                randy.nextInt(gameView.w - blueBitMap.getWidth()),
                randy.nextInt(gameView.h - blueBitMap.getHeight())));
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


    public void doCollisionCheck()
    {
        for (GoldCoin coin : coins)
        {
            int dx = coin.getCoinx() - pacx;
            int dy = coin.getCoiny() - pacy;
            double d = Math.sqrt((dx * dx) + (dy * dy));
            int r1 = pacBitmap.getHeight() + pacBitmap.getWidth() - 200;
            int r2 = coinBitMap.getHeight() + coinBitMap.getWidth() - 200;
            if (d <= r1 + r2 && !coin.isTaken() )
            {
                coin.SetIsTaken(true);
                points += coin.getValue();
                pointsView.setText(context.getText(R.string.points) +" "+ points);
                coinsLeft--;
                if (coinsLeft == 0)
                {
                    gameOver = true;
                    GameOver(true);
                }
            }
        }

        for (Enemy enemy : enemies)
        {
            int dx = enemy.getEnemyX() - pacx;
            int dy = enemy.getEnemyY() - pacy;
            double d = Math.sqrt((dx * dx) + (dy * dy));
            int r1 = pacBitmap.getHeight() + pacBitmap.getWidth() - 200;
            int r2 = enemyBitMap.getHeight() + enemyBitMap.getWidth() - 200;

            if (d <= r1 + r2 && enemy.isAlive() && !bluePickup)
            {
                gameOver = true;
                GameOver(false);
            }
            else if(d <= r1 + r2 && enemy.isAlive() && bluePickup)
            {
                points += 100;
                pointsView.setText(context.getText(R.string.points) + " " + points);
                enemy.SetIsAlive(false);
            }
        }

        for (BlueCoin coin : blueCoins)
        {
            int dx = coin.getBluex() - pacx;
            int dy = coin.getBluey() - pacy;
            double d = Math.sqrt((dx * dx) + (dy * dy));
            int r1 = pacBitmap.getHeight() + pacBitmap.getWidth() - 200;
            int r2 = blueBitMap.getHeight() + blueBitMap.getWidth() - 200;
            if (d <= r1 + r2 && !coin.isTaken())
            {
                coin.SetisTaken(true);
                bluePickup = true;
                blueCoinActive.setText(context.getResources().getString(R.string.blueCoin) + " " + bluePickup);
            }
        }
    }

    public void GameOver(boolean victory)
    {
        Toast toast;
        if (!victory)
        {
            toast = Toast.makeText(context, context.getText(R.string.gameOver)+ " Points: " + points,Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
        }
        else if(victory)
        {
            toast = Toast.makeText(context, context.getText(R.string.victory) + " Points: " + points,Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0,0);
            toast.show();
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
    public int getTime(){return time;}
    public int setTime(int value){return time = value;}

    public boolean isRunning(){return running;}
    public boolean setRunning(boolean value) {return running = value;}
    public boolean getIsPaused(){return isPaused;}
    public boolean setIsPaused(boolean value) {return isPaused = value;}
    public boolean isBluePickup(){return bluePickup;}

    public boolean getGameOver() {return gameOver;}
    public boolean setGameOver(boolean value) {return gameOver = value;}

    public boolean CoinsInit() { return coinsInit; }
    public boolean setCoinsInit(boolean value) {return coinsInit = value;}
    public boolean EnemiesInit() { return enemiesInit; }
    public boolean setEnemiesInit(boolean value) {return enemiesInit = value;}


    public ArrayList<GoldCoin> getCoins(){return coins;}
    public ArrayList<Enemy> getEnemies(){return enemies;}
    public ArrayList<BlueCoin> getBlueCoins(){return blueCoins;}

    public Bitmap getPacBitmap() {return pacBitmap;}
    public Bitmap getCoinBitMap(){return coinBitMap;}
    public Bitmap getEnemyBitMap(){return enemyBitMap;}
    public Bitmap getBlueCoinBitmap(){return blueBitMap;}


}
