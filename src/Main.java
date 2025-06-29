import Actions.MoveAction;
import BoardLogic.GameBoard;
import EnemyTypes.Trap;
import Player_Types.Player;
import Unit_Logic.Unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        Path tempFile;
        String TEST_MAP =
                "@.#\n" +
                        "###\n" +
                        "...";
        tempFile = Files.createTempFile("test_board", ".txt");
        Files.writeString(tempFile, TEST_MAP);
        GameBoard board=new GameBoard(tempFile.toString());
        Player p= board.getPlayer();
        p.accept(new MoveAction('d',board));
        System.out.println(board);
        p.accept(new MoveAction('a',board));
        System.out.println(board);
        p.accept(new MoveAction('a',board));
        System.out.println(board);
        Unit t=new Trap("bulbul",12,12);

    }
}