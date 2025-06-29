package BoardLogic;
import Actions.MoveAction;
import Actions.SpecialAttackAction;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.Mage;
import Player_Types.Player;
import Player_Types.Rogue;
import Player_Types.Warrior;
import Unit_Logic.Unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

public class GameBoard {
    /// Fields
    private GameTile[][] board;
    private Player player;
    private int enemyCount;



    /// Constructors
    // default constructor where player is selected for first Time
    // it build the board and selects Player
    public GameBoard(String TXTFilePath) throws IOException {
        this.enemyCount=0;
        Player chosenPlayer=choosePlayer();
        this.player=chosenPlayer;
        this.board =BuildBoard(TXTFilePath,chosenPlayer);

    }

    // constructor for if player is already available,for example passing a level stays with the same Player
    // so the constructor builds a new board with a predefined player
    public GameBoard(String TXTFilePath,Player player) throws IOException
    {
        this.enemyCount=0;
        this.player=player;
        this.board=BuildBoard(TXTFilePath,player);
    }

    public GameBoard(Player player, int length, int width) {
        board = new GameTile[length][width];
        this.player = player;
        for (int i = 0; i < length; i++)
            for (int j = 0; j < width; j++)
                board[i][j] = new GameTile('.', null, new Point(i,j));
    }



    /// Methods
    public GameTile[][] BuildBoard(String TXTFilePath, Player player) throws IOException
    {
        this.player=player;
        String content = Files.readString(Paths.get(TXTFilePath));
        int height=1;
        for(int i=0;i<content.length();i++)
        {
            if(content.charAt(i)=='\n') height++;
        }
        int length=(content.length()/height);
        GameTile[][] newBoard=new GameTile[height][length];
        int currentYPos=0;
        int currentXPos=0;
        for(int i=0;i<content.length();i++)
        {
            char type=content.charAt(i);
            if(type=='\n') {
                currentYPos++;
                currentXPos=0;
            }
            else
            {
                if(currentXPos<=length&& currentYPos<=height)
                {
                    if(type=='@')
                    {
                        newBoard[currentYPos][currentXPos]=new GameTile(type,player,new Point(currentXPos, currentYPos));
                        player.setLocation(new Point(currentXPos,currentYPos));
                    }
                    else
                    {
                        if(chooseUnitByType(type)!=null && !Set.of('B','Q','D').contains(type)) this.enemyCount+=1;
                        newBoard[currentYPos][currentXPos] = new GameTile(type, chooseUnitByType(type), new Point(currentXPos, currentYPos));
                    }

                    currentXPos++;

                }

            }
        }
        return newBoard;
    }


    public void nextTick(){
        char input =getDirectionInput();
        if(input =='w' ||input =='a' ||input =='s' ||input =='d')  player.accept(new MoveAction(getDirectionInput(), this));
        else if(input=='e'){player.accept(new SpecialAttackAction(this));}
        else if(input=='q'){/*DO NOTHING*/}
        GameBoard newGameBoard = temporaryGameBoard(this);
        for (int i = 0; i < getHeight(); i++)
            for (int j = 0; j < getWidth(); j++)
                if (!Set.of('@', '#', '.', 'B', 'Q', 'D').contains(board[i][j].getType()))
                    board[i][j].getUnit().accept(new MoveAction(player, new Point(i, j), board[j][i].getType(), this, newGameBoard));

        this.board = newGameBoard.board;
    }

    //helper method
    private char getDirectionInput() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.print("Enter direction (w/a/s/d Or Special Attack 'e') (Do Nothing is 'q'): ");
            input = scanner.nextLine().trim().toLowerCase();

            if (input.length() == 1&&isLegalMove(input.charAt(0))) {
                char c = input.charAt(0);
                if (c == 'w' || c == 'a' || c == 's' || c == 'd'|| c=='q'||c=='e') {
                    return c;
                }
            }

