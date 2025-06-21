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

import java.util.Random;
import java.util.Set;

public class MoveAction implements UnitVisitor {
    private char directionKey;
    private GameBoard gameBoard;
    private Point enemyLocation;
    private Player player;
    private char enemyChar;
    public MoveAction(char directionKey, GameBoard board)
    {
        this.directionKey=directionKey;
        this.gameBoard=board;
        //this builder will be called when a player moves
    }
    public MoveAction(Player player,Point enemyLocation,char enemyChar,GameBoard board)
    {
        this.enemyChar=enemyChar;
        this.player=player;
        this.enemyLocation=enemyLocation;
        this.gameBoard=board;
        //this builder will be called when an enemy Moves, so the game can calculate where the enemy should go
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
        if(this.gameBoard==null){return;}
        GameTile[][] boardMatrix=this.gameBoard.GetBoard();
        /*
        * Creating different 2 IF Blocks to show diffrence between enemy step, and normal step
        * doing this instead of putting an if block in each switchCase
        * */
        if(gameBoard!=null&&gameBoard.isLegalMove(directionKey)&&!gameBoard.isLegalMoveAndUnitThere(directionKey))
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
        if(gameBoard!=null&&gameBoard.isLegalMoveAndUnitThere(directionKey))
        {
            Unit defender;
            switch (directionKey)
            {
                case 'w':
                    defender= boardMatrix[originalY-1][originalX].getUnit();
                    defender.accept(new AttackAction(player,gameBoard,'w'));
                    break;
                case 'a':
                    defender= boardMatrix[originalY][originalX-1].getUnit();
                    defender.accept(new AttackAction(player,gameBoard,'a'));
                    break;
                case 's':
                    defender= boardMatrix[originalY+1][originalX].getUnit();
                    defender.accept(new AttackAction(player,gameBoard,'s'));
                    break;
                case 'd':
                    defender= boardMatrix[originalY][originalX+1].getUnit();
                    defender.accept(new AttackAction(player,gameBoard,'d'));
                    break;

            }

        }


    }

