package Unit_Logic;

import EnemyTypes.Enemy;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Rogue;
import Player_Types.Warrior;
import UI.UserInterfaceCallback;

public interface UnitVisitor {
    void visitMage(Mage mage);
    void visitRogue(Rogue rogue);
    void visitWarrior(Warrior warrior);
    void visitMonster(Monster monster);
    void visitTrap(Trap trap);
}
