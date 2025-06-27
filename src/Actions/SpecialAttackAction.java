package Actions;

import BoardLogic.GameBoard;
import BoardLogic.GameTile;
import BoardLogic.Point;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Player;
import Player_Types.Rogue;
import Player_Types.Warrior;
import Unit_Logic.Unit;
import Unit_Logic.UnitVisitor;

public class SpecialAttackAction implements UnitVisitor {
    private GameBoard gameBoard;
    private Point playerLocation;

    public SpecialAttackAction(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.playerLocation = gameBoard.getPlayer().getLocation();
    }

    @Override
    public void visitWarrior(Warrior warrior) {
        //TODO IMPLEMENT
    }
    private void WarriorSpecialAttack(Warrior warrior)
    {
        //TODO IMPLEMENT
    }
    private void HandleLevelUpWarrior(int firstLevel,int secondLevel,Warrior warrior)
    {
        //TODO IMPLEMENT
    }

    @Override
    public void visitMage(Mage mage) {
        //TODO IMPLEMENT
    }
    private void MageSpecialAttack(Mage mage)
    {
        //TODO IMPLEMENT
    }
    private void HandleLevelUpMage(int firstLevel,int secondLevel,Mage mage)
    {
        //TODO IMPLEMENT
    }


    @Override
    public void visitRogue(Rogue rogue) {
        RogueSpecialAttack(rogue);
    }
    private void RogueSpecialAttack(Rogue rogue) {
        if (rogue.getCurrentEnergy() >= rogue.getAbilityCost())
        {
            GameTile[][] board = gameBoard.getBoard();
            rogue.setCurrentEnergy(rogue.getCurrentEnergy() - rogue.getAbilityCost());
            for (int y = playerLocation.getY() - rogue.getAttackRange(); y <= playerLocation.getY() + rogue.getAttackRange(); y++) {
                for (int x = playerLocation.getX() - rogue.getAttackRange(); x <= playerLocation.getX() + rogue.getAttackRange(); x++)
                {
                    if (gameBoard.isLegalTileLocationY(y) && gameBoard.isLegalTileLocationX(x) && board[y][x].getUnit() != null &&( x != playerLocation.getX() || y != playerLocation.getY()))
                    {
                        if (playerLocation.distance(board[y][x].getPosition()) <= rogue.getAttackRange()) {
                            Unit enemy = board[y][x].getUnit();
                            int currentLevel=rogue.getLevel();
                            enemy.accept(new AttackAction(rogue, gameBoard, 'e'));
                            int newLevel=rogue.getLevel();
                            if(enemy.getHealth()<=0)//Doing this because regular attack actions does not handle "long range" deaths
                            {
                                board[y][x]=new GameTile('.',null,new Point(x,y));
                                HandleLevelUpRogue(currentLevel,newLevel,rogue);
                            }
                        }
                    }
                }
            }
        }
    }
    private void HandleLevelUpRogue(int firstLevel,int secondLevel,Rogue rogue)
    {
        if(secondLevel>firstLevel)
        {
            rogue.setCurrentEnergy(100);
            rogue.setAttack(rogue.getAttack()+(3*rogue.getLevel()));
        }
    }



    @Override
    public void visitMonster(Monster monster) {

    }

    @Override
    public void visitTrap(Trap trap) {

    }




}
