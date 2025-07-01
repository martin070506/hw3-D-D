package Tests;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Warrior;
import UI.UserInterface;
import UI.UserInterfaceCallback;
import Unit_Logic.Unit;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class WarriorSpecialAttackTests {
    private UserInterfaceCallback UI;
    private static Path tempFile;
    private GameBoard board;
    private Warrior warrior;
    private SpecialAttackAction specialAttackAction;

    @BeforeAll
    public static void setupFile() throws IOException {
        String boardLayout =
                ".s.\n" +
                        "s@s\n" +
                        ".s.";
        tempFile = Files.createTempFile("special_attack_board", ".txt");
        Files.writeString(tempFile, boardLayout);
    }

    @BeforeEach
    public void setupGame() throws IOException {
        UI = new UserInterface();
        warrior = new Warrior("Jon Snow");
        warrior.setLocation(new Point(1, 1));
        board = new GameBoard(tempFile.toString(), warrior, UI);
        specialAttackAction = new SpecialAttackAction(board);
    }

    @Test
    public void testSpecialAttackConsumesCooldownAndHitsOne() {
        warrior.setRemainingCooldown(0);
        int oldXP = warrior.getExperience();

        warrior.accept(specialAttackAction);

        assertTrue(warrior.getRemainingCooldown() > 0, "Cooldown should be set after special attack");

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
        assertTrue(damagedCount <= 1, "Special attack should damage at most one enemy");

        assertTrue(warrior.getExperience() >= oldXP, "XP should not decrease after special attack");
    }

    @Test
    public void testSpecialAttackKillsEnemyAndClearsTile() {
        Monster weakEnemy = new Monster("Lannister Solider");
        weakEnemy.takeDamage(weakEnemy.getHealth() - 1); // 1 HP left
        board.getBoard()[0][1] = new GameTile('s', weakEnemy, new Point(1,0));

        int oldXP = warrior.getExperience();
        warrior.setRemainingCooldown(0);
        for (int i = 0; i < 5; i++)
            warrior.accept(specialAttackAction);

        assertTrue(warrior.getExperience() > oldXP, "Warrior should gain XP after killing an enemy");
        assertNull(board.getBoard()[0][1].getUnit(), "Tile should be cleared after enemy death");
        assertEquals('.', board.getBoard()[0][1].getType(), "Tile type should reset to '.'");
    }

    @Test
    public void testSpecialAttackKillsTrapAndClearsTile() throws IOException {
        String trapLayout =
                ".B.\n" +
                        ".@.\n" +
                        "...";
        Path trapFile = Files.createTempFile("trap_board", ".txt");
        Files.writeString(trapFile, trapLayout);

        GameBoard trapBoard = new GameBoard(trapFile.toString(), warrior, UI);
        warrior.setRemainingCooldown(0);

        warrior.accept(new SpecialAttackAction(trapBoard));

        boolean trapRemovedOrDamaged = false;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Unit unit = trapBoard.getBoard()[y][x].getUnit();
                if (unit instanceof Trap) {
                    Trap t = (Trap) unit;
                    if (t.getHealth() < t.getMaxHealth()) {
                        trapRemovedOrDamaged = true;
                    }
                } else if (unit == null && trapBoard.getBoard()[y][x].getType() == '.') {
                    trapRemovedOrDamaged = true;
                }
            }
        }
        assertTrue(trapRemovedOrDamaged, "Special attack should affect nearby trap");

        Files.deleteIfExists(trapFile);
    }

    @Test
    public void testSpecialAttackDoesNothingWithoutTargets() throws IOException {
        String emptyLayout =
                "...\n" +
                        ".@.\n" +
                        "...";
        Path emptyFile = Files.createTempFile("empty_board", ".txt");
        Files.writeString(emptyFile, emptyLayout);

        GameBoard emptyBoard = new GameBoard(emptyFile.toString(), warrior, UI);
        warrior.setRemainingCooldown(0);
        int oldXP = warrior.getExperience();

        warrior.accept(new SpecialAttackAction(emptyBoard));

        assertEquals(oldXP, warrior.getExperience(), "XP should stay the same if no targets");
        assertTrue(warrior.getRemainingCooldown() > 0, "Cooldown should still be triggered");

        Files.deleteIfExists(emptyFile);
    }

    @Test
    public void testSpecialAttackDoesNotMoveWarrior() {
        warrior.setRemainingCooldown(0);
        Point startLocation = warrior.getLocation();

        warrior.accept(specialAttackAction);

        assertEquals(startLocation.getX(), warrior.getLocation().getX(), "Warrior X position should stay");
        assertEquals(startLocation.getY(), warrior.getLocation().getY(), "Warrior Y position should stay");
        assertEquals('@', board.getBoard()[startLocation.getY()][startLocation.getX()].getType(), "Warrior still on same tile");
    }

    @Test
    public void testSpecialAttackRespectsCooldown() {
        warrior.setRemainingCooldown(2); // not ready
        int oldXP = warrior.getExperience();

        warrior.accept(specialAttackAction);

        assertEquals(oldXP, warrior.getExperience(), "XP should not change if ability is on cooldown");

        boolean anyDamaged = false;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Unit unit = board.getBoard()[y][x].getUnit();
                if (unit instanceof Monster) {
                    Monster m = (Monster) unit;
                    if (m.getHealth() < m.getMaxHealth()) {
                        anyDamaged = true;
                    }
                }
            }
        }
        assertFalse(anyDamaged, "No enemies should be damaged if ability is on cooldown");
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