            System.out.println("Invalid input. Please enter only w, a, s,d ,e or q");
        }
    }


    private GameBoard temporaryGameBoard(GameBoard gameBoard) {
        GameBoard newGameBoard = new GameBoard(gameBoard.player, gameBoard.getHeight(), gameBoard.getWidth());
        for (int i = 0; i < gameBoard.getHeight(); i++)
            for (int j = 0; j < gameBoard.getWidth(); j++)
                if (Set.of('@', '#', 'B', 'Q', 'D').contains(board[i][j].getType()))
                        newGameBoard.board[i][j] = gameBoard.board[i][j];

        return newGameBoard;
    }




    public boolean isLegalMove(char directionKey)
    {
        int currentX=player.getLocation().getX();
        int currentY=player.getLocation().getY();

        GameTile[][] board=this.getBoard();
        int legalX=this.getBoard()[0].length-1;
        int legalY=this.getBoard().length-1;

        switch (directionKey) {
            case 'w':
                if(currentY<=0||board[currentY-1][currentX].getType()=='#') return false;
                break;
            case 'a':
                if(currentX<=0||board[currentY][currentX-1].getType()=='#') return false;
                break;
            case 's':

                if(currentY>=legalY||board[currentY+1][currentX].getType()=='#') return false;
                break;
            case 'd':
                if(currentX>=legalX||board[currentY][currentX+1].getType()=='#') return false;
                break;
            default: return  false;
        }

        return true;
    }
    public boolean isLegalMoveAndUnitThere(char directionKey)
    {
        int currentX=player.getLocation().getX();
        int currentY=player.getLocation().getY();

        GameTile[][] board=this.getBoard();
        if(!isLegalMove(directionKey))return false;
        switch (directionKey) {
            case 'w':
                if(board[currentY-1][currentX].isUnit()) return true;
                break;
            case 'a':
                if(board[currentY][currentX-1].isUnit()) return true;
                break;
            case 's':
                if(board[currentY+1][currentX].isUnit()) return true;
                break;
            case 'd':
                if(board[currentY][currentX+1].isUnit()) return true;
                break;
        }
        return false;
    }




    public static Unit chooseUnitByType(char type){
        return switch (type) {
            // Monsters
            case 's' -> new Monster("Lannister Solider");
            case 'k' -> new Monster("Lannister Knight");
            case 'q' -> new Monster("Queen's Guard");
            case 'z' -> new Monster("Wright");
            case 'b' -> new Monster("Bear-Wright");
            case 'g' -> new Monster("Giant-Wright");
            case 'w' -> new Monster("White Walker");
            case 'M' -> new Monster("The Mountain");
            case 'C' -> new Monster("Queen Cersei");
            case 'K' -> new Monster("Night's King");

            // Traps
            case 'B' -> new Trap("Bonus Trap", 1, 5);
            case 'Q' -> new Trap("Queen's Trap", 3, 7);
            case 'D' -> new Trap("Death Trap", 1, 10);

            default -> null; // Wall or Empty
        };
    }

    private Player choosePlayer()
    {
        Scanner s = new Scanner(System.in);
        System.out.println("Choose a Player (1-6):");
        System.out.println("1 - Jon Snow      (Warrior)     | Health: 300, Attack: 30, Defense: 4, Cooldown: 3");
        System.out.println("2 - The Hound     (Warrior)     | Health: 400, Attack: 20, Defense: 6, Cooldown: 5");
        System.out.println("3 - Melisandre    (Mage)        | Health: 100, Attack: 5, Defense: 1, Mana: 300, Spell Power: 15, Range: 6");
        System.out.println("4 - Thoros of Myr (Mage)        | Health: 250, Attack: 25, Defense: 4, Mana: 150, Spell Power: 20, Range: 4");
        System.out.println("5 - Arya Stark    (Rogue)       | Health: 150, Attack: 40, Defense: 2, Cost: 20");
        System.out.println("6 - Bronn         (Rogue)       | Health: 250, Attack: 35, Defense: 3, Cost: 50");
        System.out.print("Enter your choice (1-6): ");
        String type=s.next();
        return switch (type) {
            case "1" -> choosePlayer("1");
            case "2" -> choosePlayer("2");
            case "3" -> choosePlayer("3");
            case "4" -> choosePlayer("4");
            case "5" -> choosePlayer("5");
            case "6" -> choosePlayer("6");
            default -> {
                System.out.println("Must Enter Number Between 1 and 6");
                yield choosePlayer();
            }
        };
    }



    public GameTile[][] getBoard()
    {
        return this.board;
    }

    public int getHeight(){
        return this.board.length;
    }

    public int getWidth(){
        return this.board[0].length;
    }
    public Player getPlayer()
    {
        return this.player;
    }
    public void setEnemyCount(int enemyCount)
    {
        this.enemyCount=enemyCount;
    }
    public int getEnemyCount(){return this.enemyCount;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GameTile[] row : board) {
            for (GameTile tile : row) {

                sb.append(tile.getType());

            }
            sb.append('\n');
        }
        return sb.toString();
    }
    public boolean isLegalTileLocationX(int x)
    {
        return (x>=0 && x<board[0].length);
    }
    public boolean isLegalTileLocationY(int y)
    {
        return (y>=0 && y<board.length);
    }



    public static Player choosePlayer(String input)
    {
        return switch (input) {
            case "1" -> new Warrior("Jon snow",300,30,3,3);
            case "2" -> new Warrior("The Hound",400,20,6,5);
            case "3" -> new Mage("Melisandre", 100,5,1,300,30,15,5,6);
            case "4" -> new Mage("Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4 );
            case "5" -> new Rogue("Arya Stark", 150, 40, 2, 20);
            case "6" -> new Rogue("Bronn", 250, 35, 3, 50);
            default -> throw new IllegalArgumentException("Invalid input");
        };
    }


}
