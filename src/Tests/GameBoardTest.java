package Tests;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {
    private static Path tempFile;

    @BeforeAll
    public static void setup() throws IOException {
        String testBoard =  "@.\n" +
                            "#.\n" +
                            "..";
        tempFile = Files.createTempFile("test_board_temp", ".txt");
        Files.writeString(tempFile, testBoard);
    }

    @Test
    public void testBoardBuildsCorrectly() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString());
        GameTile[][] tiles = board.GetBoard();
        assertEquals('@', tiles[0][0].getType());
        assertEquals('.', tiles[0][1].getType());
        assertEquals('#', tiles[1][0].getType());
        assertEquals('.', tiles[2][1].getType());
    }

    @Test
    public void testPlayerPositionSetCorrectly() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString());
        Point playerPos = board.getPlayerPosition();
        assertEquals(0, playerPos.getX());
        assertEquals(0, playerPos.getY());
    }

    @Test
    public void testToStringRepresentation() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString());
        String boardString = board.toString().trim();
        assertTrue(boardString.contains("@."));
        assertTrue(boardString.contains("#."));
        assertTrue(boardString.contains(".."));
    }

    @Test
    public void testTileTypes() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString());
        GameTile[][] tiles = board.GetBoard();
        assertEquals('.', tiles[2][0].getType());
        assertEquals('.', tiles[2][1].getType());
    }

    @Test
    public void testSetPlayerPosition() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString());
        Point newPoint = new Point(1, 2);
        board.setPlayerPosition(newPoint);
        Point result = board.getPlayerPosition();
        assertEquals(1, result.getX());
        assertEquals(2, result.getY());
    }

    @Test
    public void testBoardDimensions() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString());
        GameTile[][] tiles = board.GetBoard();
        assertEquals(3, tiles.length); // 3 rows
        assertEquals(2, tiles[0].length); // 2 columns
    }

    @Test
    public void testNoExtraCharacters() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString());
        GameTile[][] tiles = board.GetBoard();
        for (GameTile[] row : tiles) {
            for (GameTile tile : row) {
                char type = tile.getType();
                assertTrue(type == '@' || type == '.' || type == '#');
            }
        }
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