    @Override
    public void visitMonster(Monster monster) {
        GameTile[][] matrix= gameBoard.GetBoard();
        System.out.println("From Move Action: \n"+gameBoard);

        if(playerInRange(monster))
        {

           int dx=enemyLocation.getX()-player.getPlayerX();
           int dy=enemyLocation.getY()- player.getPlayerY();
           if(Math.abs(dx)-Math.abs(dy)>0)
           {
               if(dx>0) ///MOVE LEFT
               {
                   Point updatedLocation=new Point(enemyLocation.getX()-1, enemyLocation.getY());
                   if(isLegalMonsterMove(updatedLocation)&& !isPlayerInTile(updatedLocation))
                   {
                       matrix[enemyLocation.getY()][enemyLocation.getX()]=new GameTile('.',null,enemyLocation);
                       matrix[updatedLocation.getY()][updatedLocation.getX()]=new GameTile(enemyChar,monster,updatedLocation);
                   }
                   if(isLegalMonsterMove(updatedLocation)&& isPlayerInTile(updatedLocation))
                   {
                       //TODO handle attack Logic
                   }
               }
               else///MOVE RIGHT
               {
                   Point updatedLocation=new Point(enemyLocation.getX()+1, enemyLocation.getY());
                   if(isLegalMonsterMove(updatedLocation)&& !isPlayerInTile(updatedLocation))
                   {
                       matrix[enemyLocation.getY()][enemyLocation.getX()]=new GameTile('.',null,enemyLocation);
                       matrix[updatedLocation.getY()][updatedLocation.getX()]=new GameTile(enemyChar,monster,updatedLocation);
                   }
                   if(isLegalMonsterMove(updatedLocation)&& isPlayerInTile(updatedLocation))
                   {
                       //TODO handle attack Logic
                   }
               }
           }
           else
           {

               if(dy>0) ///MOVE UP
               {
                   Point updatedLocation=new Point(enemyLocation.getX(), enemyLocation.getY()-1);
                   if(isLegalMonsterMove(updatedLocation)&& !isPlayerInTile(updatedLocation))
                   {
                       matrix[enemyLocation.getY()][enemyLocation.getX()]=new GameTile('.',null,enemyLocation);
                       matrix[updatedLocation.getY()][updatedLocation.getX()]=new GameTile(enemyChar,monster,updatedLocation);
                   }
                   if(isLegalMonsterMove(updatedLocation)&& isPlayerInTile(updatedLocation))
                   {
                       //TODO handle attack Logic
                   }
               }
               else ///MOVE DOWN
               {
                   Point updatedLocation=new Point(enemyLocation.getX(), enemyLocation.getY()+1);
                   if(isLegalMonsterMove(updatedLocation)&& !isPlayerInTile(updatedLocation))
                   {
                       matrix[enemyLocation.getY()][enemyLocation.getX()]=new GameTile('.',null,enemyLocation);
                       matrix[updatedLocation.getY()][updatedLocation.getX()]=new GameTile(enemyChar,monster,updatedLocation);
                   }
                   if(isLegalMonsterMove(updatedLocation)&& isPlayerInTile(updatedLocation))
                   {
                       //TODO handle attack Logic
                   }
               }
           }
        }
        else
        {


            char directionKey=chooseRandomStepForMonster();
            switch (directionKey)
            {

                case 'w':
                {


                    Point updatedLocation=new Point(enemyLocation.getX(), enemyLocation.getY()-1);
                    if(isLegalMonsterMove(updatedLocation)&& !isPlayerInTile(updatedLocation))
                    {
                        matrix[enemyLocation.getY()][enemyLocation.getX()]=new GameTile('.',null,enemyLocation);
                        matrix[updatedLocation.getY()][updatedLocation.getX()]=new GameTile(enemyChar,monster,updatedLocation);
                        System.out.println("From Move Action: W \n"+gameBoard);

                    }
                    if(isLegalMonsterMove(updatedLocation)&& isPlayerInTile(updatedLocation))
                    {
                        //TODO handle attack Logic
                    }
                    break;
                }
                case 'a':
                {

                    Point updatedLocation=new Point(enemyLocation.getX()-1, enemyLocation.getY());
                    if(isLegalMonsterMove(updatedLocation)&& !isPlayerInTile(updatedLocation))
                    {
                        matrix[enemyLocation.getY()][enemyLocation.getX()]=new GameTile('.',null,enemyLocation);
                        matrix[updatedLocation.getY()][updatedLocation.getX()]=new GameTile(enemyChar,monster,updatedLocation);
                        System.out.println("From Move Action: A \n"+gameBoard);
                    }
                    if(isLegalMonsterMove(updatedLocation)&& isPlayerInTile(updatedLocation))
                    {
                        //TODO handle attack Logic
                    }
                    break;
                }
                case 's':
                {

                    Point updatedLocation=new Point(enemyLocation.getX(), enemyLocation.getY()+1);
                    if(isLegalMonsterMove(updatedLocation)&& !isPlayerInTile(updatedLocation))
                    {
                        matrix[enemyLocation.getY()][enemyLocation.getX()]=new GameTile('.',null,enemyLocation);
                        matrix[updatedLocation.getY()][updatedLocation.getX()]=new GameTile(enemyChar,monster,updatedLocation);
                        System.out.println("From Move Action: S \n"+gameBoard);
                    }
                    if(isLegalMonsterMove(updatedLocation)&& isPlayerInTile(updatedLocation))
                    {
                        //TODO handle attack Logic
                    }
                    break;
                }
                case 'd':
                {

                    Point updatedLocation=new Point(enemyLocation.getX()+1, enemyLocation.getY());
                    if(isLegalMonsterMove(updatedLocation)&& !isPlayerInTile(updatedLocation))
                    {
                        matrix[enemyLocation.getY()][enemyLocation.getX()]=new GameTile('.',null,enemyLocation);
                        matrix[updatedLocation.getY()][updatedLocation.getX()]=new GameTile(enemyChar,monster,updatedLocation);
                        System.out.println("From Move Action: D \n"+gameBoard);
                    }
                    if(isLegalMonsterMove(updatedLocation)&& isPlayerInTile(updatedLocation))
                    {
                        //TODO handle attack Logic
                    }
                    break;
                }
                default:{System.out.println("O");}
            }
        }
    }

    @Override
    public void visitTrap(Trap trap) {

    }

    private boolean playerInRange(Monster monster)
    {
        return (player.getPlayerLocation().distance(enemyLocation)<= monster.getVisionRange());
    }
    private boolean isLegalMonsterMove(Point chosenMonsterTile)
    {
        GameTile[][] board= gameBoard.GetBoard();
        int legalX=board[0].length-1;
        int legalY=board.length-1;
        if(chosenMonsterTile.getX()>legalX ||chosenMonsterTile.getX()<0) return false;
        if(chosenMonsterTile.getY()>legalY||chosenMonsterTile.getY()<0) return false;
       if(!Set.of('.','@') .contains(gameBoard.GetBoard()[chosenMonsterTile.getY()][chosenMonsterTile.getX()].getType())) return false;
       return true;

    }
    private boolean isPlayerInTile(Point p)
    {
        return (p.equals(player.getPlayerLocation()));
    }
    private char chooseRandomStepForMonster()
    {
        char[] chars = {'w', 'a', 's', 'd', 'o'};
        //
        Random random = new Random();
        int index = random.nextInt(chars.length);
        return chars[index];
    }
}
