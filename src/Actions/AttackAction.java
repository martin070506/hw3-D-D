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
import UI.UserInterfaceCallback;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;

import java.util.Random;

public class AttackAction implements UnitVisitor {

    private Player attacker;
    private GameBoard gameBoard;
    private Unit enemyAttacker;
    private char directionKey;
    private final UserInterfaceCallback callback;


    public AttackAction(Player attacker, GameBoard gameBoard, char directionKey)
    {
        this.attacker = attacker;
        this.gameBoard = gameBoard;
        this.directionKey = directionKey;
        this.callback = gameBoard.getCallback();
    }

    public AttackAction (Enemy attacker, UserInterfaceCallback callback)
    {
        enemyAttacker = attacker;
        this.callback = callback;
    }

    @Override
    public void visitWarrior(Warrior warrior) { visitPlayer(warrior); }

    @Override
    public void visitMage(Mage mage) { visitPlayer(mage); }

    @Override
    public void visitRogue(Rogue rogue) {
        visitPlayer(rogue);
    }

    @Override
    public void visitMonster(Monster monster) { visitEnemy(monster); }

    @Override
    public void visitTrap(Trap trap) { visitEnemy(trap); }

    private void visitPlayer(Player player){
        engaged(enemyAttacker, player);
        int attack = rollAttack(enemyAttacker.getName(), enemyAttacker.getAttack());
        int defense = rollDefense(player.getName(), player.getDefense());
        dealDamage(enemyAttacker, player, attack - defense);
        // Death of player handled in setHealth in Player
    }

    private void visitEnemy(Enemy enemy){
        engaged(attacker, enemy);
        int attack = rollAttack(attacker.getName(), attacker.getAttack());
        int defense = rollDefense(enemy.getName(), enemy.getDefense());
        dealDamage(attacker, enemy, attack - defense);
        if (enemy.getHealth() == 0) // The setHealth take care in negative values
            handleDeathOfEnemy(enemy);
    }

    private void engaged(Unit attacker, Unit defender){
        callback.update(attacker.getName() + " engaged in combat with " + defender.getName() + ".\n");
        callback.update(attacker + "\n");
        callback.update(defender + "\n");
    }

    private int rollAttack(String name, int limit)
    {
        int points = new Random().nextInt(0,limit+1);
        callback.update(name + " rolled " + points + " attack points.\n");
        return points;
    }

    private int rollDefense(String name, int limit)
    {
        int points = new Random().nextInt(0,limit+1);
        callback.update(name + " rolled " + points + " Defense points.\n");
        return points;
    }

    private void dealDamage(Unit attacker, Unit defender, int damage) {
        if (damage < 0)
            damage = 0;
        callback.update(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + ".\n");
        defender.takeDamage(damage);
    }

    private void handleDeathOfEnemy(Enemy enemy)
    {
        handleXP(enemy);
        handleTileClearance(enemy);
        gameBoard.setEnemyCount(gameBoard.getEnemyCount()-1);
    }

    private void handleXP(Enemy enemy) {

        attacker.setExperience(attacker.getExperience() + enemy.getXP());
        if (attacker.getExperience() >= (50 * attacker.getLevel())) {
            attacker.setExperience(attacker.getExperience() - 50 * attacker.getLevel());
            attacker.setLevel(attacker.getLevel() + 1);
            attacker.setMaxHealth(attacker.getMaxHealth() + 10 * attacker.getLevel());
            attacker.setHealth(attacker.getMaxHealth());
            attacker.setAttack(attacker.getAttack() + (4 * attacker.getLevel()));
            attacker.setDefense(attacker.getDefense() + (attacker.getLevel()));
        }
    }
    private void handleTileClearance(Enemy enemy)
    {
        GameTile[][] boardMatrix = gameBoard.getBoard();
        int x = attacker.getLocation().getX();
        int y = attacker.getLocation().getY();

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
                break;
        }
    }
}
