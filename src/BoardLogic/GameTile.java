package BoardLogic;


import Unit_Logic.Unit;

public class GameTile {
    /// Fields
    private char type;
    private Point position;
    private Unit unit;



    /// Constructors
    public GameTile(char type, Unit unit, Point position) {

        this.type = type;
        this.unit = unit;
        this.position = position;
    }



    ///  Methods
    // Getters
    public char getType() { return type; }
    public Point getPosition() { return position; }
    public Unit getUnit() { return this.unit; }


    // Setters
    public void setType(char type) { this.type = type; }
    public void setPosition(Integer x, Integer y) {
        if (x != null) position.setX(x);
        if (y != null) position.setY(y);
    }

    public void setPosition(Point position) { this.position = position; }
    public void setUnit(Unit unit) { this.unit = unit; }


    // HelpMethods
    public double range(Point p) { return position.distance(p); }


    // toString
    @Override
    public String toString() { return type + ""; }

}
