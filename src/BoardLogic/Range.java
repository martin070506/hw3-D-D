package BoardLogic;

public class Range {

    private int distance;


    public Range(GameTile Tile1,GameTile Tile2)
    {
        this.distance=CalculateDistanceBetweenTiles(Tile1,Tile2);
    }

    private int CalculateDistanceBetweenTiles(GameTile Tile1,GameTile Tile2)
    {
        int XDist=(int)(Math.pow(Tile1.GetX()-Tile2.GetX(),2));
        int YDist=(int)(Math.pow(Tile1.GetY()-Tile2.GetY(),2));
        return Math.toIntExact((long) Math.floor(Math.sqrt(XDist+YDist)));
    }
    public int GetRange()
    {
        return this.distance;
    }
}
