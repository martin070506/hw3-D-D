package BoardLogic;

public class GameTile {

    private char CharIndicator;


    public GameTile(char C)
    {
        this.CharIndicator=C;
    }



    @Override
    public String toString()
    {
        return String.valueOf(this.CharIndicator);
    }

    public char GetTile()
    {
        return this.CharIndicator;
    }
}
