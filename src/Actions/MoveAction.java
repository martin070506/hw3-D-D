package Actions;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Enemy;
import Player_Types.Mage;
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

    }

    @Override
    public void visitMage(Mage mage) {

    }

    @Override
    public void visitRogue(Rogue rogue) {
        if(rogue.isLegalMove(directionKey))
        {
            int currentX=rogue.getPlayerX();
            int currentY=rogue.getPlayerY();
            GameTile[][] boardMatrix=this.gameBoard.GetBoard();
            boardMatrix[currentY][currentX]=new GameTile('.',currentX,currentY);
            switch (directionKey) {
                case 'w':
                    rogue.setPlayerLocation(new Point(currentX,currentY-1));
                    break;
                case 'a':
                    rogue.setPlayerLocation(new Point(currentX-1,currentY));
                    break;
                case 's':
                    rogue.setPlayerLocation(new Point(currentX,currentY+1));
                    break;
                case 'd':
                    rogue.setPlayerLocation(new Point(currentX+1,currentY));
                    break;
            }
        }

    }

    @Override
    public void visitEnemy(Enemy enemy) {

    }
}
