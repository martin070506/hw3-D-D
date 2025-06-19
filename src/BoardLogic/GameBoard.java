package BoardLogic;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class GameBoard {
    private GameTile[][] Board;
    private Point playerPosition;
    private Player player;

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
                        newBoard[currentYPos][currentXPos]=new GameTile(type,player,currentXPos,currentYPos);
                        player.setPlayerLocation(new Point(currentXPos,currentYPos));
                        this.playerPosition=new Point(currentXPos,currentYPos);
                    }
                    else
                    {
                        newBoard[currentYPos][currentXPos] = new GameTile(type, chooseUnitByType(type), currentXPos,currentYPos);
                    }

                    currentXPos++;

                }

            }
        }
        return newBoard;

    }
    public GameBoard(String TXTFilePath) throws IOException {
        Player chosenPlayer=choosePlayer();
        this.Board=BuildBoard(TXTFilePath,chosenPlayer);
        chosenPlayer.setPlayerBoard(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GameTile[] row : Board) {
            for (GameTile tile : row) {

                sb.append(tile.getType());

            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public void setPlayerPosition(Point position)
    {
       int currentX=playerPosition.getX();
       int currentY=playerPosition.getY();
        this.playerPosition=position;
      GameTile[][] gameBoard=this.GetBoard();
      gameBoard[currentY][currentX]=new GameTile('.',null,currentX,currentY);
      gameBoard[position.getY()][position.getX()]=new GameTile('@',this.player,position.getX(),position.getY());
    }
    public Point getPlayerPosition()
    {
        return this.playerPosition;
    }

    public GameTile[][] GetBoard()
    {
        return this.Board;
    }

    private Unit chooseUnitByType(char type){
        return switch (type) {
            // Monsters
//            case 's' -> new Monster("Lannister Solider", 3);
//            case 'k' -> new Monster("Lannister Knight", 4);
//            case 'q' -> new Monster("Queen's Guard", 5);
//            case 'z' -> new Monster("Wright", 3);
//            case 'b' -> new Monster("Bear-Wright", 4);
//            case 'g' -> new Monster("Giant-Wright", 5);
//            case 'w' -> new Monster("White Walker", 6);
//            case 'M' -> new Monster("The Mountain", 6);
//            case 'C' -> new Monster("Queen Cersei", 1);
//            case 'K' -> new Monster("Night's King", 8);
//
//            // Traps
//            case 'B' -> new Trap("Bonus Trap", 1, 5);
//            case 'Q' -> new Trap("Queen's Trap", 3, 7);
//            case 'D' -> new Trap("Death Trap", 1, 10);

            default -> null; //# means it is not a unit and either a Wall ('#') or Empty Tile ('.')

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

    //Only a method for Tests*****************************************
    public GameBoard(String TXTFilePath, Player chosenPlayer) throws IOException {
        this.Board = BuildBoard(TXTFilePath, chosenPlayer);
        this.playerPosition = chosenPlayer.getPlayerLocation();
        chosenPlayer.setPlayerBoard(this);
    }
    //Only a method for testing
    public static Player choosePlayer(String input)
    {
        return switch (input) {
            case "1" -> new Warrior();
            case "2" -> new Warrior();
            case "3" -> new Mage("Melisandre", 100,5,1,300,30,15,5,6);
            case "4" -> new Mage("Thoros of Myr", 250, 25, 4, 150, 20, 20, 3, 4 );
            case "5" -> new Rogue("Arya Stark", 150, 40, 2, 20);
            case "6" -> new Rogue("Bronn", 250, 35, 3, 50);
            default -> throw new IllegalArgumentException("Invalid input");
        };
    }


}
