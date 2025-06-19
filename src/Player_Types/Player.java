package Player_Types;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;
import jdk.jshell.spi.ExecutionControl;

public abstract class Player extends Unit {

    private Point playerLocation;
    private int experience;
    private int playerLevel;


    public Player(String name, int maxHealth, int attack, int defense)
    {
        super(name,maxHealth,attack,defense);
        this.experience=0;
        this.playerLevel=1;
        // arguments Like Range will be received in the specific PlayerTypes classes e.g:"mage"
    }
    public abstract void accept(UnitVisitor unitVisitor);








    public void setPlayerLocation(Point playerLocation)
    {
        this.playerLocation=playerLocation;
    }
    public Point getPlayerLocation()
    {
        return playerLocation;
    }
    public int getPlayerX()
    {
        return playerLocation.getX();
    }
    public int getPlayerY()
    {
        return playerLocation.getY();
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }



}
