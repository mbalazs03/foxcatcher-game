package foxcatcher.model;

import game.TwoPhaseMoveState;

public class FoxCatcherGameState implements TwoPhaseMoveState<Position> {
    @Override
    public boolean isLegalToMoveFrom(Position position) {
        return false;
    }

    @Override
    public boolean isLegalMove(Position position, Position t1) {
        return false;
    }

    @Override
    public void makeMove(Position position, Position t1) {

    }

    @Override
    public Player getNextPlayer() {
        return null;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public Status getStatus() {
        return null;
    }
}
