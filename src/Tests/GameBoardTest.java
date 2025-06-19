package Tests;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import Player_Types.Player;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static BoardLogic.GameBoard.choosePlayer;
import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {
    private static Path tempFile;
    private static final String TEST_MAP =
            "@.#\n" +
                    "###\n" +
                    "...";

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("test_board", ".txt");
        Files.writeString(tempFile, TEST_MAP);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testBoardBuildAndPlayerPosition() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), choosePlayer("5"));

        GameTile[][] tiles = board.GetBoard();
        assertNotNull(tiles);
        assertEquals(3, tiles.length);         // Height
        assertEquals(3, tiles[0].length);      // Width

        Point playerPos = board.getPlayer().getPlayerLocation();
        assertNotNull(playerPos);
        assertEquals(0, playerPos.getX());
        assertEquals(0, playerPos.getY());

        GameTile playerTile = tiles[0][0];
        assertEquals('@', playerTile.getType());
        assertNotNull(playerTile.getUnit());
    }

    @Test
    public void testBoardToString() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(),choosePlayer("5"));

        String expected =
                "@.#\n" +
                        "###\n" +
                        "...\n";

        assertEquals(expected,board.toString());
    }

    @Test
    public void testIsLegalMove() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), choosePlayer("5"));

        assertFalse(board.isLegalMove('a')); // left of (0,0) is out of bounds
        assertFalse(board.isLegalMove('s')); // down to (1,0) is wall '#'
        assertTrue(board.isLegalMove('d'));  // right to (0,1) is '.'
    }

    @Test
    public void testIsLegalMoveAndUnitThere() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), choosePlayer("5"));

        assertFalse(board.isLegalMoveAndUnitThere('d')); // No unit to the right
        assertFalse(board.isLegalMoveAndUnitThere('s')); // Wall below
    }

    @Test
    public void testPlayerReferencePreserved() throws IOException {
        Player player = choosePlayer("5");
        GameBoard board = new GameBoard(tempFile.toString(), player);
        assertSame(player, board.getPlayer());
    }
}
