package Tests;

import Actions.SpecialAttackAction;
import BoardLogic.GameBoard;
import BoardLogic.Point;
import EnemyTypes.Monster;
import Player_Types.Mage;
import Unit_Logic.Unit;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class MageSpecialAttackTests {

    private static Path tempFile;
    private GameBoard board;
    private Mage mage;
    private SpecialAttackAction specialAttackAction;

    @BeforeAll
    public static void setupFile() throws IOException {
        String boardLayout =
                ".s.\n" +
                        "s@s\n" +
                        ".s.";
        tempFile = Files.createTempFile("mage_attack_board", ".txt");
        Files.writeString(tempFile, boardLayout);
    }

    @BeforeEach
    public void setupGame() throws IOException {
        mage = new Mage("TestMage", 100, 30, 5, 2, 100, 20, 3, 25);
        mage.setLocation(new Point(1, 1));
        board = new GameBoard(tempFile.toString(), mage);
        specialAttackAction = new SpecialAttackAction(board);
    }

    @Test
    public void testSpecialAttackConsumesManaAndHitsEnemies() {
        int initialMana = mage.getCurrentMana();
        int initialXP = mage.getExperience();
        mage.setCurrentMana(100); // ensure enough mana

        mage.accept(specialAttackAction);

        assertEquals(100 - mage.getManaCost(), mage.getCurrentMana(), "Mana should be reduced by mana cost");

        // Ensure only at most maxSpecialAbilityHits occurred
        int damagedCount = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Unit unit = board.getBoard()[y][x].getUnit();
                if (unit instanceof Monster) {
                    Monster m = (Monster) unit;
                    if (m.getHealth() < m.getMaxHealth()) {
                        damagedCount++;
                    }
                }
            }
        }
        System.out.println("Enemies Damaged:"+damagedCount);
        assertTrue(damagedCount <= mage.getMaxSpecialAbilityHits(),
                "Should not exceed max special hits");

        // XP might increase if enemies died
        assertTrue(mage.getExperience() >= initialXP, "Mage XP should not decrease");
    }

    @Test
    public void testSpecialAttackMayKillEnemies() {
        // weaken all nearby enemies to 1 HP
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Unit unit = board.getBoard()[y][x].getUnit();
                if (unit instanceof Monster) {
                    Monster m = (Monster) unit;
                    m.takeDamage(m.getHealth() - 1);
                }
            }
        }
        mage.setCurrentMana(100);

        int initialEnemyCount = board.getEnemyCount();
        int initialXP = mage.getExperience();

        mage.accept(specialAttackAction);

        assertTrue(board.getEnemyCount() <= initialEnemyCount,
                "Enemy count should stay the same or decrease");
        assertTrue(mage.getExperience() >= initialXP,
                "Mage should gain XP or at least not lose any");
    }

    @Test
    public void testSpecialAttackDoesNotMoveMage() {
        mage.setCurrentMana(100);
        Point startLocation = mage.getLocation();

        mage.accept(specialAttackAction);

        assertEquals(startLocation.getX(), mage.getLocation().getX(), "Mage X should stay");
        assertEquals(startLocation.getY(), mage.getLocation().getY(), "Mage Y should stay");
        assertEquals('@', board.getBoard()[startLocation.getY()][startLocation.getX()].getType(),
                "Mage still on the same tile");
    }

    @Test
    public void testSpecialAttackHandlesNoMana() {
        mage.setCurrentMana(5); // less than mana cost
        int initialXP = mage.getExperience();

        mage.accept(specialAttackAction);

        assertEquals(5, mage.getCurrentMana(), "Mana should not change if not enough");
        assertEquals(initialXP, mage.getExperience(), "XP should not change");
    }

    @Test
    public void testSpecialAttackHandlesNoTargets() throws IOException {
        String emptyLayout =
                "...\n" +
                        ".@.\n" +
                        "...";
        Path emptyFile = Files.createTempFile("empty_board", ".txt");
        Files.writeString(emptyFile, emptyLayout);

        GameBoard emptyBoard = new GameBoard(emptyFile.toString(), mage);
        SpecialAttackAction special = new SpecialAttackAction(emptyBoard);

        mage.setCurrentMana(100);
        int initialXP = mage.getExperience();

        mage.accept(special);

        assertEquals(80, mage.getCurrentMana(), "Mana should still be consumed");
        assertEquals(initialXP, mage.getExperience(), "XP should stay same if no targets");

        Files.deleteIfExists(emptyFile);
    }

    @Test
    public void testSpecialAttackMultipleBlizzards() {
        mage.setCurrentMana(100);

        for (int i = 0; i < 3; i++) {
            int beforeMana = mage.getCurrentMana();
            int beforeXP = mage.getExperience();

            mage.accept(specialAttackAction);

            assertTrue(mage.getCurrentMana() < beforeMana, "Mana should decrease on Blizzard");
            assertTrue(mage.getExperience() >= beforeXP, "XP should never decrease");
        }
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
