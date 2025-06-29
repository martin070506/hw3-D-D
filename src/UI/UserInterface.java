package UI;

import BoardLogic.GameBoard;

import java.io.IOException;
import java.util.Scanner;

public class UserInterface implements GameUpdateCallback {

    private GameBoard gameBoard;
    private final Scanner scanner = new Scanner(System.in);
    private String input;

    public UserInterface(String TXTFilePath) throws IOException {
        this.gameBoard = new GameBoard(TXTFilePath);
    }

    public void startGame() {
        while (true) {
            input = scanner.nextLine().trim().toLowerCase();
            gameBoard.nextTick(input);
            // TODO Add indicator for game complete
        }
    }


    @Override
    public void update(String message) {
        System.out.print(message);
    }

    @Override
    public void endGame() {
        // TODO Stop the game
    }
}
