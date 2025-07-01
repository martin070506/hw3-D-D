package Player_Types;

import Actions.MoveAction;
import BoardLogic.GameTile;
import BoardLogic.Point;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;

import java.util.ArrayList;

public class Hunter extends Player {
    /// Fields
    private int range;
    private int arrowsCount;
    private int ticksCount;



    /// Constructors
    public Hunter(String name) {
        this(name, getHunterStat(name));
    }

    private Hunter(String name, int[] stat) {
        super(name, stat[0], stat[1], stat[2]);
        range = stat[3];
        arrowsCount = 10;
        ticksCount = 0;
    }

    /// Methods
    // Getters
    public int getRange() { return range; }
    public int getArrowsCount() { return arrowsCount; }
    public int getTicksCount() { return ticksCount; }

    // Setters
    public void setArrowsCount(int arrowsCount) {
        if (arrowsCount < 0)
            arrowsCount = 0;
        this.arrowsCount = arrowsCount;
    }

    // Override Methods
    @Override
    public void accept(UnitVisitor unitVisitor, boolean ability) { unitVisitor.visitHunter(this); }

    @Override
    public boolean castAbility(Point location){
        if (arrowsCount <= 0)
            return false;

        int level = getLevel();
        setArrowsCount(arrowsCount - 1);
        ArrayList<GameTile> enemyTileList = new ArrayList<>();
        for (int i = 1; i <= range && enemyTileList.isEmpty(); i++)
            enemyTileList = getCallback().getTileEnemiesInRange(getLocation(), i);
        if (enemyTileList.isEmpty()){
            getCallback().update(getName() + " tried to shoot an arrow but there were no enemies in range.\n");
            return false;
        }

        GameTile enemyTile = enemyTileList.getFirst();
        getCallback().update(getName() + " fired an arrow at " + enemyTile.getUnit().getName() + ".\n");

        enemyTile.getUnit().accept(getCallback().playerAttack(this, 'e', enemyTile.getPosition()), true);

        if (level != getLevel())
            MoveAction.handleLevelUpHunter(this, level, getLevel());

        return true;
    }

    @Override
    public String toString() {
        return super.toString() +
                "    Arrors: " + getArrowsCount() +
                "    Range: " + getRange();
    }

    @Override
    public int attackAbility() { return getAttack(); }

    // Other Methods
    private static int[] getHunterStat(String name) {
        return switch (name) {
            case "Ygritte" -> new int[]{220, 30, 2, 6};

            default -> throw new IllegalArgumentException("Unknown Hunter: " + name);
        };
    }

    public void updateTickCount() {
        ticksCount++;
        if (ticksCount == 10) {
            setArrowsCount(arrowsCount + getLevel());
            ticksCount = 0;
        }

    }
}
