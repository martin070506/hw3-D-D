package Tests;

import Actions.MoveAction;
import BoardLogic.GameBoard;
import Player_Types.Rogue;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private static Path tempFile;
    private GameBoard board;
    private Rogue rogue;

    @BeforeAll
    public static void setupFile() throws IOException {
        String testBoard =
                "@...\n" +  // player starts at (0,0)
                        ".#..\n" +
                        "....";
        tempFile = Files.createTempFile("test_board", ".txt");
        Files.writeString(tempFile, testBoard);
    }

    @BeforeEach
    public void setupGame() throws IOException {
        board = new GameBoard(tempFile.toString());
        rogue = new Rogue("TestRogue", 100, 20, 10, 10);
    }

    @Test
    public void testInitialPosition() {
        assertEquals(0, rogue.getPlayerX());
        assertEquals(0, rogue.getPlayerY());
    }

    @Test
    public void testMoveRight() {
        MoveAction move = new MoveAction('d', board);
        rogue.accept(move);
        assertEquals(1, rogue.getPlayerX());
        assertEquals(0, rogue.getPlayerY());
    }

    @Test
    public void testMoveDown() {
        MoveAction move = new MoveAction('s', board);
        rogue.accept(move);
        assertEquals(0, rogue.getPlayerX());
        assertEquals(1, rogue.getPlayerY());
    }

    @Test
    public void testBlockedByWall() {
        // Starting at (0,0), try to move right into (1,0) — should succeed
        MoveAction move1 = new MoveAction('d', board);
        rogue.accept(move1);
        assertEquals(1, rogue.getPlayerX());
        assertEquals(0, rogue.getPlayerY());

        // Now try to move down into (1,1), which is a wall ('#') — should NOT move
        MoveAction move2 = new MoveAction('s', board);
        rogue.accept(move2);
        assertEquals(1, rogue.getPlayerX()); // Should stay the same
        assertEquals(0, rogue.getPlayerY()); // No movement into wall
    }

    @Test
    public void testMoveLeftInvalidAtEdge() {
        MoveAction move = new MoveAction('a', board);
        rogue.accept(move);
        assertEquals(0, rogue.getPlayerX()); // can't move left at edge
    }

    @Test
    public void testMoveUpInvalidAtEdge() {
        MoveAction move = new MoveAction('w', board);
        rogue.accept(move);
        assertEquals(0, rogue.getPlayerY()); // can't move up at top edge
    }

    @Test
    public void testBoardTileReplaced() {
        MoveAction move = new MoveAction('d', board);
        rogue.accept(move);
        assertEquals('.', board.GetBoard()[0][0].getType()); // old tile updated
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
