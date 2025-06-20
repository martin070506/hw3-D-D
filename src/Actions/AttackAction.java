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
import Unit_Logic.ExpirienceExtractor;
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
    public AttackAction (Unit enemyAttacker,GameBoard gameBoard)
    {
        this.enemyAttacker=attacker;
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

        handleXP(enemy);
        handleTileClearance(enemy);
        gameBoard.setEnemyCount(gameBoard.getEnemyCount()-1);
    }
    private void handleXP(Enemy enemy)
    {
        this.attacker.setExperience(this.attacker.getExperience()+enemy.getXP());
        if(this.attacker.getExperience()>=(50*this.attacker.getPlayerLevel()))
        {
            this.attacker.setExperience(attacker.getExperience()-50*attacker.getPlayerLevel());
            this.attacker.setPlayerLevel(attacker.getPlayerLevel()+1);
            this.attacker.setMaxHealth(attacker.getMaxHealth()+10*attacker.getPlayerLevel());
            this.attacker.setHealth(attacker.getMaxHealth());
            this.attacker.setAttack(attacker.getAttack()+(4*attacker.getPlayerLevel()));
            this.attacker.setDefense(attacker.getDefense()+(attacker.getPlayerLevel()));

        }
    }
    private void handleTileClearance(Enemy enemy)
    {
        GameTile[][] boardMatrix=gameBoard.GetBoard();
        int x= attacker.getPlayerX();
        int y=attacker.getPlayerY();
        boardMatrix[y][x]=new GameTile('.',null,new Point(x,y));
        switch (directionKey)
        {
            case 'w':
            {
                boardMatrix[y-1][x]=new GameTile('@',attacker,new Point(x,y-1));
                attacker.setPlayerLocation(new Point(x,y-1));
                break;
            }
            case 'a':
            {
                boardMatrix[y][x-1]=new GameTile('@',attacker,new Point(x-1,y));
                attacker.setPlayerLocation(new Point(x-1,y));
                break;
            }
            case 's':
            {
                boardMatrix[y+1][x]=new GameTile('@',attacker,new Point(x,y+1));
                attacker.setPlayerLocation(new Point(x,y+1));
                break;
            }
            case 'd':
            {
                boardMatrix[y][x+1]=new GameTile('@',attacker,new Point(x+1,y));
                attacker.setPlayerLocation(new Point(x+1,y));
                break;
            }

        }
    }
}
