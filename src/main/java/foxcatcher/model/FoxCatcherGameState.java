package foxcatcher.model;

import game.TwoPhaseMoveState;
import javafx.beans.property.ReadOnlyObjectWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FoxCatcherGameState implements TwoPhaseMoveState<Position> {

    public static final int BOARD_SIZE = 8;
    private final ReadOnlyObjectWrapper<Field>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    private Player player;

    public FoxCatcherGameState() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<>(Field.EMPTY);
            }
        }
        board[0][2] = new ReadOnlyObjectWrapper<>(Field.LIGHT);
        board[7][1] = new ReadOnlyObjectWrapper<>(Field.DARK);
        board[7][3] = new ReadOnlyObjectWrapper<>(Field.DARK);
        board[7][5] = new ReadOnlyObjectWrapper<>(Field.DARK);
        board[7][7] = new ReadOnlyObjectWrapper<>(Field.DARK);

        player =Player.PLAYER_1;
    }

    @Override
    public boolean isLegalToMoveFrom(Position from) {
        return isOnBoard(from) && !isEmpty(from);
    }

    @Override
    public boolean isLegalMove(Position from, Position to) {
        if (isFirstPlayer()) {
            return isLegalToMoveFrom(from) && isOnBoard(to) && isEmpty(to) && isFoxMove(from, to);
        } else
            return isLegalToMoveFrom(from) && isOnBoard(to) && isEmpty(to) && isHoundMove(from, to);
    }

    @Override
    public void makeMove(Position from, Position to) {
        setField(to, getField(from));
        setField(from, Field.EMPTY);
        player = player.opponent();
    }

    @Override
    public Player getNextPlayer() {
        return player;
    }

    @Override
    public boolean isGameOver()  {
        if (isFoxTrapped(getFoxPosition())) {
            return true;
        }

        return isFoxBehindHounds(getFoxPosition(), getHoundPositions());
    }

    @Override
    public Status getStatus() {
        if (!isGameOver()) {
            return Status.IN_PROGRESS;
        }
        return isGameOver() && isFoxBehindHounds(getFoxPosition(), getHoundPositions()) ? Status.PLAYER_1_WINS : Status.PLAYER_2_WINS;
    }

    public boolean isEmpty(Position p) {
        return getField(p) == Field.EMPTY;
    }

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

    public static boolean isOnBoard(Position p) {
        return 0 <=p.row() && p.row() < BOARD_SIZE && 0 <= p.col() && p.col() < BOARD_SIZE;
    }

    public static boolean isFoxMove(Position from, Position to) {
        var dx = Math.abs(to.row() - from.row());
        var dy = Math.abs(to.col() - from.col());
        return dx * dy == 1;
    }

    public static boolean isHoundMove(Position from, Position to) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoxCatcherGameState that = (FoxCatcherGameState) o;
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
