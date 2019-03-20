package org.example.pacman;

public class BlueCoin
{
    private int bluex, bluey;
    private boolean isTaken = false;

    public BlueCoin(int bluex, int bluey)
    {
        this.bluex = bluex;
        this.bluey = bluey;
    }

    public int getBluex() {
        return bluex;
    }

    public int getBluey() {
        return bluey;
    }

    public boolean isTaken() {
        return isTaken;
    }
    public boolean SetisTaken(boolean value) {
        return isTaken = value;
    }

}
