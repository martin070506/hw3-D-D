package BoardLogic;
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
        int length=(content.length()/height);
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
                if(currentXPos<=length&& currentYPos<=height)
                {
                    if(content.charAt(i)=='@')
                    {
                        setPlayerPosition(new Point(currentXPos,currentYPos));
                    }
                    newBoard[currentYPos][currentXPos] = new GameTile(content.charAt(i), currentXPos, currentYPos);
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
//        int currentX=playerPosition.getX();
//        int currentY=playerPosition.getY();
        this.playerPosition=position;
//        GameTile[][] gameBoard=this.GetBoard();
//        gameBoard[currentY][currentX]=new GameTile('.',currentX,currentY);
    }
    public Point getPlayerPosition()
    {
        return this.playerPosition;
    }

    public GameTile[][] GetBoard()
    {
        return this.Board;
    }


}
