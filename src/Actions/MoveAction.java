package Actions;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Boss;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Player;
import Player_Types.Rogue;
import Player_Types.Warrior;
import UI.UserInterfaceCallback;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;

import java.util.Set;

public class MoveAction implements UnitVisitor {
    private char directionKey;
    private final GameBoard originalGameBoard;
    private GameBoard newGameBoard;
    private Point enemyLocation;
    private Player player;
    private char enemyType;
    private final UserInterfaceCallback callback;

    public MoveAction(char directionKey, GameBoard board)
    {
        this.directionKey = directionKey;
        originalGameBoard = board;
        callback = board.getCallback();
        //this builder will be called when a player moves
    }
    public MoveAction(Player player, Point enemyLocation, char enemyType,
                      GameBoard originalGameBoard, GameBoard newGameBoard)
    {
        this.enemyType = enemyType;
        this.player = player;
        this.enemyLocation = enemyLocation;
        this.originalGameBoard = originalGameBoard;
        this.newGameBoard = newGameBoard;
        this.callback = originalGameBoard.getCallback();
        //this builder will be called when an enemy Moves, so the game can calculate where the enemy should go
    }
    @Override
    public void visitWarrior(Warrior warrior) {
        int initialLevel = warrior.getLevel();
        visitPlayer(warrior);
        warrior.setRemainingCooldown(warrior.getRemainingCooldown() - 1);
        int newLevel = warrior.getLevel();
        if (newLevel > initialLevel)
            HandleLevelUpWarrior(warrior);
    }

    @Override
    public void visitMage(Mage mage) {
        int initialLevel = mage.getLevel();
        visitPlayer(mage);
        int newLevel = mage.getLevel();
        if (newLevel > initialLevel)
            HandleLevelUpMage(mage);
    }

    @Override
    public void visitRogue(Rogue rogue) {
        int initialLevel = rogue.getLevel();
        visitPlayer(rogue);
        int newLevel = rogue.getLevel();
        if (newLevel > initialLevel)
            HandleRogueLevelUp(rogue);
    }

    @Override
    public void visitMonster(Monster monster) {

        if (!playerInRange(monster)) {
            moveRandom(monster);
            return;
        }

        int distanceX = player.getLocation().getX() - enemyLocation.getX();
        int distanceY = player.getLocation().getY() - enemyLocation.getY();
        int moveX = 0;
        int moveY = 0;

        if (Math.abs(distanceX) > Math.abs(distanceY)) {
            if (distanceX > 0) moveX = 1;
            else moveX = -1;
        }
        else {
            if (distanceY > 0) moveY = 1;
            else moveY = -1;
        }

        Point destination = new Point(enemyLocation.getX() + moveX, enemyLocation.getY() + moveY);
        if (isLegalMonsterMove(destination))
            moveMonster(monster, destination);
    }

    @Override
    public void visitTrap(Trap trap) {
        trap.increaseTick();

        if (player.getLocation().distance(enemyLocation) < 2.0)
            player.accept(new AttackAction(trap, callback));
    }

    @Override
    public void visitBoss(Boss boss, boolean ability) {
        if (!playerInRange(boss))
            return;
        // TODO change - not ready
        if (ability)
            boss.castAbility();
        player.accept(new AttackAction(boss, callback));
    }

    private void visitPlayer(Player player)
    {
        int originalX = player.getLocation().getX();
        int originalY = player.getLocation().getY();
        if (originalGameBoard == null) { return; }
        GameTile[][] boardMatrix = originalGameBoard.getBoard();
        /*
         * Creating different 2 IF Blocks to show diffrence between enemy step, and normal step
         * doing this instead of putting an if block in each switchCase
         * */
        if (originalGameBoard.isLegalMove(directionKey) &&
                !originalGameBoard.isLegalMoveAndUnitThere(directionKey))
        {
            boardMatrix[originalY][originalX] = new GameTile('.', null,
                    new Point(originalX, originalY));
            switch (directionKey) {
                case 'w':
                    player.setLocation(new Point(originalX, originalY - 1));
                    boardMatrix[originalY - 1][originalX] = new GameTile('@', player,
                            new Point(originalX, originalY - 1));
                    break;
                case 'a':
                    player.setLocation(new Point(originalX - 1, originalY));
                    boardMatrix[originalY][originalX - 1] = new GameTile('@', player,
                            new Point(originalX - 1, originalY));
                    break;
                case 's':
                    player.setLocation(new Point(originalX, originalY + 1));
                    boardMatrix[originalY + 1][originalX] = new GameTile('@', player,
                            new Point(originalX, originalY + 1));
                    break;
                case 'd':
                    player.setLocation(new Point(originalX + 1,originalY));
                    boardMatrix[originalY][originalX + 1] = new GameTile('@', player,
                            new Point(originalX + 1, originalY));
                    break;
            }
        }

        if (originalGameBoard.isLegalMoveAndUnitThere(directionKey))
        {
            Unit defender;
            switch (directionKey)
            {
                case 'w':
                    defender= boardMatrix[originalY - 1][originalX].getUnit();
                    defender.accept(new AttackAction(player, originalGameBoard, 'w'));
                    break;
                case 'a':
                    defender = boardMatrix[originalY][originalX - 1].getUnit();
                    defender.accept(new AttackAction(player, originalGameBoard, 'a'));
                    break;
                case 's':
                    defender = boardMatrix[originalY + 1][originalX].getUnit();
                    defender.accept(new AttackAction(player, originalGameBoard, 's'));
                    break;
                case 'd':
                    defender = boardMatrix[originalY][originalX + 1].getUnit();
                    defender.accept(new AttackAction(player, originalGameBoard, 'd'));
                    break;
            }
        }
    }

