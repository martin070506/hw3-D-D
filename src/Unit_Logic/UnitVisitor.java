package Unit_Logic;

import BoardLogic.Point;
import EnemyTypes.Boss;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Rogue;
import Player_Types.Warrior;

public interface UnitVisitor {
    void visitMage(Mage mage);
    void visitRogue(Rogue rogue);
    void visitWarrior(Warrior warrior);
    void visitMonster(Monster monster, Point location);
    void visitTrap(Trap trap, Point location);
    void visitBoss(Boss boss, Point location);
}
