import BoardLogic.GameBoard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        

        String folderPath = args[0];
        String content = Files.readString(Paths.get(folderPath));
        GameBoard b=new GameBoard(folderPath);
        System.out.println(b);
        System.out.println("**************");
        System.out.println(b.GetBoard()[0][48]==null);

    }
}