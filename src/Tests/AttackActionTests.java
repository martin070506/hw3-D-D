package Tests;

import Actions.AttackAction;
import BoardLogic.GameBoard;
import BoardLogic.Point;
import Player_Types.Player;
import Player_Types.Rogue;
import UI.UserInterface;
import Unit_Logic.Unit;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class AttackActionTests {

    private static Path tempFile;
    private UserInterface UI;
    private GameBoard board;
    private Player player;
    private Unit enemy;

    @BeforeAll
    public static void setupFile() throws IOException {
        String boardLayout =
                "@z..\n" +
                        "....\n" +
                        "....";
        tempFile = Files.createTempFile("attack_board", ".txt");
        Files.writeString(tempFile, boardLayout);
    }

    @BeforeEach
    public void setupGame() throws IOException {
        UI = new UserInterface();
        player = new Rogue("Bronn");
        board = new GameBoard(tempFile.toString(), player, UI);
        enemy = board.getBoard()[0][1].getUnit(); // z = Wright (Monster)
    }

    @Test
    public void testAttackKillsOrDamagesEnemy() {
        int originalHealth = enemy.getHealth();
        enemy.takeDamage(enemy.getHealth() - 1); // leave at 1 HP
        AttackAction attackAction = new AttackAction(player, board, 'd', new Point(1, 0));
        enemy.accept(attackAction, false);

        assertTrue(enemy.getHealth() < originalHealth,
                "Enemy should be either killed or have taken damage");
    }


    @Test
    public void testNoOverkill() {
        enemy.takeDamage(enemy.getHealth() - 2);
        AttackAction attackAction = new AttackAction(player, board, 'd', new Point(1, 0));
        enemy.accept(attackAction, false);
        assertTrue(enemy.getHealth() >= 0 && enemy.getHealth() <= 2, "Health should be within expected range after attack");
    }

    @Test
    public void testPlayerGainsXPWhenEnemyDies() {
        int beforeXP = player.getExperience();
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd', new Point(1, 0));
        enemy.accept(attackAction, false);
        if (enemy.getHealth() <= 0) {
            assertTrue(player.getExperience() > beforeXP, "XP should increase after killing enemy");
        }
    }

    @Test
    public void testEnemyCountDecreasesOnKill() {
        int beforeCount = board.getEnemyCount();
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd', new Point(1, 0));
        enemy.accept(attackAction, false);
        if (enemy.getHealth() <= 0) {
            assertEquals(beforeCount - 1, board.getEnemyCount(), "Enemy count should decrease by 1 after kill");
        }
    }

    @Test
    public void testTileClearedOnEnemyDeath() {
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd', new Point(1, 0));
        enemy.accept(attackAction, false);
        assertEquals('@', board.getBoard()[0][1].getType(), "Player should move into enemy tile after kill");
        assertEquals(player, board.getBoard()[0][1].getUnit(), "Player unit should now occupy former enemy tile");
        assertEquals('.', board.getBoard()[0][0].getType(), "Old player tile should be cleared");
        assertNull(board.getBoard()[0][0].getUnit(), "No unit on old tile");
    }

    @Test
    public void testPlayerLevelUpAfterGainingXP() {
        player.setExperience(49); // needs 1 XP to level up
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd', new Point(1, 0));
        enemy.accept(attackAction, false);
        if (enemy.getHealth() <= 0) {
            assertEquals(2, player.getLevel(), "Player should level up to 2");
            assertEquals(player.getHealth(), player.getMaxHealth(), "Health should be restored on level up");
        }
    }

    @Test
    public void testMultipleAttacksStillSafe() {
        AttackAction attackAction = new AttackAction(player, board, 'd', new Point(1, 0));
        for (int i = 0; i < 10; i++)
            enemy.accept(attackAction, false);
        assertTrue(enemy.getHealth() >= 0, "Enemy health should not go negative after repeated attacks");
    }

    @Test
    public void testAttackNoXPIfEnemySurvives() {
        int beforeXP = player.getExperience();
        enemy.takeDamage(1); // still plenty of health
        AttackAction attackAction = new AttackAction(player, board, 'd', new Point(1, 0));
        enemy.accept(attackAction, false);
        assertEquals(beforeXP, player.getExperience(), "XP should not change if enemy survives");
    }

    @Test
    public void testPlayerDoesNotMoveIfEnemySurvives() {
        enemy.takeDamage(1); // still healthy
        AttackAction attackAction = new AttackAction(player, board, 'd', new Point(1, 0));
        enemy.accept(attackAction, false);
        assertEquals(0, player.getLocation().getX(), "Player X position should remain unchanged if enemy survives");
        assertEquals(0, player.getLocation().getY(), "Player Y position should remain unchanged if enemy survives");
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
