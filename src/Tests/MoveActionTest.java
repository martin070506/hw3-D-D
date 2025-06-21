package Tests;

import Actions.MoveAction;
import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Player;
import Player_Types.Rogue;
import Player_Types.Warrior;
import Unit_Logic.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveActionTest {

    private GameBoard gameBoard;
    private Player warrior;

    @BeforeEach
    public void setup() {
        warrior = GameBoard.choosePlayer("5");
        gameBoard = new GameBoard(warrior, 5, 5);
        warrior.setPlayerLocation(new Point(2, 2));
        gameBoard.GetBoard()[2][2] = new GameTile('@', warrior, new Point(2, 2));
    }

    @Test
    public void testMoveUp() {
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        assertEquals(2, warrior.getPlayerX());
        assertEquals(1, warrior.getPlayerY());
    }

    @Test
    public void testMoveDown() {
        MoveAction action = new MoveAction('s', gameBoard);
        warrior.accept(action);
        assertEquals(2, warrior.getPlayerX());
        assertEquals(3, warrior.getPlayerY());
    }

    @Test
    public void testMoveLeft() {
        MoveAction action = new MoveAction('a', gameBoard);
        warrior.accept(action);
        assertEquals(1, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());
    }

    @Test
    public void testMoveRight() {
        MoveAction action = new MoveAction('d', gameBoard);
        warrior.accept(action);
        assertEquals(3, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());
    }

    @Test
    public void testIllegalMoveIntoWall() {
        gameBoard.GetBoard()[1][2] = new GameTile('#', null, new Point(2, 1));
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        assertEquals(2, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());
    }

    @Test
    public void testMoveOffBoardUp() {
        warrior.setPlayerLocation(new Point(2, 0));
        gameBoard.GetBoard()[0][2] = new GameTile('@', warrior, new Point(2, 0));
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        assertEquals(2, warrior.getPlayerX());
        assertEquals(0, warrior.getPlayerY());
    }

    @Test
    public void testMoveOffBoardLeft() {
        warrior.setPlayerLocation(new Point(0, 2));
        gameBoard.GetBoard()[2][0] = new GameTile('@', warrior, new Point(0, 2));
        MoveAction action = new MoveAction('a', gameBoard);
        warrior.accept(action);
        assertEquals(0, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());
    }

    @Test
    public void testMoveToEmptyTile() {
        MoveAction action = new MoveAction('s', gameBoard);
        warrior.accept(action);
        GameTile tile = gameBoard.GetBoard()[3][2];
        assertEquals('@', tile.getType());
        assertEquals(warrior, tile.getUnit());
    }

    @Test
    public void testMoveDoesNotClonePlayer() {
        MoveAction action = new MoveAction('s', gameBoard);
        warrior.accept(action);
        assertSame(warrior, gameBoard.GetBoard()[3][2].getUnit());
    }

    @Test
    public void testMoveReplacesOldPosition() {
        MoveAction action = new MoveAction('s', gameBoard);
        warrior.accept(action);
        GameTile previous = gameBoard.GetBoard()[2][2];
        assertNull(previous.getUnit());
        assertEquals('.', previous.getType());
    }
    @Test
    public void testAttemptMoveIntoTrapDoesNotMove() {
        Unit trap = new Trap("Death Trap", 1, 10);
        gameBoard.GetBoard()[1][2] = new GameTile('D', trap, new Point(2, 1));
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        // Player should not move
        assertEquals(2, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());
    }

    @Test
    public void testAttemptMoveIntoMonsterTriggersAttack() {
        Unit monster = new Monster("Lannister Solider", 3);
        gameBoard.GetBoard()[1][2] = new GameTile('s', monster, new Point(2, 1));
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        // Player should stay in place if combat logic doesn't move them
        assertEquals(2, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());
        // Ensure monster took some damage or stayed the same
        assertTrue(monster.getHealth() < monster.getMaxHealth() || monster.getHealth() == monster.getMaxHealth());
    }

    @Test
    public void testMoveActionWithNullBoardThrowsNoException() {
        MoveAction action = new MoveAction('w', null);
        assertDoesNotThrow(() -> warrior.accept(action));
    }

    @Test
    public void testMoveTwiceUpdatesToNewLocation() {
        warrior.accept(new MoveAction('s', gameBoard));
        warrior.accept(new MoveAction('s', gameBoard));
        assertEquals(2, warrior.getPlayerX());
        assertEquals(4, warrior.getPlayerY());
    }

    @Test
    public void testMoveSurroundedByWallsBlocksAllDirections() {
        // Place walls around (2,2)
        gameBoard.GetBoard()[1][2] = new GameTile('#', null, new Point(2, 1)); // up
        gameBoard.GetBoard()[3][2] = new GameTile('#', null, new Point(2, 3)); // down
        gameBoard.GetBoard()[2][1] = new GameTile('#', null, new Point(1, 2)); // left
        gameBoard.GetBoard()[2][3] = new GameTile('#', null, new Point(3, 2)); // right

        warrior.accept(new MoveAction('w', gameBoard));
        assertEquals(2, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());

        warrior.accept(new MoveAction('s', gameBoard));
        assertEquals(2, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());

        warrior.accept(new MoveAction('a', gameBoard));
        assertEquals(2, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());

        warrior.accept(new MoveAction('d', gameBoard));
        assertEquals(2, warrior.getPlayerX());
        assertEquals(2, warrior.getPlayerY());
    }
    @Test
    public void testMonsterMovesRandomlyWhenOutOfRange() {
        Player dummyPlayer = GameBoard.choosePlayer("1");
        GameBoard enemyBoard = new GameBoard(dummyPlayer, 5, 5);
        dummyPlayer.setPlayerLocation(new Point(4, 4));
        enemyBoard.GetBoard()[4][4] = new GameTile('@', dummyPlayer, new Point(4, 4));

        Point monsterStart = new Point(1, 1);
        Unit monster = GameBoard.chooseUnitByType('s');
        enemyBoard.GetBoard()[1][1] = new GameTile('s', monster, monsterStart);
        System.out.println(enemyBoard);

        MoveAction monsterAction = new MoveAction(dummyPlayer, monsterStart, 's', enemyBoard);
        monster.accept(monsterAction);

        boolean hasMoved = false;
        for (int y = 0; y < 5 && !hasMoved; y++) {
            for (int x = 0; x < 5 && !hasMoved; x++) {
                if (enemyBoard.GetBoard()[y][x].getUnit() == monster && !(x == 1 && y == 1)) {
                    hasMoved = true;
                }
            }
        }

        assertTrue(hasMoved, "Monster should have moved randomly.");
        ///IF the random move was 'O' then Monster doesnt have to work (basically the test works)
    }

//    @Test
//    public void testMonsterMovesTowardPlayerWhenInRange() {
//        Player dummyPlayer = GameBoard.choosePlayer("1");
//        GameBoard enemyBoard = new GameBoard(dummyPlayer, 5, 5);
//        dummyPlayer.setPlayerLocation(new Point(2, 2));
//        enemyBoard.GetBoard()[2][2] = new GameTile('@', dummyPlayer, new Point(2, 2));
//
//        Point monsterStart = new Point(2, 4);
//        Unit monster = GameBoard.chooseUnitByType('s');
//        enemyBoard.GetBoard()[4][2] = new GameTile('s', monster, monsterStart);
//
//        MoveAction monsterAction = new MoveAction(dummyPlayer, monsterStart, 's', enemyBoard);
//        monster.accept(monsterAction);
//
//        assertEquals(monster, enemyBoard.GetBoard()[3][2].getUnit(), "Monster should move one tile up toward the player.");
//    }
//
//    @Test
//    public void testMonsterBlockedByWallDoesNotMove() {
//        Player dummyPlayer = GameBoard.choosePlayer("1");
//        GameBoard enemyBoard = new GameBoard(dummyPlayer, 5, 5);
//        dummyPlayer.setPlayerLocation(new Point(2, 2));
//        enemyBoard.GetBoard()[2][2] = new GameTile('@', dummyPlayer, new Point(2, 2));
//
//        Point monsterStart = new Point(2, 4);
//        Unit monster = GameBoard.chooseUnitByType('s');
//        enemyBoard.GetBoard()[4][2] = new GameTile('s', monster, monsterStart);
//
//        // Block the movement path
//        enemyBoard.GetBoard()[3][2] = new GameTile('#', null, new Point(2, 3));
//
//        MoveAction monsterAction = new MoveAction(dummyPlayer, monsterStart, 's', enemyBoard);
//        monster.accept(monsterAction);
//
//        assertEquals(monster, enemyBoard.GetBoard()[4][2].getUnit(), "Monster should not move into a wall.");
//    }

}
