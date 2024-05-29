package foxcatcher.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testToString() {
        Position pos = new Position(1, 2);
        assertEquals("(1,2)", pos.toString());
    }

    @Test
    void row() {
        Position pos = new Position(3, 4);
        assertEquals(3, pos.row());
    }

    @Test
    void col() {
        Position pos = new Position(3, 4);
        assertEquals(4, pos.col());
    }
}