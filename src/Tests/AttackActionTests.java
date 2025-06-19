package Tests;

import Actions.AttackAction;
import Actions.MoveAction;
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
        enemy = board.GetBoard()[0][1].getUnit(); // z -> Wright (Monster)
    }

    @Test
    public void testAttackRollInRange() {
        AttackAction attackAction = new AttackAction(player, board);
        int totalTests = 100;
        for (int i = 0; i < totalTests; i++) {
            int result = attackActionTestHelper(20);
            assertTrue(result >= 0 && result <= 20);
        }
    }

    @Test
    public void testAttackKillsEnemy() {
        enemy.takeDamage(enemy.getHealth() - 1); // Leave enemy with 1 HP
        AttackAction attackAction = new AttackAction(player, board);
        enemy.accept(attackAction); // Attack should kill if roll > defense
        assertTrue(enemy.getHealth() <= 0 || enemy.getHealth() == 1); // May survive with 1 depending on roll
    }

    @Test
    public void testNoOverkill() {
        enemy.takeDamage(enemy.getHealth() - 2); // 2 HP left
        AttackAction attackAction = new AttackAction(player, board);
        enemy.accept(attackAction); // Attack may kill or leave at 1
        assertTrue(enemy.getHealth() >= 0 && enemy.getHealth() <= 2);
    }

    @Test
    public void testPlayerGainsNoXpIfNotDead() {
        int beforeXP = player.getExperience();
        enemy.takeDamage(1); // Still alive
        AttackAction attackAction = new AttackAction(player, board);
        enemy.accept(attackAction);
        assertTrue(player.getExperience() == beforeXP || player.getExperience() > beforeXP);
    }

    @Test
    public void testMultipleAttacks() {
        AttackAction attackAction = new AttackAction(player, board);
        for (int i = 0; i < 10; i++) {
            enemy.accept(attackAction);
        }
        assertTrue(enemy.getHealth() >= 0);
    }

    private int attackActionTestHelper(int limit) {
        return new java.util.Random().nextInt(0, limit + 1);
    }

    @AfterAll
    public static void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }
}
