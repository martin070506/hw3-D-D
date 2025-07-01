package BoardLogic;


import EnemyTypes.Trap;
import Unit_Logic.Unit;

import java.util.Set;

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


    // Other Methods
    public double range(Point p) { return position.distance(p); }

    public boolean isUnit() { return unit != null; }

    @Override
    public String toString() {

        if (Set.of('B', 'Q', 'D').contains(type) && !((Trap) unit).isVisible())
            return ".";
        return type + "";
    }
}
