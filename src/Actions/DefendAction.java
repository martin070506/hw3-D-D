package Actions;

import EnemyTypes.Enemy;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Rogue;
import Player_Types.Warrior;
import Unit_Logic.UnitVisitor;

public class DefendAction implements UnitVisitor {
    @Override
    public void visitWarrior(Warrior warrior) {

    }

    @Override
    public void visitMage(Mage mage) {

    }

    @Override
    public void visitRogue(Rogue rogue) {

    }

    @Override
    public void visitMonster(Monster monster) {

    }

    @Override
    public void visitTrap(Trap trap) {

    }
}
