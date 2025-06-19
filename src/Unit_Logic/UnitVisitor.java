package Unit_Logic;

import EnemyTypes.Enemy;
import Player_Types.Mage;
import Player_Types.Rogue;
import Player_Types.Warrior;

public interface UnitVisitor {
    public void visitMage(Mage mage);
    public void visitRogue(Rogue rogue);
    public void visitWarrior(Warrior warrior);
    public void visitEnemy(Enemy enemy);
}
