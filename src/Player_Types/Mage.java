package Player_Types;

import BoardLogic.GameBoard;
import Unit_Logic.UnitVisitor;

public class Mage extends Player   {
    private int attackRange;
    private int manaPool;
    private int currentMana;
    private int manaCost;
    private int maxSpecialAbilityHits;
    private int spellPower;
    public Mage(String name, int maxHealth, int attack, int defense, int manaPool,int manaCost,int spellPower,int maxSpecialAbilityHits,int attackRange)
    {
        super(name,maxHealth,attack,defense);
        this.attackRange=attackRange;
        this.manaPool=manaPool;
        this.currentMana=manaPool/4;
        this.manaCost=manaCost;
        this.maxSpecialAbilityHits=maxSpecialAbilityHits;
        this.spellPower=spellPower;

    }
    @Override
    public void accept(UnitVisitor unitVisitor) { unitVisitor.visitMage(this); }

    public int getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public int getManaPool() {
        return manaPool;
    }

    public void setManaPool(int manaPool) {
        this.manaPool = manaPool;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getMaxSpecialAbilityHits() {
        return maxSpecialAbilityHits;
    }

    public void setMaxSpecialAbilityHits(int maxSpecialAbilityHits) {
        this.maxSpecialAbilityHits = maxSpecialAbilityHits;
    }

    public int getSpellPower() {
        return spellPower;
    }

    public void setSpellPower(int spellPower) {
        this.spellPower = spellPower;
    }
}



