import Actions.MoveAction;
import BoardLogic.GameBoard;
import EnemyTypes.Trap;
import Player_Types.Player;
import UI.UserInterface;
import Unit_Logic.Unit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        UserInterface ui=new UserInterface("D:\\Learning\\Munhe Atzamim\\levels_dir");
       ui.startGame();


    }
}