package Actions;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Enemy;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Player;
import Player_Types.Rogue;
import Player_Types.Warrior;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;

import java.util.Random;

public class AttackAction implements UnitVisitor {

    private Player attacker;
    private GameBoard gameBoard;
    private Unit enemyAttacker;
    private char directionKey;

    public AttackAction(Player attacker, GameBoard gameBoard,char directionKey)
    {
        this.attacker=attacker;
        this.gameBoard=gameBoard;
        this.directionKey=directionKey;
    }
    public AttackAction (Enemy attacker)
    {
        this.enemyAttacker = attacker;
    }

    @Override
    public void visitWarrior(Warrior warrior) {
        visitPlayer(warrior);
    }

    @Override
    public void visitMage(Mage mage) {
        visitPlayer(mage);
    }

    @Override
    public void visitRogue(Rogue rogue) {
        visitPlayer(rogue);
    }

    @Override
    public void visitMonster(Monster monster) {
        int attack = rollAttackDefense(attacker.getAttack());
        int defense = rollAttackDefense(monster.getDefense());

        monster.takeDamage(attack - defense);
        if (monster.getHealth() <= 0)
            handleDeathOfEnemy(monster);
    }


    @Override
    public void visitTrap(Trap trap) {
        int attack = rollAttackDefense(attacker.getAttack());
        int defense = rollAttackDefense(trap.getDefense());

        trap.takeDamage(attack - defense);
        if (trap.getHealth() == 0)
            handleDeathOfEnemy(trap);
    }

    private void visitPlayer(Player player){
        int attack = rollAttackDefense(enemyAttacker.getAttack());
        int defense = rollAttackDefense(player.getDefense());
        player.takeDamage(attack - defense);
        // if (player.getHealth() == 0) handleDeathOfPlayer(player); TODO ?
    }

    private int rollAttackDefense(int limit)
    {
        return new Random().nextInt(0,limit+1);
    }
    private void handleDeathOfEnemy(Enemy enemy)
    {

        handleXP(enemy);
        handleTileClearance(enemy);
        gameBoard.setEnemyCount(gameBoard.getEnemyCount()-1);
    }

    private void handleXP(Enemy enemy) {

        attacker.setExperience(attacker.getExperience() + enemy.getXP());
        if (this.attacker.getExperience() >= (50 * this.attacker.getLevel())) {

            this.attacker.setExperience(attacker.getExperience() - 50 * attacker.getLevel());
            this.attacker.setLevel(attacker.getLevel() + 1);
            this.attacker.setMaxHealth(attacker.getMaxHealth() + 10 * attacker.getLevel());
            this.attacker.setHealth(attacker.getMaxHealth());
            this.attacker.setAttack(attacker.getAttack() + (4 * attacker.getLevel()));
            this.attacker.setDefense(attacker.getDefense() + (attacker.getLevel()));

        }
    }
    private void handleTileClearance(Enemy enemy)
    {
        GameTile[][] boardMatrix=gameBoard.getBoard();
        int x=attacker.getLocation().getX();
        int y=attacker.getLocation().getY();

        switch (directionKey)
        {
            case 'w':
            {
                boardMatrix[y][x]=new GameTile('.',null,new Point(x,y));
                boardMatrix[y-1][x]=new GameTile('@',attacker,new Point(x,y-1));
                attacker.setLocation(new Point(x,y-1));
                break;
            }
            case 'a':
            {
                boardMatrix[y][x]=new GameTile('.',null,new Point(x,y));
                boardMatrix[y][x-1]=new GameTile('@',attacker,new Point(x-1,y));
                attacker.setLocation(new Point(x-1,y));
                break;
            }
            case 's':
            {
                boardMatrix[y][x]=new GameTile('.',null,new Point(x,y));
                boardMatrix[y+1][x]=new GameTile('@',attacker,new Point(x,y+1));
                attacker.setLocation(new Point(x,y+1));
                break;
            }
            case 'd':
            {
                boardMatrix[y][x]=new GameTile('.',null,new Point(x,y));
                boardMatrix[y][x+1]=new GameTile('@',attacker,new Point(x+1,y));
                attacker.setLocation(new Point(x+1,y));
                break;
            }
            default:
            {
                break;
            }

        }
    }
}
