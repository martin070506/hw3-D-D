package BoardLogic;

import Actions.AttackAction;
import EnemyTypes.Enemy;
import Player_Types.Player;
import Unit_Logic.Unit;

import java.util.ArrayList;

public interface GameBoardCallback {
    AttackAction playerAttack(Player player, char direction);
    void enemyAttack(Enemy enemy);
    ArrayList<Unit> getEnemiesInRange(Point point, int distance);
    void endGame();
}
