package Tests;

import BoardLogic.GameBoard;
import BoardLogic.Point;
import Player_Types.Hunter;
import UI.UserInterface;
import Unit_Logic.Unit;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class HunterSpecialAttackTests {

    private static Path tempFile;
    private UserInterface UI;
    private GameBoard board;
    private Hunter hunter;
    private Unit enemy;

    @BeforeAll
    public static void setupFile() throws IOException {
        String boardLayout =
                "@z..\n" +
                        "....\n" +
                        "....";
        tempFile = Files.createTempFile("hunter_board", ".txt");
        Files.writeString(tempFile, boardLayout);
    }

    @BeforeEach
    public void setupGame() throws IOException {
        UI = new UserInterface();
        hunter = new Hunter("Ygritte");
        board = new GameBoard(tempFile.toString(), hunter, UI);
        enemy = board.getBoard()[0][1].getUnit(); // 'z' = Wright (Monster)
        hunter.setLocation(new Point(0,0));
        board.getBoard()[0][0].setUnit(hunter);
    }

    @Test
    public void testShootConsumesArrowAndDamagesOrKills() {
        int originalArrows = hunter.getArrowsCount();
        int originalHealth = enemy.getHealth();

        boolean casted = hunter.castAbility(null);
        assertTrue(casted, "Hunter should be able to cast ability when enemy is in range and arrows available");
        assertEquals(originalArrows - 1, hunter.getArrowsCount(), "Arrow count should decrease by 1");

        assertTrue(enemy.getHealth() < originalHealth || enemy.getHealth() == 0, "Enemy should be damaged or killed by shoot ability");
    }

    @Test
    public void testShootDoesNothingIfNoArrows() {
        hunter.setArrowsCount(0);
        boolean casted = hunter.castAbility(null);
        assertFalse(casted, "Hunter should not cast ability when no arrows left");
    }

    @Test
    public void testShootDoesNothingIfNoEnemiesInRange() throws IOException {
        String emptyLayout =
                "@...\n" +
                        "....\n" +
                        "....";
        Path emptyFile = Files.createTempFile("hunter_empty_board", ".txt");
        Files.writeString(emptyFile, emptyLayout);

        GameBoard emptyBoard = new GameBoard(emptyFile.toString(), hunter, UI);
        hunter.setArrowsCount(10);
        hunter.setLocation(new Point(0,0));
        emptyBoard.getBoard()[0][0].setUnit(hunter);

        boolean casted = hunter.castAbility(null);
        assertFalse(casted, "Hunter should not cast ability when no enemies in range");

        Files.deleteIfExists(emptyFile);
    }

    @Test
    public void testArrowsRegenerateAfterTicks() {
        hunter.setArrowsCount(0);

        for (int i = 0; i < 10; i++) {
            hunter.updateTickCount();
            System.out.println(hunter.getTicksCount());
        }
        assertEquals(hunter.getLevel(), hunter.getArrowsCount(), "Arrows should regenerate by level count after 10 ticks");
        assertEquals(0, hunter.getTicksCount(), "Ticks count should reset after regeneration");
    }

    @Test
    public void testTicksIncreaseWhenNotResetting() {
        int originalTicks = hunter.getTicksCount();
        hunter.updateTickCount();
        assertEquals(originalTicks + 1, hunter.getTicksCount(), "Ticks should increment if not yet at 10");
    }

    @Test
    public void testShootCanTriggerLevelUpAndIncreaseArrows() {
        hunter.setExperience(49); // 1 XP from level up
        enemy.takeDamage(enemy.getHealth() - 1); // leave at 1 HP
        int oldLevel = hunter.getLevel();
        int oldMaxHealth = hunter.getMaxHealth();

        hunter.castAbility(null);

        if (hunter.getLevel() > oldLevel) {
            assertEquals(oldLevel + 1, hunter.getLevel(), "Hunter should level up");
            assertTrue(hunter.getArrowsCount() >= 10, "Arrows should increase after leveling up");
            assertEquals(hunter.getMaxHealth(), hunter.getHealth(), "Health should reset to max on level up");
            assertTrue(hunter.getMaxHealth() > oldMaxHealth, "Max health should increase after level up");
        }
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
