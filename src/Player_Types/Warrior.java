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
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitWarrior(this); }

    // Other Methods
    private static int[] getWarriorStat(String name) {
        return switch (name) {
            case "Jon Snow" -> new int[]{300, 30, 3, 3};
            case "The Hound" -> new int[]{400, 20, 6, 5};

            default -> throw new IllegalArgumentException("Unknown Warrior: " + name);
        };
    }

    @Override
    public String toString() {
        return super.toString() +
                "    Cooldown: " + getRemainingCooldown() + '/' + getAbilityCooldown();
    }
}
