package Actions;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
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
}
