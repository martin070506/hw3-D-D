package Player_Types;

import BoardLogic.GameBoard;
import Unit_Logic.UnitVisitor;

public class Rogue extends Player {

    private int attackRange;
    private int maxEnergy;
    private int currentEnergy;
    private int abilityCost;
    public Rogue(String name, int maxHealth, int attack, int defense,int abilityCost,GameBoard board)
    {
        super(name,maxHealth,attack,defense,board);
        this.attackRange=2;
        this.maxEnergy=100;
        this.currentEnergy=maxEnergy;
        this.abilityCost=abilityCost;
    }
    @Override
    public void accept(UnitVisitor unitVisitor) {
        unitVisitor.visitRogue(this);
    }
}
