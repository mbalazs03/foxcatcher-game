package foxcatcher.cli;

import foxcatcher.model.Position;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleGameTest {
    @Test
    void parseMove() {
        assertEquals(new Position(1, 1), ConsoleGame.parseMove("1 1"));
        assertEquals(new Position(6, 0), ConsoleGame.parseMove("6 0"));
        assertThrows(IllegalArgumentException.class, () -> ConsoleGame.parseMove("0"));
        assertThrows(IllegalArgumentException.class, () -> ConsoleGame.parseMove("0 0 0"));
    }
}