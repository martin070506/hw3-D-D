package UI;

import BoardLogic.GameBoard;
import Player_Types.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class UserInterface implements UserInterfaceCallback {

    private GameBoard gameBoard;
    private Player player;
    private final Scanner scanner = new Scanner(System.in);
    private String input;
    private boolean gameOver;
    private File[] files;

    public UserInterface(String TXTFolderPath) throws IOException {
        File folder = new File(TXTFolderPath);
        File[] files = folder.listFiles();
        this.files = files;
        File firstFile = null;
        assert files != null;
        for (File f : files)
            if (f.isFile() && f.getName().toLowerCase().endsWith(".txt")) {
                firstFile = f;
                break;
            }

        if (firstFile == null)
            throw new IOException("No .txt files found in folder: " + TXTFolderPath);

        this.gameOver = false;
    }

    public UserInterface() {}

    public void startGame() throws IOException {
        player = new GameBoard(this).choosePlayer();
        for (File f : files)
            if (!gameOver)
                startLevel(f.getAbsolutePath());

        System.out.println(gameBoard.getPlayer());
        if (gameOver)
            System.out.println("Game Over.");
        else
            System.out.println("You Won.");
    }

    public void startLevel(String txtFilePath) throws IOException {
        gameBoard = new GameBoard(txtFilePath, player, this);
        System.out.println(gameBoard.getPlayer());
        System.out.println(gameBoard);
        while (!gameOver && gameBoard.getEnemyCount() > 0) {

            input = scanner.nextLine().trim().toLowerCase();
            gameBoard.nextTick(input);
        }
    }

    @Override
    public void update(String message) {
        System.out.print(message);
    }

    @Override
    public void endGame() {
        gameOver = true;
    }

    public GameBoard getGameBoard() { return gameBoard;}
    public void TestMethod(String FolderPath) throws IOException {
        File folder=new File(FolderPath);

        gameBoard=new GameBoard(Objects.requireNonNull(folder.listFiles())[0].getAbsolutePath(), this);
        System.out.println(gameBoard);
    }
}
