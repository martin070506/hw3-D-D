package Player_Types;

import BoardLogic.GameBoard;
import Unit_Logic.UnitVisitor;

public class Rogue extends Player {

    private int attackRange;
    private int maxEnergy;
    private int currentEnergy;
    private int abilityCost;
    public Rogue(String name, int maxHealth, int attack, int defense,int abilityCost)
    {
        super(name,maxHealth,attack,defense);
        this.attackRange=2;
        this.maxEnergy=100;
        this.currentEnergy=maxEnergy;
        this.abilityCost=abilityCost;
    }
    @Override
    public void accept(UnitVisitor unitVisitor) {
        unitVisitor.visitRogue(this);
    }

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
}
