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

    private TextView pointsView;
    private int pacx, pacy;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen
    private int amountofCoins = 10;
    private int coinsLeft;
    private boolean gameOver = false;
    private Random randy = new Random();
    private boolean coinsInit = false;

    public Game(Context context, TextView view)
    {
        this.context = context;
        this.pointsView = view;
        pacBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman);
        coinBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.golden);
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
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        coinsInit = false;
        gameOver = false;
        coinsLeft = amountofCoins;

        for (GoldCoin coin : coins)
        {
            coin.SetIsTaken(true);
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

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void movePacmanRight(int pixels)
    {
        //still within our boundaries?
        if (pacx+pixels+pacBitmap.getWidth()<w) {
            pacx = pacx + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }
    public void movePacmanLeft(int pixels)
    {
        //still within our boundaries?
        if (pacx+pixels > 0) {
            pacx = pacx - pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }
    public void movePacmanUp(int pixels)
    {
        //still within our boundaries?
        if (pacy+pixels > 0) {
            pacy = pacy - pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }
    public void movePacmanDown(int pixels)
    {
        //still within our boundaries?
        if (pacy+pixels+pacBitmap.getHeight()<h) {
            pacy = pacy + pixels;
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    //TODO check if the pacman touches a gold coin
    //and if yes, then update the neccesseary data
    //for the gold coins and the points
    //so you need to go through the arraylist of goldcoins and
    //check each of them for a collision with the pacman
    public void doCollisionCheck()
    {
        for (GoldCoin coin : coins)
        {
            if ((Math.sqrt(((coin.getCoinx() - pacx) * (coin.getCoinx()- pacx))
                    + ((coin.getCoiny() - pacy) * (coin.getCoiny() - pacy))) <= 150
                    && !coin.isTaken()) || Math.sqrt((-(coin.getCoinx() - pacx) * -(coin.getCoinx() - pacx))
                    + (-(coin.getCoiny() - pacy) * -(coin.getCoiny() - pacy))) <= 150
                    && !coin.isTaken() )
            {
                coin.SetIsTaken(true);
                points += coin.getValue();
                pointsView.setText(" " + context.getText(R.string.points) +" "+ points);
                coinsLeft--;
                if (coinsLeft == 0)
                {
                    gameOver = true;
                }
            }
        }
    }

    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getPoints()
    {
        return points;
    }

    public boolean getGameOver() {return gameOver;}

    public boolean CoinsInit() { return coinsInit; }
    public boolean SetCoinsInit(boolean value) {return coinsInit = value;}

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }
    public Bitmap getCoinBitMap(){return coinBitMap;}


}
