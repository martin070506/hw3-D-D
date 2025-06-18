package Actions;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Enemy;
import Player_Types.Mage;
import Player_Types.Player;
import Player_Types.Rogue;
import Player_Types.Warrior;
import Unit_Logic.UnitVisitor;

public class MoveAction implements UnitVisitor {
    private char directionKey;
    private GameBoard gameBoard;
    public MoveAction(char directionKey, GameBoard board)
    {
        this.directionKey=directionKey;
        this.gameBoard=board;
    }
    @Override
    public void visitWarrior(Warrior warrior) {
        visitPlayer(warrior);
    }

    @Override
    public void visitMage(Mage mage) {
        visitPlayer(mage);
    }

    @Override
    public void visitRogue(Rogue rogue) {
        visitPlayer(rogue);

    }
    private void visitPlayer(Player player)
    {
        if(player.isLegalMove(directionKey))
        {
            int currentX=player.getPlayerX();
            int currentY=player.getPlayerY();
            GameTile[][] boardMatrix=this.gameBoard.GetBoard();
            boardMatrix[currentY][currentX]=new GameTile('.',null,currentX,currentY);
            switch (directionKey) {
                case 'w':
                    player.setPlayerLocation(new Point(currentX,currentY-1));
                    //Handle if player stepped on a unit and create a new Attack Action
                    break;
                case 'a':
                    player.setPlayerLocation(new Point(currentX-1,currentY));
                    //Handle if player stepped on a unit and create a new Attack Action
                    break;
                case 's':
                    player.setPlayerLocation(new Point(currentX,currentY+1));
                    //Handle if player stepped on a unit and create a new Attack Action
                    break;
                case 'd':
                    player.setPlayerLocation(new Point(currentX+1,currentY));
                    //Handle if player stepped on a unit and create a new Attack Action
                    break;
            }
        }
    }

    @Override
    public void visitEnemy(Enemy enemy) {

    }
}
