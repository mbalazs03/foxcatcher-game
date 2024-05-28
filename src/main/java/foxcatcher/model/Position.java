package foxcatcher.model;

/**
 * A record representing a position on the game board.
 *
 * @param row the row of the position
 * @param col the column of the position
 */
public record Position(int row, int col) {
    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}
