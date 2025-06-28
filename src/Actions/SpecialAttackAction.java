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

import java.util.ArrayList;
import java.util.Random;

public class SpecialAttackAction implements UnitVisitor {
    private GameBoard gameBoard;
    private Point playerLocation;

    public SpecialAttackAction(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.playerLocation = gameBoard.getPlayer().getLocation();
    }

    @Override
    public void visitWarrior(Warrior warrior) {
       if(warrior.getRemainingCooldown()==0){WarriorSpecialAttack(warrior);}
    }
    private void WarriorSpecialAttack(Warrior warrior)
    {
        GameTile[][] board= gameBoard.getBoard();
        warrior.setRemainingCooldown(warrior.getAbilityCooldown());
        ArrayList<GameTile> enemyList=AddEnemiesInRangeToList(warrior);
        if(enemyList.isEmpty())
        {
            return;
        }
        GameTile tile=GetRandomTile(enemyList);
        Unit enemyToAttack=tile.getUnit();
        int initialLevel= warrior.getLevel();
        enemyToAttack.accept(new AttackAction(warrior,gameBoard,'e'));
        int newLevel= warrior.getLevel();
        if(newLevel>initialLevel) HandleWarriorLevelUp(warrior);
        if(enemyToAttack.getHealth()<=0)
        {
            board[tile.getPosition().getY()][tile.getPosition().getX()]=new GameTile('.',null,tile.getPosition());
        }

    }
    private GameTile GetRandomTile(ArrayList<GameTile> l)
    {
        Random rnd=new Random();
        GameTile u=l.get(rnd.nextInt(l.size()));
        return  u;
    }
    private ArrayList<GameTile> AddEnemiesInRangeToList(Warrior warrior)
    {
        GameTile[][] board= gameBoard.getBoard();
        ArrayList<GameTile> enemyList=new ArrayList<>();
        for (int y = playerLocation.getY() - warrior.getSpecialAttackRange(); y <= playerLocation.getY() + warrior.getSpecialAttackRange(); y++)
        {
            for (int x = playerLocation.getX() - warrior.getSpecialAttackRange(); x <= playerLocation.getX() + warrior.getSpecialAttackRange(); x++)
            {
                if (gameBoard.isLegalTileLocationY(y) && gameBoard.isLegalTileLocationX(x) && board[y][x].getUnit() != null &&( x != playerLocation.getX() || y != playerLocation.getY()))
                {
                    if (playerLocation.distance(board[y][x].getPosition()) <= warrior.getSpecialAttackRange()) {
                       enemyList.add(board[y][x]);
                    }
                }
            }
        }
        return enemyList;
    }
    private void HandleWarriorLevelUp(Warrior warrior)
    {
        warrior.setRemainingCooldown(0);
        warrior.setMaxHealth(warrior.getMaxHealth()+(5* warrior.getLevel()));
        warrior.setHealth(warrior.getMaxHealth());
        warrior.setAttack(warrior.getAttack()+(2*warrior.getAttack()));
        warrior.setDefense(warrior.getDefense()+(2*warrior.getDefense()));
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
                            int initialLevel= rogue.getLevel();//For Handling the Rogue Specific Level Up****
                            enemy.accept(new AttackAction(rogue, gameBoard, 'e'));
                            int newLevel= rogue.getLevel();

                            if(enemy.getHealth()<=0)//Doing this because regular attack actions does not handle "long range" deaths
                            {
                                board[y][x]=new GameTile('.',null,new Point(x,y));
                                if(newLevel>initialLevel) HandleLevelUpRogue(rogue);
                            }
                        }
                    }
                }
            }
        }
    }
    private void HandleLevelUpRogue(Rogue rogue)
    {
        rogue.setCurrentEnergy(100);
        rogue.setAttack(rogue.getAttack()+(3*rogue.getLevel()));
    }



    @Override
    public void visitMonster(Monster monster) {

    }

    @Override
    public void visitTrap(Trap trap) {

    }




}
