package Player_Types;

import BoardLogic.Point;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;

import java.util.ArrayList;

public class Rogue extends Player {
    /// Fields
    private int attackRange;
    private int maxEnergy;
    private int currentEnergy;
    private int abilityCost;



    /// Constructors
    public Rogue(String name)
    {
        this(name, getRogueStat(name));
        this.attackRange = 2;
        this.maxEnergy = 100;
        currentEnergy = maxEnergy;
    }

    private Rogue(String name, int[] stat)
    {
        super(name, stat[0], stat[1], stat[2]);
        abilityCost = stat[3];
    }



    /// Methods
    // Getters
    public int getAttackRange() {
        return attackRange;
    }
    public int getMaxEnergy() {
        return maxEnergy;
    }
    public int getCurrentEnergy() {
        return currentEnergy;
    }
    public int getAbilityCost() {
        return abilityCost;
    }

    // Setters
    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }
    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }
    public void setCurrentEnergy(int currentEnergy) {
        this.currentEnergy = currentEnergy;
    }
    public void setAbilityCost(int abilityCost) {
        this.abilityCost = abilityCost;
    }

    // Override Methods
    @Override
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitRogue(this); }

    @Override
    public boolean castAbility(Point location){
        if (currentEnergy < abilityCost)
            return false;

        int level = getLevel();
        currentEnergy -= abilityCost;
        ArrayList<Unit> enemyList = getCallback().getEnemiesInRange(getLocation(), attackRange);
        for (Unit enemy : enemyList)
            enemy.accept(getCallback().playerAttack(this, 'e'));
        if (level != getLevel());
            // TODO something

        return true;
    }

    // Other Methods
    private static int[] getRogueStat(String name) {
        return switch (name) {
            case "Arya Stark" -> new int[]{150, 40, 2, 20};
            case "Bronn" -> new int[]{250, 35, 3, 50};

            default -> throw new IllegalArgumentException("Unknown Rogue: " + name);
        };
    }


    @Override
    public String toString() {
        return super.toString() + "    Energy: " + getCurrentEnergy() + '/' + getMaxEnergy();
    }
}
