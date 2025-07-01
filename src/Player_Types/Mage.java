package Player_Types;

import BoardLogic.GameBoard;
import Unit_Logic.UnitVisitor;

public class Mage extends Player   {
    /// Fields
    private int attackRange;
    private int manaPool;
    private int currentMana;
    private int manaCost;
    private int maxSpecialAbilityHits;
    private int spellPower;



    /// Constructors
    public Mage(String name)
    {
        this(name, getMageStat(name));
    }

    private Mage(String name, int[] stat){
        super(name, stat[0], stat[1], stat[2]);
        attackRange = stat[3];
        manaPool = stat[4];
        currentMana = manaPool / 4;
        manaCost = stat[5];
        maxSpecialAbilityHits = stat[6];
        spellPower = stat[7];
    }



    /// Methods
    // Getters
    public int getAttackRange() { return attackRange; }
    public int getManaPool() { return manaPool; }
    public int getCurrentMana() { return currentMana; }
    public int getManaCost() { return manaCost; }
    public int getMaxSpecialAbilityHits() {
        return maxSpecialAbilityHits;
    }
    public int getSpellPower() {
        return spellPower;
    }

    // Setters
    public void setAttackRange(int attackRange) { this.attackRange = attackRange; }
    public void setManaPool(int manaPool) {
        this.manaPool = manaPool;
    }
    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }
    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }
    public void setMaxSpecialAbilityHits(int maxSpecialAbilityHits) { this.maxSpecialAbilityHits = maxSpecialAbilityHits; }
    public void setSpellPower(int spellPower) {
        this.spellPower = spellPower;
    }

    // Abstract Methods
    @Override
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitMage(this); }

    // Other Methods
    private static int[] getMageStat(String name) {
        return switch (name) {
            case "Melisandre" -> new int[]{100, 5, 1, 6, 300, 30, 5, 15};
            case "Thoros of Myr" -> new int[]{250, 25, 4, 4, 150, 20, 3, 20};

            default -> throw new IllegalArgumentException("Unknown Mage: " + name);
        };
    }

    @Override
    public String toString() {
        return super.toString() +
                "    Mana: " + getCurrentMana() + '/' + getManaPool() +
                "    Spell Power: " + getSpellPower();
    }
}



