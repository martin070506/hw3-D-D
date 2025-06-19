package Tests;

import BoardLogic.GameTile;
import BoardLogic.Point;
import Unit_Logic.Unit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTileTest {

    // Simple dummy Unit class for testing
    private static class TestUnit extends Unit {
        public TestUnit() {
            super("TestUnit", 1, 1, 1); // assuming Unit has a constructor like this
        }
    }

    @Test
    public void testTileCreationWithCoordinates() {
        GameTile tile = new GameTile('.', null, new Point(1, 2));
        assertEquals('.', tile.getType());
        assertEquals(1, tile.getPosition().getX());
        assertEquals(2, tile.getPosition().getY());
        assertNull(tile.getUnit());
    }

    @Test
    public void testTileCreationWithPoint() {
        Point point = new Point(5, 7);
        GameTile tile = new GameTile('#', null, point);
        assertEquals('#', tile.getType());
        assertEquals(5, tile.getPosition().getX());
        assertEquals(7, tile.getPosition().getY());
    }

    @Test
    public void testTileWithUnit() {
        TestUnit unit = new TestUnit();
        GameTile tile = new GameTile('@', unit, new Point(0, 0));
        assertEquals('@', tile.getType());
        assertNotNull(tile.getUnit());
        assertSame(unit, tile.getUnit());
    }

    @Test
    public void testSetPosition() {
        GameTile tile = new GameTile('.', null, new Point(0, 0));
        tile.setPosition(3, 4);
        assertEquals(3, tile.getPosition().getX());
        assertEquals(4, tile.getPosition().getY());

        // Partial update
        tile.setPosition(null, 10);
        assertEquals(3, tile.getPosition().getX());
        assertEquals(10, tile.getPosition().getY());
    }

    @Test
    public void testRangeCalculation() {
        GameTile tile = new GameTile('.', null, new Point(0, 0));
        Point p = new Point(3, 4);
        assertEquals(5.0, tile.range(p));
    }

    @Test
    public void testToStringOutput() {
        GameTile tile = new GameTile('.', null, new Point(2, 3));
        assertEquals(".(2,3)", tile.toString().replace(" ", ""));
    }
}
