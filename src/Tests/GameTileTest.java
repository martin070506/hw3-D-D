package Tests;

import BoardLogic.GameTile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTileTest {

    @Test
    public void testConstructorAndGetters() {
        GameTile tile = new GameTile('X', 3, 5);
        assertEquals('X', tile.getType());
        assertEquals(3, tile.getPosition().getX());
        assertEquals(5, tile.getPosition().getY());
    }

    @Test
    public void testToString() {
        GameTile tile = new GameTile('A', 0, 0);
        assertEquals("A (0, 0)", tile.toString());
    }

    @Test
    public void testRangeBetweenTiles() {
        GameTile t1 = new GameTile('A', 0, 0);
        GameTile t2 = new GameTile('B', 3, 4);
        assertEquals(5, t1.range(t2.getPosition())); // 3-4-5 triangle
    }
}
