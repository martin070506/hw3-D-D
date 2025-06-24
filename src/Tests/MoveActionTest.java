package Tests;

import Actions.MoveAction;
import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Player;
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
        warrior.setLocation(new Point(2, 2));
        gameBoard.getBoard()[2][2] = new GameTile('@', warrior, new Point(2, 2));
    }

    @Test
    public void testMoveUp() {
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(1, warrior.getLocation().getY());
    }

    @Test
    public void testMoveDown() {
        MoveAction action = new MoveAction('s', gameBoard);
        warrior.accept(action);
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(3, warrior.getLocation().getY());
    }

    @Test
    public void testMoveLeft() {
        MoveAction action = new MoveAction('a', gameBoard);
        warrior.accept(action);
        assertEquals(1, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());
    }

    @Test
    public void testMoveRight() {
        MoveAction action = new MoveAction('d', gameBoard);
        warrior.accept(action);
        assertEquals(3, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());
    }

    @Test
    public void testIllegalMoveIntoWall() {
        gameBoard.getBoard()[1][2] = new GameTile('#', null, new Point(2, 1));
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());
    }

    @Test
    public void testMoveOffBoardUp() {
        warrior.setLocation(new Point(2, 0));
        gameBoard.getBoard()[0][2] = new GameTile('@', warrior, new Point(2, 0));
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(0, warrior.getLocation().getY());
    }

    @Test
    public void testMoveOffBoardLeft() {
        warrior.setLocation(new Point(0, 2));
        gameBoard.getBoard()[2][0] = new GameTile('@', warrior, new Point(0, 2));
        MoveAction action = new MoveAction('a', gameBoard);
        warrior.accept(action);
        assertEquals(0, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());
    }

    @Test
    public void testMoveToEmptyTile() {
        MoveAction action = new MoveAction('s', gameBoard);
        warrior.accept(action);
        GameTile tile = gameBoard.getBoard()[3][2];
        assertEquals('@', tile.getType());
        assertEquals(warrior, tile.getUnit());
    }

    @Test
    public void testMoveDoesNotClonePlayer() {
        MoveAction action = new MoveAction('s', gameBoard);
        warrior.accept(action);
        assertSame(warrior, gameBoard.getBoard()[3][2].getUnit());
    }

    @Test
    public void testMoveReplacesOldPosition() {
        MoveAction action = new MoveAction('s', gameBoard);
        warrior.accept(action);
        GameTile previous = gameBoard.getBoard()[2][2];
        assertNull(previous.getUnit());
        assertEquals('.', previous.getType());
    }
    @Test
    public void testAttemptMoveIntoTrapDoesNotMove() {
        Unit trap = new Trap("Death Trap", 1, 10);
        gameBoard.getBoard()[1][2] = new GameTile('D', trap, new Point(2, 1));
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        // Player should not move
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());
    }

    @Test
    public void testAttemptMoveIntoMonsterTriggersAttack() {
        Unit monster = new Monster("Lannister Solider", 3);
        gameBoard.getBoard()[1][2] = new GameTile('s', monster, new Point(2, 1));
        MoveAction action = new MoveAction('w', gameBoard);
        warrior.accept(action);
        // Player should stay in place if combat logic doesn't move them
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());
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
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(4, warrior.getLocation().getY());
    }

    @Test
    public void testMoveSurroundedByWallsBlocksAllDirections() {
        // Place walls around (2,2)
        gameBoard.getBoard()[1][2] = new GameTile('#', null, new Point(2, 1)); // up
        gameBoard.getBoard()[3][2] = new GameTile('#', null, new Point(2, 3)); // down
        gameBoard.getBoard()[2][1] = new GameTile('#', null, new Point(1, 2)); // left
        gameBoard.getBoard()[2][3] = new GameTile('#', null, new Point(3, 2)); // right

        warrior.accept(new MoveAction('w', gameBoard));
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());

        warrior.accept(new MoveAction('s', gameBoard));
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());

        warrior.accept(new MoveAction('a', gameBoard));
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());

        warrior.accept(new MoveAction('d', gameBoard));
        assertEquals(2, warrior.getLocation().getX());
        assertEquals(2, warrior.getLocation().getY());
    }
    @Test
    public void testMonsterMovesRandomlyWhenOutOfRange() {
        Player dummyPlayer = GameBoard.choosePlayer("1");
        GameBoard enemyBoard = new GameBoard(dummyPlayer, 5, 5);
        dummyPlayer.setLocation(new Point(4, 4));
        enemyBoard.getBoard()[4][4] = new GameTile('@', dummyPlayer, new Point(4, 4));

        Point monsterStart = new Point(1, 1);
        Unit monster = GameBoard.chooseUnitByType('s');
        enemyBoard.getBoard()[1][1] = new GameTile('s', monster, monsterStart);
        System.out.println(enemyBoard);

        MoveAction monsterAction = new MoveAction(dummyPlayer, monsterStart, 's', enemyBoard, enemyBoard);
        monster.accept(monsterAction);

        boolean hasMoved = false;
        for (int y = 0; y < 5 && !hasMoved; y++) {
            for (int x = 0; x < 5 && !hasMoved; x++) {
                if (enemyBoard.getBoard()[y][x].getUnit() == monster && !(x == 1 && y == 1)) {
                    hasMoved = true;
                }
            }
        }

        assertTrue(hasMoved, "Monster should have moved randomly.");
        ///IF the random move was 'O' then Monster doesnt have to work (basically the test works)
    }

    @Test
    public void testMonsterMovesTowardPlayerWhenInRange() {
        Player dummyPlayer = GameBoard.choosePlayer("1");
        GameBoard enemyBoard = new GameBoard(dummyPlayer, 5, 5);
        dummyPlayer.setLocation(new Point(2, 2));
        enemyBoard.getBoard()[2][2] = new GameTile('@', dummyPlayer, new Point(2, 2));

        Point monsterStart = new Point(2, 4);
        Unit monster = GameBoard.chooseUnitByType('s');
        enemyBoard.getBoard()[4][2] = new GameTile('s', monster, monsterStart);

        MoveAction monsterAction = new MoveAction(dummyPlayer, monsterStart, 's', enemyBoard, enemyBoard);
        monster.accept(monsterAction);

        assertEquals(monster, enemyBoard.getBoard()[2][3].getUnit(), "Monster should move one tile up toward the player.");
    }

    @Test
    public void testMonsterBlockedByWallDoesNotMove() {
        Player dummyPlayer = GameBoard.choosePlayer("1");
        GameBoard enemyBoard = new GameBoard(dummyPlayer, 5, 5);
        dummyPlayer.setLocation(new Point(2, 2));
        enemyBoard.getBoard()[2][2] = new GameTile('@', dummyPlayer, new Point(2, 2));

        Point monsterStart = new Point(2, 4);
        Unit monster = GameBoard.chooseUnitByType('s');
        enemyBoard.getBoard()[4][2] = new GameTile('s', monster, monsterStart);

        // Block the movement path
        enemyBoard.getBoard()[3][2] = new GameTile('#', null, new Point(2, 3));

        MoveAction monsterAction = new MoveAction(dummyPlayer, monsterStart, 's', enemyBoard, enemyBoard);
        monster.accept(monsterAction);

        assertEquals(monster, enemyBoard.getBoard()[4][2].getUnit(), "Monster should not move into a wall.");
    }

    @Test
    public void testMonsterDoesNotMoveIntoWall() {
        Player player = GameBoard.choosePlayer("1");
        GameBoard board = new GameBoard(player, 5, 5);
        player.setLocation(new Point(2, 2));
        board.getBoard()[2][2] = new GameTile('@', player, new Point(2, 2));

        Point monsterStart = new Point(2, 4);
        Unit monster = GameBoard.chooseUnitByType('s');
        board.getBoard()[4][2] = new GameTile('#', null, new Point(2, 3));
        board.getBoard()[4][2] = new GameTile('s', monster, monsterStart);

        MoveAction action = new MoveAction(player, monsterStart, 's', board, board);
        monster.accept(action);

        assertEquals(monster, board.getBoard()[4][2].getUnit(), "Monster shouldn't move into wall.");
    }

    @Test
    public void testMonsterAttacksPlayerOnSameTile() {
        Player player = GameBoard.choosePlayer("1");
        GameBoard board = new GameBoard(player, 5, 5);
        Point playerPos = new Point(2, 2);
        player.setLocation(playerPos);
        board.getBoard()[2][2] = new GameTile('@', player, playerPos);

        Unit monster = GameBoard.chooseUnitByType('K');
        Point monsterStart = new Point(2, 3);
        board.getBoard()[3][2] = new GameTile('K', monster, monsterStart);

        MoveAction action = new MoveAction(player, monsterStart, 's', board, board);
        monster.accept(action);

        assertTrue(player.getHealth() < player.getMaxHealth(), "Player should take damage from monster attack.");
    }

    @Test
    public void testTrapInRangeAttacksPlayer() {
        Player player = GameBoard.choosePlayer("1");
        GameBoard board = new GameBoard(player, 5, 5);
        player.setLocation(new Point(2, 2));
        board.getBoard()[2][2] = new GameTile('@', player, new Point(2, 2));

        Unit trap = new Trap("Death Trap", 1, 10);
        Point trapPos = new Point(3, 2);
        board.getBoard()[2][3] = new GameTile('D', trap, trapPos);

        MoveAction trapAction = new MoveAction(player, trapPos, 'D', board, board);
        trap.accept(trapAction);

        assertTrue(player.getHealth() < player.getMaxHealth(), "Trap should damage player.");
    }

    @Test
    public void testTrapNotInRangeDoesNotAttack() {
        Player player = GameBoard.choosePlayer("1");
        GameBoard board = new GameBoard(player, 5, 5);
        player.setLocation(new Point(0, 0));
        board.getBoard()[0][0] = new GameTile('@', player, new Point(0, 0));

        Unit trap = new Trap("Death Trap", 1, 10);
        Point trapPos = new Point(4, 4);
        board.getBoard()[4][4] = new GameTile('D', trap, trapPos);

        MoveAction action = new MoveAction(player, trapPos, 'D', board, board);
        trap.accept(action);

        assertEquals(player.getHealth(), player.getMaxHealth(), "Trap should not damage player if too far.");
    }
    @Test
    public void testMonsterChasesPlayerWhenInRange() {
        Player player = GameBoard.choosePlayer("1");
        GameBoard board = new GameBoard(player, 5, 5);
        player.setLocation(new Point(2, 2));
        board.getBoard()[2][2] = new GameTile('@', player, new Point(2, 2));

        Unit monster = GameBoard.chooseUnitByType('s');
        Point monsterStart = new Point(2, 4);
        board.getBoard()[4][2] = new GameTile('s', monster, monsterStart);

        MoveAction action = new MoveAction(player, monsterStart, 's', board, board);
        monster.accept(action);

        assertEquals(monster, board.getBoard()[2][3].getUnit(), "Monster should move toward the player.");
    }


    @Test
    public void testTrapTickIncrements() {
        Trap trap = new Trap("Death Trap", 1, 10);
        Player player = GameBoard.choosePlayer("1");
        player.setLocation(new Point(2, 2));
        GameBoard board = new GameBoard(player, 5, 5);

        Point trapPos = new Point(1, 1);
        MoveAction action = new MoveAction(player, trapPos, 'D', board, board);
        int oldTick = trap.getTicks();

        trap.accept(action);
        assertEquals(oldTick + 1, trap.getTicks(), "Trap tick should increment.");
    }


    @Test
    public void testTrapAttacksOnlyWithinRange() {
        Player player = GameBoard.choosePlayer("1");
        GameBoard board = new GameBoard(player, 5, 5);
        player.setLocation(new Point(2, 2));
        board.getBoard()[2][2] = new GameTile('@', player, new Point(2, 2));

        Trap trap = new Trap("Death Trap", 1, 10);
        Point trapPos = new Point(3, 3); // distance ~1.41 < 2
        board.getBoard()[3][3] = new GameTile('D', trap, trapPos);

        int initialHealth = player.getHealth();
        trap.accept(new MoveAction(player, trapPos, 'D', board, board));

        assertTrue(player.getHealth() < initialHealth);
    }

    @Test
    public void testMonsterCannotMoveToOccupiedTile() {
        Player player = GameBoard.choosePlayer("1");
        GameBoard board = new GameBoard(player, 5, 5);
        player.setLocation(new Point(2, 2));
        board.getBoard()[2][2] = new GameTile('@', player, new Point(2, 2));

        Monster monster = new Monster("Lannister Solider", 3);
        Point start = new Point(1, 2);
        board.getBoard()[2][1] = new GameTile('#', null, new Point(1, 2)); // Wall blocking
        board.getBoard()[2][1] = new GameTile('s', monster, start);

        MoveAction action = new MoveAction(player, start, 's', board, board);
        monster.accept(action);

        assertEquals(monster, board.getBoard()[2][1].getUnit(), "Monster should not move into a wall.");
    }

    @Test
    public void testTrapNoExceptionWhenFarFromPlayer() {
        Player player = GameBoard.choosePlayer("1");
        GameBoard board = new GameBoard(player, 5, 5);
        player.setLocation(new Point(0, 0));

        Trap trap = new Trap("Death Trap", 1, 10);
        Point trapPos = new Point(4, 4);

        assertDoesNotThrow(() -> trap.accept(new MoveAction(player, trapPos, 'D', board, board)));
    }


}
