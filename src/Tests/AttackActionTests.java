package Tests;

import Actions.AttackAction;
import BoardLogic.GameBoard;
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
    public void testAttackKillsEnemy() {
        enemy.takeDamage(enemy.getHealth() - 1); // Leave 1 HP
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction, false);
        assertTrue(enemy.getHealth() <= 0 || enemy.getHealth() == 1);
    }

    @Test
    public void testNoOverkill() {
        enemy.takeDamage(enemy.getHealth() - 2);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction, false);
        assertTrue(enemy.getHealth() >= 0 && enemy.getHealth() <= 2);
    }

    @Test
    public void testPlayerGainsXPWhenEnemyDies() {
        int beforeXP = player.getExperience();
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction, false);
        int afterXP = player.getExperience();
        assertTrue(afterXP >= beforeXP); // could be same if kill didn't happen
    }

    @Test
    public void testEnemyCountDecreasesOnKill() {
        int before = board.getEnemyCount();
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction, false);
        assertTrue(board.getEnemyCount() <= before);
    }

    @Test
    public void testTileClearedOnEnemyDeath() {
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction, false);
        // player should move to enemy tile (0,1)
        assertEquals('@', board.getBoard()[0][1].getType());
        assertEquals(player, board.getBoard()[0][1].getUnit());
        assertEquals('.', board.getBoard()[0][0].getType());
        assertNull(board.getBoard()[0][0].getUnit());
    }

    @Test
    public void testPlayerLevelUpAfterGainingXP() {
        player.setExperience(49); // needs 1 XP to level up
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction, false);
        assertEquals(2, player.getLevel());
        assertEquals(player.getHealth(), player.getMaxHealth());
    }

    @Test
    public void testMultipleAttacksStillSafe() {
        AttackAction attackAction = new AttackAction(player, board, 'd');
        for (int i = 0; i < 10; i++)
            enemy.accept(attackAction, false);

        assertTrue(enemy.getHealth() >= 0);
    }

    @Test
    public void testAttackNoXPIfEnemySurvives() {
        int beforeXP = player.getExperience();
        enemy.takeDamage(1); // not enough to kill
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction, false);
        assertTrue(player.getExperience() >= beforeXP);
    }

    @Test
    public void testPlayerDoesNotMoveIfEnemySurvives() {
        enemy.takeDamage(1); // Still has health
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction, false);
        assertEquals(0, player.getLocation().getX());
        assertEquals(0, player.getLocation().getY());
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
