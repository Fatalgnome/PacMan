package org.example.pacman;

public class Enemy
{
    private int enemyX, enemyY;
    private boolean isAlive = true;

    public Enemy(int enemyX, int enemyY)
    {
        this.enemyX = enemyX;
        this.enemyY = enemyY;
    }

    public int getEnemyX(){return enemyX;}
    public int getEnemyY(){return enemyY;}
    public int setEnemyX(int value){return enemyX = value;}
    public int setEnemyY(int value){return enemyY = value;}

    public boolean isAlive(){return isAlive;}
    public boolean SetIsAlive(boolean value){return isAlive = value;}
}
