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
        int Length=(content.length()/height)-1;
        GameTile[][] NewBoard=new GameTile[height][Length];
        int CurrentYPos=0;
        int CurrentXPos=0;
        for(int i=0;i<content.length();i++)
        {
            if(content.charAt(i)=='\n') {
                CurrentYPos++;
                CurrentXPos=0;
            }
            else
            {
                if(CurrentXPos<=48 && CurrentYPos<=18)
                {
                    NewBoard[CurrentYPos][CurrentXPos]=new GameTile(content.charAt(i));
                    CurrentXPos++;
                }

            }
        }
        return NewBoard;

    }
    public GameBoard(String TXTFilePath) throws IOException {
        this.Board=BuildBoard(TXTFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GameTile[] row : Board) {
            for (GameTile tile : row) {

                sb.append(tile.GetTile());

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
