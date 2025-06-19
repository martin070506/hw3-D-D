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
}
