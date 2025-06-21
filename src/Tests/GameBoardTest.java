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
            "@s.\n" +
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
        assertEquals(3, tiles.length);
        assertEquals(3, tiles[0].length);

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
        GameBoard board = new GameBoard(tempFile.toString(), choosePlayer("5"));

        String expected =
                "@s.\n" +
                        "###\n" +
                        "...\n";

        assertEquals(expected, board.toString());
    }

    @Test
    public void testEnemyCount() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), choosePlayer("5"));
        assertEquals(1, board.getEnemyCount(), "There should be 1 enemy on the board.");
    }
}