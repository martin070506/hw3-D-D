package BoardLogic;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Unit_Logic.Unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameBoard {
    private GameTile[][] Board;
    private Point playerPosition;

    public GameTile[][] BuildBoard(String TXTFilePath) throws IOException
    {

        String content = Files.readString(Paths.get(TXTFilePath));
        int height=1;
        for(int i=0;i<content.length();i++)
        {
            if(content.charAt(i)=='\n') height++;
        }
        int length=(content.length()/height)-1;
        GameTile[][] newBoard=new GameTile[height][length];
        int currentYPos=0;
        int currentXPos=0;
        for(int i=0;i<content.length();i++)
        {
            if(content.charAt(i)=='\n') {
                currentYPos++;
                currentXPos=0;
            }
            else
            {
                if(currentXPos<=48 && currentYPos<=18)
                {
                    if(content.charAt(i)=='@')
                        /// can you change all content.charAt(i) to char type int the start of the code ?
                    {
                        setPlayerPosition(new Point(currentXPos,currentYPos));
                    }
                    newBoard[currentYPos][currentXPos] =
                            new GameTile(content.charAt(i), chooseUnitByType(content.charAt(i)), currentXPos, currentYPos);
                    currentXPos++;

                }

            }
        }
        return newBoard;
    }

    public GameBoard(String TXTFilePath) throws IOException {
        this.Board=BuildBoard(TXTFilePath);
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
        this.playerPosition=position;
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
            case 's' -> new Monster("Lannister Solider", 3);
            case 'k' -> new Monster("Lannister Knight", 4);
            case 'q' -> new Monster("Queen's Guard", 5);
            case 'z' -> new Monster("Wright", 3);
            case 'b' -> new Monster("Bear-Wright", 4);
            case 'g' -> new Monster("Giant-Wright", 5);
            case 'w' -> new Monster("White Walker", 6);
            case 'M' -> new Monster("The Mountain", 6);
            case 'C' -> new Monster("Queen Cersei", 1);
            case 'K' -> new Monster("Night's King", 8);

            // Traps
            case 'B' -> new Trap("Bonus Trap", 1, 5);
            case 'Q' -> new Trap("Queen's Trap", 3, 7);
            case 'D' -> new Trap("Death Trap", 1, 10);

            default -> throw new IllegalArgumentException("Unknown Unit: " + type);
        };
    }
}
