package UI;

import BoardLogic.GameBoard;

import java.io.IOException;
import java.util.Scanner;

public class UserInterface implements UserInterfaceCallback {

    private GameBoard gameBoard;
    private final Scanner scanner = new Scanner(System.in);
    private String input;
    private boolean gameOver;

    public UserInterface(String TXTFilePath) throws IOException {
        this.gameBoard = new GameBoard(TXTFilePath);
        this.gameOver = false;
    }

    public void startGame() {
        while (!gameOver) {
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
        System.out.println(gameBoard.toString());
        gameOver = true;
    }
}
