package Player_Types;

import BoardLogic.GameBoard;
import Unit_Logic.UnitVisitor;

public class Warrior extends Player
{
    private int abilityCooldown;
    private int remainingCooldown;

    @Override
    public void accept(UnitVisitor unitVisitor) {
        unitVisitor.visitWarrior(this);
    }
    public Warrior(String name, int maxHealth, int attack, int defense,int abilityCooldown)
    {
        super(name,maxHealth,attack,defense);
        this.abilityCooldown=abilityCooldown;

    }
    //Todo handle remaining cooldown (idea: a tick is in move action, so inside visit warrior i will handle it)
}
