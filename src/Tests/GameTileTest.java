package BoardLogic;

import EnemyTypes.Trap;
import Unit_Logic.Unit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTileTest {

    @Test
    void testConstructionAndGetters() {
        Point p = new Point(2, 3);
        GameTile tile = new GameTile('#', null, p);

        assertEquals('#', tile.getType());
        assertEquals(p, tile.getPosition());
        assertNull(tile.getUnit());
    }

    @Test
    void testSetters() {
        Point p = new Point(1, 1);
        GameTile tile = new GameTile('.', null, p);

        tile.setType('@');
        tile.setPosition(4, 5);
        assertEquals('@', tile.getType());
        assertEquals(4, tile.getPosition().getX());
        assertEquals(5, tile.getPosition().getY());

        Point newP = new Point(7,8);
        tile.setPosition(newP);
        assertEquals(newP, tile.getPosition());
    }

    @Test
    void testIsUnit() {
        GameTile emptyTile = new GameTile('.', null, new Point(0,0));
        assertFalse(emptyTile.isUnit());

        Unit mockUnit = new Trap("Test Trap");
        GameTile unitTile = new GameTile('B', mockUnit, new Point(0,0));
        assertTrue(unitTile.isUnit());
    }

    @Test
    void testRange() {
        Point p = new Point(2,3);
        GameTile tile = new GameTile('.', null, p);

        assertEquals(Math.sqrt(5), tile.range(new Point(0,2)), 0.001);
    }


}
