package Tests;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import Player_Types.Rogue;
import UI.UserInterface;
import UI.UserInterfaceCallback;
import Unit_Logic.Unit;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {
    private static Path tempFile;
    private static final String TEST_MAP =
            "@s.\n" +
                    "###\n" +
                    "...";
    private UserInterfaceCallback UI;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("test_board", ".txt");
        Files.writeString(tempFile, TEST_MAP);
        UI = new UserInterface();
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testBoardBuildAndPlayerPosition() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), new Rogue("Arya Stark"), UI);

        GameTile[][] tiles = board.getBoard();
        assertNotNull(tiles);
        assertEquals(3, tiles.length);
        assertEquals(3, tiles[0].length);

        Point playerPos = board.getPlayer().getLocation();
        assertNotNull(playerPos);
        assertEquals(0, playerPos.getX());
        assertEquals(0, playerPos.getY());

        GameTile playerTile = tiles[0][0];
        assertEquals('@', playerTile.getType());
        assertNotNull(playerTile.getUnit());
    }

    @Test
    public void testIsLegalMoveTrue() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), new Rogue("Arya Stark"), UI);
        assertTrue(board.isLegalMove('d'));
    }

    @Test
    public void testIsLegalMoveFalse() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), new Rogue("Arya Stark"), UI);
        assertFalse(board.isLegalMove('w'));
    }

    @Test
    public void testIsLegalMoveToWall() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), new Rogue("Arya Stark"), UI);
        assertFalse(board.isLegalMove('s'));
    }

    @Test
    public void testIsLegalMoveAndUnitThereTrue() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), new Rogue("Arya Stark"), UI);
        assertTrue(board.isLegalMoveAndUnitThere('d'));
    }

    @Test
    public void testIsLegalMoveAndUnitThereFalse() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), new Rogue("Arya Stark"), UI);
        assertFalse(board.isLegalMoveAndUnitThere('a'));
    }

    @Test
    public void testChooseUnitByTypeKnownMonster() {
        Unit unit = GameBoard.chooseUnitByType('s');
        assertNotNull(unit);
        assertEquals("Lannister Solider", unit.getName());
    }

    @Test
    public void testChooseUnitByTypeTrap() {
        Unit unit = GameBoard.chooseUnitByType('D');
        assertNotNull(unit);
        assertEquals("Death Trap", unit.getName());
    }

    @Test
    public void testChooseUnitByTypeUnknown() {
        Unit unit = GameBoard.chooseUnitByType('#');
        assertNull(unit);
    }


    @Test
    public void testBoardToString() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), new Rogue("Arya Stark"), UI);

        String expected =
                "@s.\n" +
                        "###\n" +
                        "...\n";

        assertEquals(expected, board.toString());
    }

    @Test
    public void testEnemyCount() throws IOException {
        GameBoard board = new GameBoard(tempFile.toString(), new Rogue("Arya Stark"), UI);
        assertEquals(1, board.getEnemyCount(), "There should be 1 enemy on the board.");
    }
}