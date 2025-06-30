package Player_Types;

import BoardLogic.GameBoardCallback;
import BoardLogic.Point;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;

public abstract class Player extends Unit {
    /// Fields
    private Point location; // TODO not being used, and has getter
    private int experience;
    private int level;
    private GameBoardCallback callback;



    /// Constructors
    public Player(String name, int maxHealth, int attack, int defense)
    {
        super(name, maxHealth, attack, defense);
        experience = 0;
        level = 1;
        // arguments Like Range will be received in the specific PlayerTypes classes e.g:"mage"
    }



    /// Methods
    // Getters
    public Point getLocation() { return location; }
    public int getExperience() { return experience; }
    public int getLevel() { return level; }

    // Setters
    public void setLocation(Point location) { this.location = location; }
    public void setExperience(int experience) { this.experience = experience; }
    public void setLevel(int playerLevel) { this.level = playerLevel; }


    // Abstract Methods
    public abstract void accept(UnitVisitor unitVisitor);

    // Other Methods
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (getHealth() <= 0)
            callback.endGame();
    }

    @Override
    public String toString() {
        return super.toString() +
                "    Level: " + getLevel() +
                "    Experience: " + getExperience() + '/' + 50 * getLevel();
    }
}
