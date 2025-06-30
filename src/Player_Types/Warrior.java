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
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitWarrior(this); }

    // Other Methods
    @Override
    public String toString() {
        return super.toString() +
                "    Cooldown: " + getRemainingCooldown() + '/' + getAbilityCooldown();
    }
}
