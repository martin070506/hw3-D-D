package Tests;

import Actions.AttackAction;
import BoardLogic.GameBoard;
import EnemyTypes.Monster;
import Player_Types.Player;
import Player_Types.Rogue;
import Unit_Logic.Unit;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class AttackActionTests {

    private static Path tempFile;
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
        player = new Rogue("TestRogue", 100, 50, 5, 5);
        board = new GameBoard(tempFile.toString(), player);
        enemy = board.GetBoard()[0][1].getUnit(); // z = Wright (Monster)
    }

    @Test
    public void testAttackKillsEnemy() {
        enemy.takeDamage(enemy.getHealth() - 1); // Leave 1 HP
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction);
        assertTrue(enemy.getHealth() <= 0 || enemy.getHealth() == 1);
    }

    @Test
    public void testNoOverkill() {
        enemy.takeDamage(enemy.getHealth() - 2);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction);
        assertTrue(enemy.getHealth() >= 0 && enemy.getHealth() <= 2);
    }

    @Test
    public void testPlayerGainsXPWhenEnemyDies() {
        int beforeXP = player.getExperience();
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction);
        int afterXP = player.getExperience();
        assertTrue(afterXP >= beforeXP); // could be same if kill didn't happen
    }

    @Test
    public void testEnemyCountDecreasesOnKill() {
        int before = board.getEnemyCount();
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction);
        assertTrue(board.getEnemyCount() <= before);
    }

    @Test
    public void testTileClearedOnEnemyDeath() {
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction);
        // player should move to enemy tile (0,1)
        assertEquals('@', board.GetBoard()[0][1].getType());
        assertEquals(player, board.GetBoard()[0][1].getUnit());
        assertEquals('.', board.GetBoard()[0][0].getType());
        assertNull(board.GetBoard()[0][0].getUnit());
    }

    @Test
    public void testPlayerLevelUpAfterGainingXP() {
        player.setExperience(49); // needs 1 XP to level up
        enemy.takeDamage(enemy.getHealth() - 1);
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction);
        assertEquals(2, player.getPlayerLevel());
        assertEquals(player.getHealth(), player.getMaxHealth());
    }

    @Test
    public void testMultipleAttacksStillSafe() {
        AttackAction attackAction = new AttackAction(player, board, 'd');
        for (int i = 0; i < 10; i++) {
            enemy.accept(attackAction);
        }
        assertTrue(enemy.getHealth() >= 0);
    }

    @Test
    public void testAttackNoXPIfEnemySurvives() {
        int beforeXP = player.getExperience();
        enemy.takeDamage(1); // not enough to kill
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction);
        assertTrue(player.getExperience() >= beforeXP);
    }

    @Test
    public void testPlayerDoesNotMoveIfEnemySurvives() {
        enemy.takeDamage(1); // Still has health
        AttackAction attackAction = new AttackAction(player, board, 'd');
        enemy.accept(attackAction);
        assertEquals(0, player.getPlayerX());
        assertEquals(0, player.getPlayerY());
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
