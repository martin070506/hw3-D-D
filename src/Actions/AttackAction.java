package Actions;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Boss;
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
    private Point location;
    private final UserInterfaceCallback callback;


    public AttackAction(Player attacker, GameBoard gameBoard, char directionKey)
    {
        this.attacker = attacker;
        this.gameBoard = gameBoard;
        this.directionKey = directionKey;
        this.callback = gameBoard.getCallback();
    }

    public AttackAction(Enemy attacker, Point location, UserInterfaceCallback callback)
    {
        enemyAttacker = attacker;
        this.location = location;
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
    public void visitMonster(Monster monster, Point location, boolean ability) { visitEnemy(monster, location, ability); }

    @Override
    public void visitTrap(Trap trap, Point location, boolean ability) { visitEnemy(trap, location, ability); }

    @Override
    public void visitBoss(Boss boss, Point location, boolean ability) { visitEnemy(boss, location, ability); }

    private void visitPlayer(Player player){
        engaged(enemyAttacker, player);
        int attack = rollAttack(enemyAttacker.getName(), enemyAttacker.getAttack());
        int defense = rollDefense(player.getName(), player.getDefense());
        dealDamage(enemyAttacker, player, attack - defense, false);
        // Death of player handled in setHealth in Player
    }

    private void visitEnemy(Enemy enemy, Point location, boolean ability){
        int attack = 0;
        int defense = 0;
        if (!ability) {
            engaged(attacker, enemy);
            attack = rollAttack(attacker.getName(), attacker.getAttack());
            defense = rollDefense(enemy.getName(), enemy.getDefense());
        }
        else {
            attack = attacker.attackAbility();
            defense = rollDefense(enemy.getName(), enemy.getDefense());
        }
        dealDamage(attacker, enemy, attack - defense, ability);
        if (enemy.getHealth() == 0) {
            callback.update(enemy.getName() + " died. " + attacker.getName() + " gained " +
                    enemy.getXP() + " experience.\n");
            handleDeathOfEnemy(enemy, location);
        }
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

    private void dealDamage(Unit attacker, Unit defender, int damage, boolean ability) {
        if (damage < 0)
            damage = 0;
        if (ability)
            callback.update(attacker.getName() + " hit " + defender.getName() + " for " + damage + " ability damage.\n");
        else
            callback.update(attacker.getName() + " dealt " + damage + " damage to " + defender.getName() + ".\n");
        defender.takeDamage(damage);
    }

    private void handleDeathOfEnemy(Enemy enemy, Point location)
    {
        handleXP(enemy);
        handleTileClearance(location);
        gameBoard.setEnemyCount(gameBoard.getEnemyCount()-1);
    }

    private void handleXP(Enemy enemy) {

        attacker.setExperience(attacker.getExperience() + enemy.getXP());
        while (attacker.getExperience() >= (50 * attacker.getLevel())) {
            attacker.setExperience(attacker.getExperience() - 50 * attacker.getLevel());
            attacker.setLevel(attacker.getLevel() + 1);
            attacker.setMaxHealth(attacker.getMaxHealth() + 10 * attacker.getLevel());
            attacker.setHealth(attacker.getMaxHealth());
            attacker.setAttack(attacker.getAttack() + (4 * attacker.getLevel()));
            attacker.setDefense(attacker.getDefense() + (attacker.getLevel()));
        }
    }

    private void handleTileClearance(Point location)
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
                boardMatrix[y][x] = new GameTile('.', null, new Point(x,y));
                boardMatrix[y + 1][x] = new GameTile('@', attacker, new Point(x, y + 1));
                attacker.setLocation(new Point(x,y + 1));
                break;
            }
            case 'd':
            {
                boardMatrix[y][x] = new GameTile('.', null, new Point(x,y));
                boardMatrix[y][x + 1] = new GameTile('@', attacker, new Point(x + 1, y));
                attacker.setLocation(new Point(x + 1, y));
                break;
            }
            case 'e':
            {
                boardMatrix[location.getY()][location.getX()] =
                        new GameTile('.', null, location);
                break;
            }
            default:
                break;
        }
    }
}
