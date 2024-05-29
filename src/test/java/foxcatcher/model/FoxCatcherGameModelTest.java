package foxcatcher.model;

import game.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoxCatcherGameModelTest {

    private FoxCatcherGameModel model;

    @BeforeEach
    public void setUp() {
        model = new FoxCatcherGameModel();
    }

    private void foxLose(FoxCatcherGameModel model) {
        model.makeMove(new Position(0,2), new Position(1,1));
        model.makeMove(new Position(7,1), new Position(6,0));
        model.makeMove(new Position(1,1), new Position(0,0));
        model.makeMove(new Position(6,0), new Position(5,1));
        model.makeMove(new Position(0,0), new Position(1,1));
        model.makeMove(new Position(5,1), new Position(4,0));
        model.makeMove(new Position(1,1), new Position(0,0));
        model.makeMove(new Position(4,0), new Position(3,1));
        model.makeMove(new Position(0,0), new Position(1,1));
        model.makeMove(new Position(3,1), new Position(2,0));
        model.makeMove(new Position(1,1), new Position(0,0));
        model.makeMove(new Position(2,0), new Position(1,1));
    }

    private void houndLose(FoxCatcherGameModel model) {
        model.makeMove(new Position(0,2), new Position(1,1));
        model.makeMove(new Position(7,3), new Position(6,2));
        model.makeMove(new Position(1,1), new Position(2,2));
        model.makeMove(new Position(6,2), new Position(5,3));
        model.makeMove(new Position(2,2), new Position(3,3));
        model.makeMove(new Position(5,3), new Position(4,2));
        model.makeMove(new Position(3,3), new Position(4,4));
        model.makeMove(new Position(4,2), new Position(3,3));
        model.makeMove(new Position(4,4), new Position(5,3));
        model.makeMove(new Position(7,1), new Position(6,0));
        model.makeMove(new Position(5,3), new Position(6,2));
        model.makeMove(new Position(7,5), new Position(6,4));
        model.makeMove(new Position(6,2), new Position(7,3));
        model.makeMove(new Position(7,7), new Position(6,6));
    }

    @Test
    void isLegalToMoveFromP1() {
        assertTrue(model.isLegalToMoveFrom(new Position(0, 2)));
        assertFalse(model.isLegalToMoveFrom(new Position(1,1)));
        assertFalse(model.isLegalToMoveFrom(new Position(-1, 1)));
        assertFalse(model.isLegalToMoveFrom(new Position(1, -1)));
    }

    @Test
    void isLegalToMoveFromP2() {
        model.makeMove(new Position(0,2), new Position(1, 1));
        assertTrue(model.isLegalToMoveFrom(new Position(7,1)));
        assertTrue(model.isLegalToMoveFrom(new Position(7,3)));
        assertTrue(model.isLegalToMoveFrom(new Position(7,5)));
        assertTrue(model.isLegalToMoveFrom(new Position(7,7)));
        assertFalse(model.isLegalToMoveFrom(new Position(4,5)));
        assertFalse(model.isLegalToMoveFrom(new Position(-6,4)));
        assertFalse(model.isLegalToMoveFrom(new Position(0,0)));
        assertFalse(model.isLegalToMoveFrom(new Position(1,1)));
    }

    @Test
    void isLegalMoveP1() {
        model.makeMove(new Position(0, 2), new Position(1,3));
        model.makeMove(new Position(7,1), new Position(6,2));
        Position from =new Position(1, 3);
        assertTrue(model.isLegalMove(from, new Position(0,2)));
        assertTrue(model.isLegalMove(from, new Position(0, 4)));
        assertTrue(model.isLegalMove(from, new Position(2, 2)));
        assertTrue(model.isLegalMove(from, new Position(2, 4)));
        assertFalse(model.isLegalMove(from, new Position(0, 0)));
        assertFalse(model.isLegalMove(from, new Position(7, 7)));
        assertFalse(model.isLegalMove(from, new Position(-1, 1)));
        assertFalse(model.isLegalMove(from, new Position(1, -1)));
    }

    @Test
    void isLegalMoveP2() {
        model.makeMove(new Position(0, 2), new Position(1,3));
        Position from =new Position(7, 1);
        assertTrue(model.isLegalMove(from, new Position(6,0)));
        assertTrue(model.isLegalMove(from, new Position(6, 2)));
        assertFalse(model.isLegalMove(from, new Position(0,0)));
        assertFalse(model.isLegalMove(from, new Position(-1,0)));
        assertFalse(model.isLegalMove(from, new Position(6,1)));
        assertFalse(model.isLegalMove(from, new Position(7,-1)));
    }

    @Test
    void makeMove() {
        Position foxPosition = new Position(0, 2);
        Position newFoxPosition = new Position(1, 3);
        model.makeMove(foxPosition, newFoxPosition);
        assertEquals(Field.EMPTY, model.getField(foxPosition));
        assertEquals(Field.LIGHT, model.getField(newFoxPosition));
        assertEquals(State.Player.PLAYER_2, model.getNextPlayer());
    }

    @Test
    void getNextPlayer() {
        assertEquals(State.Player.PLAYER_1, model.getNextPlayer());
        model.makeMove(new Position(0, 2), new Position(1, 3));
        assertEquals(State.Player.PLAYER_2, model.getNextPlayer());
    }

    @Test
    void isGameOver() {
        assertFalse(model.isGameOver());
        foxLose(model);
        assertTrue(model.isGameOver());
        assertEquals(State.Status.PLAYER_2_WINS, model.getStatus());
    }

    @Test
    void getStatus() {
        assertEquals(State.Status.IN_PROGRESS, model.getStatus());
        foxLose(model);
        assertEquals(State.Status.PLAYER_2_WINS, model.getStatus());
        model = new FoxCatcherGameModel();
        houndLose(model);
        assertEquals(State.Status.PLAYER_1_WINS, model.getStatus());
    }

    @Test
    void isEmpty() {
        assertTrue(model.isEmpty(new Position(4, 4)));
        assertFalse(model.isEmpty(new Position(0, 2)));
    }

    @Test
    void getField() {
        assertEquals(Field.LIGHT, model.getField(new Position(0, 2)));
        assertEquals(Field.DARK, model.getField(new Position(7, 1)));
        assertEquals(Field.EMPTY, model.getField(new Position(4, 4)));
    }

    @Test
    void fieldProperty() {
        assertNotNull(model.fieldProperty(0, 2));
        assertEquals(Field.LIGHT, model.fieldProperty(0, 2).get());
        assertNotNull(model.fieldProperty(7, 1));
        assertEquals(Field.DARK, model.fieldProperty(7, 1).get());
    }

    @Test
    void getTurns() {
        assertEquals(0, model.getTurns());
        model.makeMove(new Position(0, 2), new Position(1, 3));
        assertEquals(1, model.getTurns());
        model.makeMove(new Position(7, 1), new Position(6, 2));
        assertEquals(2, model.getTurns());
    }

    @Test
    void startGameTime() {
        model.startGameTime();
        assertNotNull(model.start);
    }

    @Test
    void endGameTime() {
        model.startGameTime();
        model.endGameTime();
        assertNotNull(model.end);
    }

    @Test
    void getGameDurationInSeconds() {
        model.startGameTime();
        try {
            Thread.sleep(1000); // Simulate a 1-second game duration
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        model.endGameTime();
        long duration = model.getGameDurationInSeconds();
        assertEquals(1, duration);
    }

    @Test
    void testEquals() {
        FoxCatcherGameModel model2 = new FoxCatcherGameModel();
        assertEquals(model, model2);
        model.makeMove(new Position(0, 2), new Position(1, 3));
        assertNotEquals(model, model2);
    }

    @Test
    void testHashCode() {
        FoxCatcherGameModel model2 = new FoxCatcherGameModel();
        houndLose(model2);
        assertEquals(State.Status.PLAYER_1_WINS, model2.getStatus());
    }

    @Test
    void testToString() {
        String expectedBoard =
                """
                         _  _  F  _  _  _  _  _\s
                         _  _  _  _  _  _  _  _\s
                         _  _  _  _  _  _  _  _\s
                         _  _  _  _  _  _  _  _\s
                         _  _  _  _  _  _  _  _\s
                         _  _  _  _  _  _  _  _\s
                         _  _  _  _  _  _  _  _\s
                         _  D  _  D  _  D  _  D\s
                        """;
        assertEquals(expectedBoard, model.toString());
    }
}