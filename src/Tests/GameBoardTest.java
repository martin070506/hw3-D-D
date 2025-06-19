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

        Point playerPos = board.getPlayerPosition();
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

        assertEquals(expected, board.toString());
    }

    @Test
    public void testSetPlayerPosition() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(),choosePlayer("5"));

        Point newPos = new Point(2, 2);
        board.setPlayerPosition(newPos);

        assertEquals(2, board.getPlayerPosition().getX());
        assertEquals(2, board.getPlayerPosition().getY());

        GameTile newTile = board.GetBoard()[2][2];
        assertEquals('@', newTile.getType());

        GameTile oldTile = board.GetBoard()[0][0];
        assertEquals('.', oldTile.getType());
    }
}
