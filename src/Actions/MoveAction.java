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
            int originalX=player.getPlayerX();
            int originalY=player.getPlayerY();
            GameTile[][] boardMatrix=this.gameBoard.GetBoard();
            boardMatrix[originalY][originalX]=new GameTile('.',null,originalX,originalY);
            switch (directionKey) {
                case 'w':
                    player.setPlayerLocation(new Point(originalX,originalY-1));
                    boardMatrix[originalY-1][originalX]=new GameTile('@',player,originalX,originalY-1);

                    //Handle if player stepped on a unit and create a new Attack Action
                    break;
                case 'a':
                    player.setPlayerLocation(new Point(originalX-1,originalY));
                    boardMatrix[originalY][originalX-1]=new GameTile('@',player,originalX-1,originalY);
                    //Handle if player stepped on a unit and create a new Attack Action
                    break;
                case 's':
                    player.setPlayerLocation(new Point(originalX,originalY+1));
                    boardMatrix[originalY+1][originalX]=new GameTile('@',player,originalX,originalY+1);
                    //Handle if player stepped on a unit and create a new Attack Action
                    break;
                case 'd':
                    player.setPlayerLocation(new Point(originalX+1,originalY));
                    boardMatrix[originalY][originalX+1]=new GameTile('@',player,originalX+1,originalY);
                    //Handle if player stepped on a unit and create a new Attack Action
                    break;
            }
        }
    }

    @Override
    public void visitEnemy(Enemy enemy) {

    }
}
