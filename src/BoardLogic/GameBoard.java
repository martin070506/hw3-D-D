package BoardLogic;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GameBoard {
    private GameTile[][] Board;

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
                    newBoard[currentYPos][currentXPos] =
                            new GameTile(content.charAt(i), currentXPos, currentYPos);
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

    public GameTile[][] GetBoard()
    {
        return this.Board;
    }


}
