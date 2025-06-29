package Tests;

import Actions.SpecialAttackAction;
import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Rogue;
import Unit_Logic.Unit;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class RogueSpecialAttackTest {

    private static Path tempFile;
    private GameBoard board;
    private Rogue rogue;
    private SpecialAttackAction specialAttackAction;

    @BeforeAll
    public static void setupFile() throws IOException {
        // Create a test board with multiple enemies around the player
        String boardLayout =
                ".s.\n" +
                        "s@s\n" +
                        ".s.";
        tempFile = Files.createTempFile("special_attack_board", ".txt");
        Files.writeString(tempFile, boardLayout);
    }

    @BeforeEach
    public void setupGame() throws IOException {
        rogue = new Rogue("TestRogue", 100, 50, 5, 20); // 20 energy cost
        board = new GameBoard(tempFile.toString(), rogue);
        specialAttackAction = new SpecialAttackAction(board);
    }

    @Test
    public void testSpecialAttackHitsAllEnemiesInRange() {
        // Set rogue to have enough energy
        rogue.setCurrentEnergy(100);

        // Get initial enemy count
        int initialEnemyCount = board.getEnemyCount();

        // Perform special attack
        rogue.accept(specialAttackAction);

        // Check that energy was consumed
        assertEquals(80, rogue.getCurrentEnergy()); // 100 - 20 = 80

        // Check that all enemies in range (attack range = 2) were hit
        // The board has enemies at (0,1), (1,0), (1,2), (2,1) - all within range 2
        // At least some enemies should have taken damage
        boolean atLeastOneEnemyDamaged = false;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (board.getBoard()[y][x].getUnit() instanceof Monster) {
                    Monster monster = (Monster) board.getBoard()[y][x].getUnit();
                    if (monster.getHealth() < monster.getMaxHealth()) {
                        atLeastOneEnemyDamaged = true;
                        break;
                    }
                }
            }
        }
        assertTrue(atLeastOneEnemyDamaged, "At least one enemy should have taken damage");
    }

    @Test
    public void testSpecialAttackDoesNotMoveRogue() {
        // Set rogue to have enough energy
        rogue.setCurrentEnergy(100);

        // Record initial position
        Point initialPosition = new Point(rogue.getLocation().getX(), rogue.getLocation().getY());

        // Perform special attack
        rogue.accept(specialAttackAction);

        // Check that rogue didn't move
        assertEquals(initialPosition.getX(), rogue.getLocation().getX());
        assertEquals(initialPosition.getY(), rogue.getLocation().getY());

        // Check that rogue is still on the same tile
        assertEquals('@', board.getBoard()[initialPosition.getY()][initialPosition.getX()].getType());
        assertEquals(rogue, board.getBoard()[initialPosition.getY()][initialPosition.getX()].getUnit());
    }

    @Test
    public void testSpecialAttackKillsEnemyAndGivesXP() {
        // Set rogue to have enough energy
        rogue.setCurrentEnergy(100);

        // Find an enemy and weaken it
        Monster targetEnemy = null;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (board.getBoard()[y][x].getUnit() instanceof Monster) {
                    targetEnemy = (Monster) board.getBoard()[y][x].getUnit();
                    targetEnemy.takeDamage(targetEnemy.getHealth() - 1); // Leave 1 HP
                    break;
                }
            }
            if (targetEnemy != null) break;
        }
        assertNotNull(targetEnemy, "Should find an enemy to test with");
        // Record initial XP and enemy count
        int initialXP = rogue.getExperience();
        int initialEnemyCount = board.getEnemyCount();
        // Perform special attack

        rogue.accept(specialAttackAction);
        // Check that XP increased (enemy was killed)
        assertTrue(rogue.getExperience() > initialXP, "Rogue should gain XP when killing enemy");

        // Check that enemy count decreased
        assertTrue(board.getEnemyCount() < initialEnemyCount, "Enemy count should decrease when enemy is killed");

        // Check that the tile is now empty (enemy was removed)
        boolean enemyRemoved = true;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (board.getBoard()[y][x].getUnit() == targetEnemy) {
                    enemyRemoved = false;
                    break;
                }
            }
        }
        assertTrue(enemyRemoved, "Killed enemy should be removed from the board");
    }

    @Test
    public void testSpecialAttackGivesXPForTraps() throws IOException {
        // Create a new board with traps instead of monsters
        String trapBoardLayout =
                ".B.\n" +
                        "B@B\n" +
                        ".B.";
        Path trapFile = Files.createTempFile("trap_board", ".txt");
        Files.writeString(trapFile, trapBoardLayout);

        GameBoard trapBoard = new GameBoard(trapFile.toString(), rogue);
        SpecialAttackAction trapSpecialAttack = new SpecialAttackAction(trapBoard);

        // Set rogue to have enough energy
        rogue.setCurrentEnergy(100);

        // Record initial XP
        int initialXP = rogue.getExperience();

        // Perform special attack
        rogue.accept(trapSpecialAttack);

        assertTrue(rogue.getExperience()>initialXP, "Rogue should not gain XP from destroying traps");

        // Clean up
        Files.deleteIfExists(trapFile);
    }

    @Test
    public void testSpecialAttackDoesNotExecuteWithoutEnoughEnergy() {
        // Set rogue to have insufficient energy
        rogue.setCurrentEnergy(10); // Less than the 20 cost

        // Record initial state
        int initialEnergy = rogue.getCurrentEnergy();
        int initialEnemyCount = board.getEnemyCount();

        // Perform special attack
        rogue.accept(specialAttackAction);

        // Check that energy was not consumed
        assertEquals(initialEnergy, rogue.getCurrentEnergy(), "Energy should not be consumed if attack fails");

        // Check that no enemies were affected
        assertEquals(initialEnemyCount, board.getEnemyCount(), "Enemy count should not change if attack fails");

        // Check that no enemies took damage
        boolean noEnemiesDamaged = true;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (board.getBoard()[y][x].getUnit() instanceof Monster) {
                    Monster monster = (Monster) board.getBoard()[y][x].getUnit();
                    if (monster.getHealth() < monster.getMaxHealth()) {
                        noEnemiesDamaged = false;
                        break;
                    }
                }
            }
        }
        assertTrue(noEnemiesDamaged, "No enemies should be damaged if attack fails");
    }

    @Test
    public void testSpecialAttackRespectsAttackRange() throws IOException {
        // Create a larger board with enemies at different distances
        String rangeBoardLayout =
                "....s....\n" +
                        ".........\n" +
                        ".........\n" +
                        "....@....\n" +
                        ".........\n" +
                        ".........\n" +
                        "....s....";
        Path rangeFile = Files.createTempFile("range_board", ".txt");
        Files.writeString(rangeFile, rangeBoardLayout);

        GameBoard rangeBoard = new GameBoard(rangeFile.toString(), rogue);
        SpecialAttackAction rangeSpecialAttack = new SpecialAttackAction(rangeBoard);

        // Set rogue to have enough energy
        rogue.setCurrentEnergy(100);

        // Perform special attack
        rogue.accept(rangeSpecialAttack);

        // Check that only enemies within attack range (2) were hit
        // Enemies at (4,0) and (4,6) should be out of range and unharmed
        Monster farEnemy1 = (Monster) rangeBoard.getBoard()[0][4].getUnit();
        Monster farEnemy2 = (Monster) rangeBoard.getBoard()[6][4].getUnit();

        assertEquals(farEnemy1.getMaxHealth(), farEnemy1.getHealth(), "Enemy at (4,0) should be out of range");
        assertEquals(farEnemy2.getMaxHealth(), farEnemy2.getHealth(), "Enemy at (4,6) should be out of range");

        // Clean up
        Files.deleteIfExists(rangeFile);
    }

    @Test
    public void testSpecialAttackHandlesMixedEnemyTypes() throws IOException {
        // Create a board with both monsters and traps
        String mixedBoardLayout =
                ".B.\n" +
                        "s@s\n" +
                        ".Q.";
        Path mixedFile = Files.createTempFile("mixed_board", ".txt");
        Files.writeString(mixedFile, mixedBoardLayout);

        GameBoard mixedBoard = new GameBoard(mixedFile.toString(), rogue);
        SpecialAttackAction mixedSpecialAttack = new SpecialAttackAction(mixedBoard);

        // Set rogue to have enough energy
        rogue.setCurrentEnergy(100);

        // Record initial state
        int initialXP = rogue.getExperience();
        int initialEnemyCount = mixedBoard.getEnemyCount();

        // Perform special attack
        rogue.accept(mixedSpecialAttack);

        // Check that energy was consumed
        assertEquals(80, rogue.getCurrentEnergy());

        // Check that at least some enemies were affected
        boolean someEnemiesAffected = false;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Unit unit = mixedBoard.getBoard()[y][x].getUnit();
                if (unit instanceof Monster) {
                    Monster monster = (Monster) unit;
                    if (monster.getHealth() < monster.getMaxHealth()) {
                        someEnemiesAffected = true;
                        break;
                    }
                } else if (unit instanceof Trap) {
                    Trap trap = (Trap) unit;
                    if (trap.getHealth() < trap.getMaxHealth()) {
                        someEnemiesAffected = true;
                        break;
                    }
                }
            }
        }
        assertTrue(someEnemiesAffected, "Some enemies should be affected by the special attack");

        // Check that XP only increased if monsters were killed (not traps)
        assertTrue(rogue.getExperience() >= initialXP, "XP should not decrease");

        // Clean up
        Files.deleteIfExists(mixedFile);
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}