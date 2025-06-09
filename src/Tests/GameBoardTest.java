package Tests;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {

    private static Path tempFile;

    @BeforeAll
    public static void setup() throws IOException {
        String testBoard =
                "@123\n" +
                        "ABCD\n" +
                        "EFGH\n";
        tempFile = Files.createTempFile("test_board", ".txt");
        Files.writeString(tempFile, testBoard);
    }

    @Test
    public void testBoardLoading() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString());
        GameTile[][] tiles = board.GetBoard();

        assertEquals('@', tiles[0][0].GetTile());
        assertEquals('C', tiles[1][2].GetTile());
        assertEquals('A', tiles[1][0].GetTile());
        assertEquals('H', tiles[2][3].GetTile());
    }

    @Test
    public void testToStringOutput() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString());
        String output = board.toString().trim();
        assertTrue(output.contains("@123"));
        assertTrue(output.contains("ABCD"));
    }

    @AfterAll
    public static void cleanUp() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}