    private void HandleLevelUpWarrior(Warrior warrior) {
        warrior.setRemainingCooldown(0);
        warrior.setMaxHealth(warrior.getMaxHealth() + (5 * warrior.getLevel()));
        warrior.setHealth(warrior.getMaxHealth());
        warrior.setAttack(warrior.getAttack() + (2 * warrior.getAttack()));
        warrior.setDefense(warrior.getDefense() + (2 * warrior.getDefense()));
    }

    private void HandleLevelUpMage(Mage mage)
    {
        mage.setManaPool(mage.getManaPool() + (25 * mage.getLevel()));
        mage.setCurrentMana(Math.min((int)(mage.getCurrentMana() + (mage.getManaPool() / 4)),
                mage.getManaPool()));
    }

    private void HandleRogueLevelUp(Rogue rogue)
    {
        rogue.setCurrentEnergy(100);
        rogue.setAttack(rogue.getAttack() + (3 * rogue.getLevel()));
    }

    private boolean playerInRange(Monster monster)  { return softRange(monster) && hardRange(monster); }

    private boolean softRange(Monster monster) {
        if (Math.abs(player.getLocation().getX() - enemyLocation.getX()) > monster.getVisionRange())
            return false;
        if (Math.abs(player.getLocation().getY() - enemyLocation.getY()) > monster.getVisionRange())
            return false;

        return true;
    }

    private boolean hardRange(Monster monster) {
        return player.getLocation().distance(enemyLocation) < monster.getVisionRange();
    }

    private void moveRandom(Monster monster) {
        int moveX = 0;
        int moveY = 0;
        switch ((int) (Math.random() * 4)) {
            case 0: // Move left
                if (isLegalMonsterMove(new Point(enemyLocation.getX() - 1, enemyLocation.getY()))) moveX = -1;
                break;
            case 1: // Move right
                if (isLegalMonsterMove(new Point(enemyLocation.getX() + 1, enemyLocation.getY()))) moveX = 1;
                break;
            case 2: // Move up
                if (isLegalMonsterMove(new Point(enemyLocation.getX(), enemyLocation.getY() - 1))) moveY = -1;
                break;
            case 3: // Move down
                if (isLegalMonsterMove(new Point(enemyLocation.getX(), enemyLocation.getY() + 1))) moveY = 1;
                break;
        }

        if (moveX + moveY != 0)
            moveMonster(monster, new Point(enemyLocation.getX() + moveX, enemyLocation.getY() + moveY));
    }

    private void moveMonster(Monster monster, Point destination) {
        if (player.getLocation().equals(destination)) {
            player.accept(new AttackAction(monster, callback));
            return;
        }

        newGameBoard.getBoard()[destination.getY()][destination.getX()] = new GameTile(enemyType, monster,
                new Point(destination.getX(), destination.getY()));
        newGameBoard.getBoard()[enemyLocation.getY()][enemyLocation.getX()] = new GameTile('.', null,
                new Point(enemyLocation.getX(), enemyLocation.getY()));
    }

    private boolean isLegalMonsterMove(Point monsterLocation)
    {
        if (monsterLocation.getX() >= originalGameBoard.getWidth() ||
                monsterLocation.getX() < 0 ||
                monsterLocation.getY() >= originalGameBoard.getHeight() ||
                monsterLocation.getY() < 0)
            return false;

        if (!Set.of('.','@').contains(originalGameBoard.getBoard()[monsterLocation.getY()][monsterLocation.getX()].getType()) &&
                !Set.of('.','@').contains(newGameBoard.getBoard()[monsterLocation.getY()][monsterLocation.getX()].getType()))
            return false;

        return true;
    }
}
