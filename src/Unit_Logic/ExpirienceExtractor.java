package Unit_Logic;

import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Rogue;
import Player_Types.Warrior;

public  class ExpirienceExtractor implements UnitVisitor {
    private int xp;
    @Override
    public void visitMonster(Monster monster) {
        this.xp= monster.getXP();
    }

    @Override
    public void visitTrap(Trap trap) {
        this.xp=trap.getXP();
    }

    public int getXp() {
        return xp;
    }

    @Override
    public void visitMage(Mage mage) {

    }

    @Override
    public void visitWarrior(Warrior warrior) {

    }

    @Override
    public void visitRogue(Rogue rogue) {

    }

}
