package Actions;

import BoardLogic.GameBoard;
import EnemyTypes.Enemy;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Player;
import Player_Types.Rogue;
import Player_Types.Warrior;
import Unit_Logic.ExpirienceExtractor;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;

import java.util.Random;

public class AttackAction implements UnitVisitor {

    private Player attacker;
    private GameBoard gameBoard;

    public AttackAction(Player attacker, GameBoard gameBoard)
    {
        this.attacker=attacker;
        this.gameBoard=gameBoard;
    }

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
        int attack=rollAttackDefense(attacker.getAttack());
        int defense=rollAttackDefense(monster.getDefense());
        if((attack-defense)>0)
        {
            monster.takeDamage(attack-defense);
            if(monster.getHealth()==0)
            {
                handleDeathOfEnemy(monster);
            }
        }
    }


    @Override
    public void visitTrap(Trap trap) {

    }

    private int rollAttackDefense(int limit)
    {
        return new Random().nextInt(0,limit+1);
    }
    private void handleDeathOfEnemy(Enemy enemy)
    {

        //TODO handle levelUp if needed
        //TODO handle tile clearence if enemy died
    }
}
