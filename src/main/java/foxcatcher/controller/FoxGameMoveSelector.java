package foxcatcher.controller;

import foxcatcher.model.FoxCatcherGameModel;
import foxcatcher.model.Position;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;


public class FoxGameMoveSelector {
    public enum Phase {
        SELECT_FROM,
        SELECT_TO,
        READY_TO_MOVE
    }

    private final FoxCatcherGameModel model;
    private ReadOnlyObjectWrapper<Phase> phase =new ReadOnlyObjectWrapper<>(Phase.SELECT_FROM);
    private boolean invalidSelection = false;
    private Position from, to;

    public FoxGameMoveSelector(FoxCatcherGameModel model) {
        this.model = model;
    }

    public Phase getPhase() {
        return phase.get();
    }

    public ReadOnlyObjectProperty<Phase> phaseProperty() {
        return phase.getReadOnlyProperty();
    }

    public boolean isReadyToMove() {
        return getPhase() == Phase.READY_TO_MOVE;
    }

    public void select(Position position) {
        switch (phase.get()) {
            case SELECT_FROM -> selectFrom(position);
            case SELECT_TO -> selectTo(position);
            case READY_TO_MOVE -> throw new IllegalStateException();
        }
    }

    private void selectFrom(Position position) {
        if (model.isLegalToMoveFrom(position)) {
            from = position;
            phase.set(Phase.SELECT_TO);
            invalidSelection = false;
        } else {
            invalidSelection = true;
        }
    }

    private void selectTo(Position position) {
        if (model.isLegalMove(from, position)) {
            to = position;
            phase.set(Phase.READY_TO_MOVE);
            invalidSelection = false;
        } else {
            invalidSelection = true;
        }
    }

    public boolean isInvalidSelection() {
        return invalidSelection;
    }

    public void makeMove() {
        if (getPhase() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        if (model.isLegalMove(from, to)){
            model.makeMove(from, to);
            reset();
        } else {
            System.err.println("Illegal move!");
        }
    }

    public void reset() {
        from = null;
        to = null;
        phase.set(Phase.SELECT_FROM);
        invalidSelection = false;
    }

}
