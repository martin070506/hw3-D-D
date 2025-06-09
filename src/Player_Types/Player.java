package Player_Types;

import BoardLogic.GameBoard;
import BoardLogic.Point;
import Unit_Logic.Unit;
import jdk.jshell.spi.ExecutionControl;

public class Player extends Unit {

    private Point playerLocation;
    private int experience;
    private int playerLevel;
    private int rangeOfAttack;

    public Player(String name, int maxHealth, int attack, int defense,GameBoard board)
    {
        super(name,maxHealth,attack,defense);
        this.playerLocation=getPlayerLocationFromBoard(board);
        this.experience=0;
        this.playerLevel=1;
        // arguments Like Range will be received in the specific PlayerTypes classes e.g:"mage"
    }
    public void walk(char directionKey) throws Exception {
        throw new Exception("Not implemented");
    }






    private Point getPlayerLocationFromBoard(GameBoard board)
    {
        return board.getPlayerPosition();
    }
}
