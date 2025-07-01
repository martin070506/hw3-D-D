package BoardLogic;

import Actions.AttackAction;
import EnemyTypes.Enemy;
import Player_Types.Player;
import Unit_Logic.Unit;

import java.util.ArrayList;

public interface GameBoardCallback {
    AttackAction playerAttack(Player player, char direction, Point location);
    void enemyAttack(Enemy enemy, Point location);
    ArrayList<GameTile> getTileEnemiesInRange(Point point, int distance);
    void endGame();
    void update(String message);
}
