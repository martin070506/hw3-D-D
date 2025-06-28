package Player_Types;

import BoardLogic.GameBoard;
import Unit_Logic.UnitVisitor;

public class Warrior extends Player
{
    private int abilityCooldown;
    private int remainingCooldown;
    private int specialAttackRange;

    @Override
    public void accept(UnitVisitor unitVisitor) {
        unitVisitor.visitWarrior(this);
    }
    public Warrior(String name, int maxHealth, int attack, int defense,int abilityCooldown)
    {
        super(name,maxHealth,attack,defense);
        this.abilityCooldown=abilityCooldown;
        this.specialAttackRange=3;

    }
    //Todo handle remaining cooldown (idea: a tick is in move action, so inside visit warrior i will handle it)

    public int getAbilityCooldown() {
        return abilityCooldown;
    }

    public void setAbilityCooldown(int abilityCooldown) {
        this.abilityCooldown = abilityCooldown;
    }

    public int getRemainingCooldown() {
        return remainingCooldown;
    }
    public int getSpecialAttackRange(){return  specialAttackRange;}

    public void setRemainingCooldown(int remainingCooldown) {
        this.remainingCooldown = remainingCooldown;
        if(remainingCooldown<=0) this.remainingCooldown=0;

    }
}
