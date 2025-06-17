package Player_Types;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;
import jdk.jshell.spi.ExecutionControl;

public abstract class Player extends Unit {

    private Point playerLocation;
    private int experience;
    private int playerLevel;
    private GameBoard gameBoard;

    public Player(String name, int maxHealth, int attack, int defense,GameBoard board)
    {
        super(name,maxHealth,attack,defense);
        this.playerLocation=getPlayerLocationFromBoard(board);
        this.experience=0;
        this.playerLevel=1;
        this.gameBoard=board;
        // arguments Like Range will be received in the specific PlayerTypes classes e.g:"mage"
    }
    private Point getPlayerLocationFromBoard(GameBoard board)
    {
        return board.getPlayerPosition();
    }
    public abstract void accept(UnitVisitor unitVisitor);

    public boolean isLegalMove(char directionKey)
    {
        int currentX=playerLocation.getX();
        int currentY=playerLocation.getY();
        GameTile[][] board=this.gameBoard.GetBoard();
        int legalX=this.gameBoard.GetBoard()[0].length-1;
        int legalY=this.gameBoard.GetBoard().length-1;

        switch (directionKey) {
            case 'w':
                if(playerLocation.getY()<=0||board[currentY-1][currentX].getType()=='#') return false;
                break;
            case 'a':
                if(playerLocation.getX()<=0||board[currentY][currentX-1].getType()=='#') return false;
                break;
            case 's':
                if(playerLocation.getY()>=legalY||board[currentY+1][currentX].getType()=='#') return false;
                break;
            case 'd':
                if(playerLocation.getX()>=legalX||board[currentY][currentX+1].getType()=='#') return false;
                break;
        }
        return true;
    }






    public void setPlayerLocation(Point playerLocation)
    {
        this.playerLocation=playerLocation;
        this.gameBoard.setPlayerPosition(playerLocation);
    }
    public Point getPlayerLocation()
    {
        return playerLocation;
    }
    public int getPlayerX()
    {
        return playerLocation.getX();
    }
    public int getPlayerY()
    {
        return playerLocation.getY();
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public GameBoard getPlayerBoard() {
        return gameBoard;
    }

    public void setPlayerBoard(GameBoard playerBoard) {
        this.gameBoard = playerBoard;
    }

}
