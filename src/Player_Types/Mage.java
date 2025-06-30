package Player_Types;

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
    public Mage(String name, int maxHealth, int attack, int defense, int attackRange, int manaPool,
                int manaCost, int maxSpecialAbilityHits, int spellPower)
    {
        super(name, maxHealth, attack, defense);
        this.attackRange = attackRange;
        this.manaPool = manaPool;
        this.currentMana = manaPool / 4;
        this.manaCost = manaCost;
        this.maxSpecialAbilityHits = maxSpecialAbilityHits;
        this.spellPower = spellPower;
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
    @Override
    public String toString() {
        return super.toString() +
                "    Mana: " + getCurrentMana() + '/' + getManaPool() +
                "    Spell Power: " + getSpellPower();
    }
}



