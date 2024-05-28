package foxcatcher.model;

import game.TwoPhaseMoveState;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents the state and logic of the FoxCatcher game.
 */
public class FoxCatcherGameModel implements TwoPhaseMoveState<Position> {

    /**
     * The size of the game board.
     */
    public static final int BOARD_SIZE = 8;
    private final ReadOnlyObjectWrapper<Field>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    private Player player = Player.PLAYER_1;
    private int turns = 0;
    private ZonedDateTime start, end;

    /**
     * Initializes a new game model with an empty board, a light piece at (0,2), and dark pieces at (7,1), (7,3), (7,5), and (7,7).
     */
    public FoxCatcherGameModel() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<>(Field.EMPTY);
            }
        }
        board[0][2] = new ReadOnlyObjectWrapper<>(Field.LIGHT);
        for (var i = 1; i < 8; i += 2) {
            board[7][i] = new ReadOnlyObjectWrapper<>(Field.DARK);
        }
    }

    /**
     * Checks if the move from the specified {@link Position} is legal.
     *
     * @param from The {@link Position} to move from.
     * @return True if the move is legal, false otherwise.
     */
    @Override
    public boolean isLegalToMoveFrom(Position from) {
        return isOnBoard(from) && !isEmpty(from) && canMoveWith(from) && !isGameOver();
    }

    /**
     * Checks if the move from the specified {@link Position} to another {@link Position} is legal.
     *
     * @param from The {@link Position} to move from.
     * @param to The {@link Position} to move to.
     * @return True if the move is legal, false otherwise.
     */
    @Override
    public boolean isLegalMove(Position from, Position to) {
        if (isFirstPlayer()) {
            return isLegalToMoveFrom(from) && isOnBoard(to) && isEmpty(to) && isFoxMove(from, to);
        } else
            return isLegalToMoveFrom(from) && isOnBoard(to) && isEmpty(to) && isHoundMove(from, to);
    }

    /**
     * Executes a move from one {@link Position} to another.
     * @param from The {@link Position} to move from.
     * @param to The {@link Position} to move to.
     */
    @Override
    public void makeMove(Position from, Position to) {
        setField(to, getField(from));
        setField(from, Field.EMPTY);
        addTurn();
        player = player.opponent();
    }

    /**
     * Gets the {@link game.State.Player} who should make the next move.
     *
     * @return The next player.
     */
    @Override
    public Player getNextPlayer() {
        return player = player.opponent();
    }

    /**
     * Checks if the game is over.
     *
     * @return True if the game is over, false otherwise.
     */
    @Override
    public boolean isGameOver()  {
        if (isFoxTrapped(getFoxPosition())) {
            return true;
        }
        return isFoxBehindHounds(getFoxPosition(), getHoundPositions());
    }

    /**
     * @return The current game {@link game.State.Status}.
     */
    @Override
    public Status getStatus() {
        if (!isGameOver()) {
            return Status.IN_PROGRESS;
        }
        return isGameOver() && isFoxBehindHounds(getFoxPosition(), getHoundPositions()) ? Status.PLAYER_1_WINS : Status.PLAYER_2_WINS;
    }

    /**
     * Checks if the specified {@link Position} is empty.
     *
     * @param p The {@link Position} to check.
     * @return True if the {@link Position} is empty, false otherwise.
     */
    public boolean isEmpty(Position p) {
        return getField(p) == Field.EMPTY;
    }

    /**
     * Gets the {@link Field} value at the specified {@link Position}.
     *
     * @param p The {@link Position} to get the {@link Field} value from.
     * @return The {@link Field} value at the specified {@link Position}.
     */
    public Field getField(Position p) {
        return board[p.row()][p.col()].get();
    }

    private void setField(Position p, Field field) {
        board[p.row()][p.col()].set(field);
    }

    private Position getFoxPosition() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (getField(new Position(i, j)) == Field.LIGHT) {
                    return new Position(i, j);
                }
            }
        }
        throw new IllegalStateException("Fox not found");
    }

    private List<Position> getHoundPositions() {
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (getField(new Position(i, j)) == Field.DARK) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }


    private static boolean isOnBoard(Position p) {
        return 0 <=p.row() && p.row() < BOARD_SIZE && 0 <= p.col() && p.col() < BOARD_SIZE;
    }

    private static boolean isFoxMove(Position from, Position to) {
        var dx = Math.abs(to.row() - from.row());
        var dy = Math.abs(to.col() - from.col());
        return dx * dy == 1;
    }

    private static boolean isHoundMove(Position from, Position to) {
        var dx = to.row() - from.row();
        var dy = Math.abs(to.col() - from.col());
        if (dx < 0) {
            return Math.abs(dx * dy) == 1;
        } else {
            return false;
        }
    }

    private boolean isFirstPlayer() {
        return player == Player.PLAYER_1;
    }

    private boolean canMoveWith(Position from) {
        Field field = getField(from);
        return (isFirstPlayer() && field == Field.LIGHT) || (!isFirstPlayer() && field == Field.DARK);
    }

    private boolean isFoxTrapped(Position foxPosition) {
        int moves = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                Position newPosition = new Position(foxPosition.row() + i, foxPosition.col() + j);
                if (isOnBoard(newPosition) && isFoxMove(foxPosition, newPosition) && isEmpty(newPosition)) {
                    moves++;
                }
            }
        }
        return moves == 0;
    }

    private boolean isFoxBehindHounds(Position foxPosition, List<Position> houndPositions) {
        int hounds = 0;
        for (Position houndPosition : houndPositions) {
            if (houndPosition.row() < foxPosition.row()) {
                hounds += 1;
            }
        }
        return hounds == 4;
    }

    /**
     * Gets a {@link ReadOnlyObjectProperty} for the {@link Field} at the specified coordinates.
     * @param i The row index.
     * @param j The column index.
     * @return A {@link ReadOnlyObjectProperty} for the {@link Field} at the specified coordinates.
     */
    public ReadOnlyObjectProperty<Field> fieldProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    private void addTurn() {
        turns += 1;
    }

    /**
     * Gets the number of turns taken so far.
     * @return The number of turns.
     */
    public int getTurns() {
        return turns;
    }

    /**
     * Records the start time of the game.
     */
    public void startGameTime() {
        this.start = ZonedDateTime.now();
    }

    /**
     * Records the end time of the game.
     */
    public void endGameTime() {
        this.end = ZonedDateTime.now();
    }

    /**
     * Gets the duration of the game in seconds.
     * @return The duration of the game in seconds.
     */
    public long getGameDurationInSeconds() {
        if (start != null && end != null) {
            return Duration.between(start, end).getSeconds();
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoxCatcherGameModel that = (FoxCatcherGameModel) o;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j].get() != that.board[i][j].get()) {
                    return false;
                }
            }
        }
        return player == that.player;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(player);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }

    @Override
    public String toString() {
        var stringBuilder= new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                switch (board[i][j].get()) {
                    case DARK -> stringBuilder.append(" D ");
                    case LIGHT -> stringBuilder.append(" F ");
                    case EMPTY -> stringBuilder.append(" _ ");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
