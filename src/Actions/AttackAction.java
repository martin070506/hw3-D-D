package Actions;

import EnemyTypes.Enemy;
import Player_Types.Mage;
import Player_Types.Rogue;
import Player_Types.Warrior;
import Unit_Logic.UnitVisitor;

public class AttackAction implements UnitVisitor {
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
    public void visitEnemy(Enemy enemy) {

    }
}
