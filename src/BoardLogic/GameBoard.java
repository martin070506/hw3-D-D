package BoardLogic;
import Actions.AttackAction;
import Actions.MoveAction;
import EnemyTypes.Enemy;
import EnemyTypes.Monster;
import EnemyTypes.Trap;
import Player_Types.*;
import UI.UserInterfaceCallback;
import Unit_Logic.Unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class GameBoard implements GameBoardCallback {
    /// Fields
    private GameTile[][] board;
    private final Player player;
    private int enemyCount;
    private final UserInterfaceCallback callback;



    /// Constructors
    // default constructor where player is selected for first Time
    // it build the board and selects Player
    public GameBoard(String TXTFilePath, UserInterfaceCallback UI) throws IOException {
        callback = UI;
        enemyCount = 0;
        Player chosenPlayer = choosePlayer();
        player = chosenPlayer;
        player.setCallback(this);
        board = BuildBoard(TXTFilePath, chosenPlayer);
    }

    // constructor for if player is already available,for example passing a level stays with the same
    // Player so the constructor builds a new board with a predefined player
    public GameBoard(String TXTFilePath, Player player, UserInterfaceCallback UI) throws IOException
    {
        callback = UI;
        enemyCount = 0;
        this.player = player;
        player.setCallback(this);
        board = BuildBoard(TXTFilePath, player);
    }

    public GameBoard(Player player, int length, int width, UserInterfaceCallback UI) {
        callback = UI;
        board = new GameTile[length][width];
        this.player = player;
        player.setCallback(this);
        for (int y = 0; y < length; y++)
            for (int x = 0; x < width; x++)
                board[y][x] = new GameTile('.', null, new Point(x,y));
    }

    public GameBoard(UserInterfaceCallback UI){
        callback = UI;
        player = null;
    }




    /// Methods
    // Getters
    public GameTile[][] getBoard() { return this.board; }
    public Player getPlayer() { return this.player; }
    public int getEnemyCount() { return this.enemyCount; }
    public UserInterfaceCallback getCallback() { return callback; }

    // Setters
    public void setEnemyCount(int enemyCount) { this.enemyCount = enemyCount; }

    // Override Methods
    @Override
    public void endGame() {
        board[player.getLocation().getY()][player.getLocation().getX()].setType('X');
        callback.endGame();
    }

    @Override
    public ArrayList<GameTile> getTileEnemiesInRange(Point point, int distance){
        ArrayList<GameTile> enemyTileList = new ArrayList<>();
        for (int y = Math.max(point.getY() - distance, 0);
             y <= Math.min(point.getY() + distance, getHeight() - 1); y++)
            for (int x = Math.max(point.getX() - distance, 0);
                 x <= Math.min(point.getX() + distance, getWidth() - 1); x++)
                if (board[y][x].getUnit() != null &&
                        !point.equals(new Point(x,y)) &&
                        point.distance(new Point(x,y)) <= distance)
                    enemyTileList.add(board[y][x]);

        return enemyTileList;
    }


    @Override
    public AttackAction playerAttack(Player player, char direction, Point loaction){
        return new AttackAction(player, this, direction, loaction);
    }

    @Override
    public void enemyAttack(Enemy enemy, Point location) {
        player.accept(new AttackAction(enemy, location, callback), false);
    }

    @Override
    public void update(String message) { callback.update(message); }

    // Other Methods
    public void nextTick(String input){
        if (input.length() != 1)
            return;
        callback.update("Enemies Left:" + getEnemyCount()+ '\n');

        switch (input.charAt(0)) {
            case 'w':
            case 'a':
            case 's':
            case 'd':
                player.accept(new MoveAction(input.charAt(0), this), false);
                break;
            case 'e':
                player.castAbility(null);
                break;
            case 'q':
                break;
            default:
                return;
        }
        callback.update(player.toString() + '\n');
        GameBoard newGameBoard = temporaryGameBoard(this);
        int counter=0;
        for (int y = 0; y < getHeight(); y++)
        {
            for (int x = 0; x < getWidth(); x++)
            {
                if (!Set.of('@', '#', '.').contains(board[y][x].getType()) && board[y][x].getUnit() != null)
                {
                    board[y][x].getUnit().accept(new MoveAction(player, new Point(x,y), board[y][x].getType(), this, newGameBoard), false);
                    counter++;
                }


            }
        }
         this.setEnemyCount(counter);

        board = newGameBoard.board;
        callback.update(toString());

    }

    public GameTile[][] BuildBoard(String TXTFilePath, Player player) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(TXTFilePath));
        int height = lines.size();
        int width = lines.getFirst().length();  // since you guarantee all lines same length

        GameTile[][] newBoard = new GameTile[height][width];

        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                char type = line.charAt(x);
                if (type == '@') {
                    newBoard[y][x] = new GameTile(type, player, new Point(x,y));
                    player.setLocation(new Point(x,y));
                }
                else {
                    if (chooseUnitByType(type) != null)
                        enemyCount += 1;
                    newBoard[y][x] = new GameTile(type, chooseUnitByType(type), new Point(x,y));
                }
            }
        }

        return newBoard;
    }


    private GameBoard temporaryGameBoard(GameBoard gameBoard) {
        GameBoard newGameBoard = new GameBoard(gameBoard.player, gameBoard.getHeight(),
                gameBoard.getWidth(), callback);
        for (int y = 0; y < gameBoard.getHeight(); y++)
            for (int x = 0; x < gameBoard.getWidth(); x++)
                if (board[y][x] != null &&
                        Set.of('@', '#', 'B', 'Q', 'D').contains(board[y][x].getType()))
                        newGameBoard.board[y][x] = gameBoard.board[y][x];

        return newGameBoard;
    }

    public boolean isLegalMove(char directionKey)
    {
        int currentX = player.getLocation().getX();
        int currentY = player.getLocation().getY();

        GameTile[][] board = getBoard();
        int legalX = getWidth() - 1;
        int legalY = getHeight() - 1;

        switch (directionKey) {
            case 'w':
                if (currentY <= 0 || board[currentY - 1][currentX].getType() == '#')
                    return false;
                break;
            case 'a':
                if (currentX <= 0 || board[currentY][currentX - 1].getType() == '#')
                    return false;
                break;
            case 's':
                if (currentY >= legalY || board[currentY + 1][currentX].getType() == '#')
                    return false;
                break;
            case 'd':
                if (currentX >= legalX || board[currentY][currentX + 1].getType() == '#')
                    return false;
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean isLegalMoveAndUnitThere(char directionKey)
    {
        int currentX = player.getLocation().getX();
        int currentY = player.getLocation().getY();

        GameTile[][] board = getBoard();
        if (!isLegalMove(directionKey))
            return false;
        switch (directionKey) {
            case 'w':
                if (board[currentY - 1][currentX].isUnit())
                    return true;
                break;
            case 'a':
                if (board[currentY][currentX - 1].isUnit())
                    return true;
                break;
            case 's':
                if (board[currentY + 1][currentX].isUnit())
                    return true;
                break;
            case 'd':
                if (board[currentY][currentX + 1].isUnit())
                    return true;
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
            case 'B' -> new Trap("Bonus Trap");
            case 'Q' -> new Trap("Queen's Trap");
            case 'D' -> new Trap("Death Trap");

            default -> null; // Wall or Empty
        };
    }

    public Player choosePlayer()
    {
        Scanner s = new Scanner(System.in);
        callback.update("Select Player :\n");
        callback.update("1 - Jon Snow      (Warrior)  | Health: 300, Attack: 30, Defense: 4, Cooldown: 3\n");
        callback.update("2 - The Hound     (Warrior)  | Health: 400, Attack: 20, Defense: 6, Cooldown: 5\n");
        callback.update("3 - Melisandre    (Mage)     | Health: 100, Attack:  5, Defense: 1, Mana: 300, Spell Power: 15, Range: 6\n");
        callback.update("4 - Thoros of Myr (Mage)     | Health: 250, Attack: 25, Defense: 4, Mana: 150, Spell Power: 20, Range: 4\n");
        callback.update("5 - Arya Stark    (Rogue)    | Health: 150, Attack: 40, Defense: 2, Cost: 20\n");
        callback.update("6 - Bronn         (Rogue)    | Health: 250, Attack: 35, Defense: 3, Cost: 50\n");
        callback.update("7 - Ygritte       (Hunter)   | Health: 220, Attack: 30, Defense: 2, Range: 6\n");
        String type = s.next();
        return switch (type) {
            case "1" -> choosePlayer("1");
            case "2" -> choosePlayer("2");
            case "3" -> choosePlayer("3");
            case "4" -> choosePlayer("4");
            case "5" -> choosePlayer("5");
            case "6" -> choosePlayer("6");
            case "7" -> choosePlayer("7");
            default -> choosePlayer(); // That the way they did in their example
        };
    }

    private Player choosePlayer(String input)
    {
        return switch (input) {
            case "1" -> new Warrior("Jon Snow");
            case "2" -> new Warrior("The Hound");
            case "3" -> new Mage("Melisandre");
            case "4" -> new Mage("Thoros of Myr");
            case "5" -> new Rogue("Arya Stark");
            case "6" -> new Rogue("Bronn");
            case "7" -> new Hunter("Ygritte");
            default -> throw new IllegalArgumentException("Invalid input");
        };
    }

    public int getHeight(){ return this.board.length; }
    public int getWidth(){ return this.board[0].length; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GameTile[] row : board) {
            for (GameTile tile : row)
                if(tile != null)
                    sb.append(tile);
            sb.append('\n');
        }
        return sb.toString();
    }
}
