package Actions;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Enemy;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Player;
import Player_Types.Rogue;
import Player_Types.Warrior;
import Unit_Logic.Unit;
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
        int originalX=player.getPlayerX();
        int originalY=player.getPlayerY();
        GameTile[][] boardMatrix=this.gameBoard.GetBoard();
        /*
        * Creating different 2 IF Blocks to show diffrence between enemy step, and normal step
        * doing this instead of putting an if block in each switchCase
        * */
        if(gameBoard.isLegalMove(directionKey)&&!gameBoard.isLegalMoveAndUnitThere(directionKey))
        {
            boardMatrix[originalY][originalX]=new GameTile('.',null,new Point(originalX, originalY));
            switch (directionKey) {
                case 'w':
                    player.setPlayerLocation(new Point(originalX,originalY-1));
                    boardMatrix[originalY-1][originalX]=new GameTile('@',player,new Point(originalX, originalY - 1));
                    break;
                case 'a':
                    player.setPlayerLocation(new Point(originalX-1,originalY));
                    boardMatrix[originalY][originalX-1]=new GameTile('@',player,new Point(originalX - 1, originalY));
                    break;
                case 's':
                    player.setPlayerLocation(new Point(originalX,originalY+1));
                    boardMatrix[originalY+1][originalX]=new GameTile('@',player,new Point(originalX, originalY + 1));
                    break;
                case 'd':
                    player.setPlayerLocation(new Point(originalX+1,originalY));
                    boardMatrix[originalY][originalX+1]=new GameTile('@',player,new Point(originalX + 1, originalY));
                    break;
            }
        }
        if(gameBoard.isLegalMoveAndUnitThere(directionKey))
        {
            Unit defender;
            switch (directionKey)
            {
                case 'w':
                    defender= boardMatrix[originalY-1][originalX].getUnit();
                    defender.accept(new AttackAction(player,gameBoard));
                    break;
                case 'a':
                    defender= boardMatrix[originalY][originalX-1].getUnit();
                    defender.accept(new AttackAction(player,gameBoard));
                    break;
                case 's':
                    defender= boardMatrix[originalY+1][originalX].getUnit();
                    defender.accept(new AttackAction(player,gameBoard));
                    break;
                case 'd':
                    defender= boardMatrix[originalY][originalX+1].getUnit();
                    defender.accept(new AttackAction(player,gameBoard));
                    break;

            }

        }


    }

    @Override
    public void visitMonster(Monster monster) {

    }

    @Override
    public void visitTrap(Trap trap) {

    }

    public GameBoard getBoard()
    {
        return this.gameBoard;
    }
}
