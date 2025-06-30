package Player_Types;

import BoardLogic.GameBoard;
import Unit_Logic.UnitVisitor;

public class Warrior extends Player
{
    ///  Fields
    private int abilityCooldown;
    private int remainingCooldown;
    private int specialAttackRange;



    /// Constructors
    public Warrior(String name, int maxHealth, int attack, int defense, int abilityCooldown)
    {
        super(name, maxHealth, attack, defense);
        this.abilityCooldown = abilityCooldown;
        this.specialAttackRange = 3;
        // Todo handle remaining cooldown (idea: a tick is in move action, so inside visit warrior i will handle it)
        // Take in consideration that right after the creation of the unit it being printed - so
        // remainingCooldown need to be implemented in the constructor
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
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitWarrior(this); }

    // Other Methods
    @Override
    public String toString() {
        return super.toString() +
                "    Cooldown: " + getRemainingCooldown() + '/' + getAbilityCooldown();
    }
}
