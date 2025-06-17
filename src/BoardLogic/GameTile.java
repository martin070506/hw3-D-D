package BoardLogic;


import Unit_Logic.Unit;

public class GameTile {

    private char type;
    private Point position;
    private boolean isUnit;
    private Unit unit;


    public GameTile(char type, int x, int y) {

        this.type = type;
        this.position = new Point(x, y);
        this.isUnit=false;
        this.unit=null;
    }
    public GameTile(char type,Unit unit,int x,int y)
    {
        this.position=new Point(x,y);
        this.isUnit=true;
        this.type=type;
    }



    public char getType() { return type; }
    public Point getPosition() { return position; }

    public void setPosition(Integer x, Integer y) {

        if (x != null)
            position.setX(x);
        if (y != null)
            position.setY(y);
    }

    public void setType(char type) { this.type = type; }

    public double range(Point p) { return position.distance(p); }

    @Override
    public String toString() { return type + " " + position.toString(); }

}
