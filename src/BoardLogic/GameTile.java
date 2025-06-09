package BoardLogic;

public class GameTile {

    private char CharIndicator;
    private int x;
    private int y;



    public GameTile(char C,int x,int y)
    {
        this.CharIndicator=C;
        this.x=x;
        this.y=y;
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
    public int GetX()
    {
        return x;
    }
    public int GetY()
    {
        return  y;
    }

    public int GetRangeFromTile(GameTile tile)
    {
        return new Range(this,tile).GetRange();
    }

}
