package org.example.pacman;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin
{
    private int coinx,coiny;
    private boolean isTaken = false;
    private final int value = 10;

    public GoldCoin(int coinx, int coiny)
{
    this.coinx = coinx;
    this.coiny = coiny;
}

    public int getCoinx()
    {
        return coinx;
    }
    public int getCoiny()
    {
        return coiny;
    }

    public boolean isTaken()
    {
        return isTaken;
    }
    public boolean SetIsTaken(boolean value)
    {
        return isTaken = value;
    }

    public int getValue()
    {
        return value;
    }
}
