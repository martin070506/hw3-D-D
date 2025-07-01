package Player_Types;

import Actions.MoveAction;
import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import Unit_Logic.UnitVisitor;

import java.util.ArrayList;
import java.util.Random;

public class Warrior extends Player
{
    ///  Fields
    private int abilityCooldown;
    private int remainingCooldown;
    private int specialAttackRange;



    /// Constructors
    public Warrior(String name)
    {
        this(name, getWarriorStat(name));
    }

    private Warrior(String name, int[] stat){
        super(name, stat[0], stat[1], stat[2]);
        abilityCooldown = stat[3];
        specialAttackRange = 3;
        remainingCooldown = 0;
    }



    /// Methods
    // Getters
    public int getAbilityCooldown() {
        return abilityCooldown;
    }
    public int getRemainingCooldown() {
        return remainingCooldown;
    }
    public int getSpecialAttackRange() { return specialAttackRange; }

    // Setters
    public void setAbilityCooldown(int abilityCooldown) {
        this.abilityCooldown = abilityCooldown;
    }
    public void setRemainingCooldown(int remainingCooldown) {
        if (remainingCooldown < 0)
            remainingCooldown = 0;
        this.remainingCooldown = remainingCooldown;
    }

    // Abstract Methods
    @Override
    public void accept(UnitVisitor unitVisitor, boolean ability) { unitVisitor.visitWarrior(this); }

    @Override
    public boolean castAbility(Point location){
        if (remainingCooldown > 0)
            return false;

        int level = getLevel();
        remainingCooldown = abilityCooldown;
        ArrayList<GameTile> enemyTileList = getCallback().getTileEnemiesInRange(getLocation(), 3);;

        getCallback().update(getName() + " used Avenger's Shield, healing for " + 10 * getDefense() + ".\n");
        setHealth(getHealth() + 10 * getDefense());
        if (!enemyTileList.isEmpty()) {
            GameTile enemyTile = enemyTileList.get(new Random().nextInt(enemyTileList.size()));
            enemyTile.getUnit().accept(getCallback().playerAttack(this, 'e', enemyTile.getPosition()), true);

        }
        if (level != getLevel())
            MoveAction.handleLevelUpWarrior(this, level, getLevel());


        return true;
    }

    @Override
    public String toString() {
        return super.toString() +
                "    Cooldown: " + getRemainingCooldown() + '/' + getAbilityCooldown();
    }

    @Override
    public int attackAbility() { return (int) (0.1 * getMaxHealth()); }

    // Other Methods
    private static int[] getWarriorStat(String name) {
        return switch (name) {
            case "Jon Snow" -> new int[]{300, 30, 3, 3};
            case "The Hound" -> new int[]{400, 20, 6, 5};

            default -> throw new IllegalArgumentException("Unknown Warrior: " + name);
        };
    }
}

