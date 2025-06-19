package Tests;

import Actions.MoveAction;
import BoardLogic.GameBoard;
import Player_Types.Player;
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
        rogue = (Rogue) GameBoard.choosePlayer("5"); // Player 5 = Arya Stark
        board = new GameBoard(tempFile.toString(), rogue); // No input required
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
        // Move right into (1,0) – should work
        rogue.accept(new MoveAction('d', board));
        assertEquals(1, rogue.getPlayerX());
        assertEquals(0, rogue.getPlayerY());

        // Try to move down into (1,1) – blocked by wall
        rogue.accept(new MoveAction('s', board));
        assertEquals(1, rogue.getPlayerX()); // no change
        assertEquals(0, rogue.getPlayerY()); // still at previous position
    }

    @Test
    public void testMoveLeftInvalidAtEdge() {
        rogue.accept(new MoveAction('a', board)); // left from (0,0)
        assertEquals(0, rogue.getPlayerX()); // shouldn't move
    }

    @Test
    public void testMoveUpInvalidAtEdge() {
        rogue.accept(new MoveAction('w', board)); // up from (0,0)
        assertEquals(0, rogue.getPlayerY()); // shouldn't move
    }

    @Test
    public void testBoardTileReplaced() {
        rogue.accept(new MoveAction('d', board)); // move right
        assertEquals('.', board.GetBoard()[0][0].getType()); // original tile cleared
        assertEquals('@', board.GetBoard()[0][1].getType()); // new position is player
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
