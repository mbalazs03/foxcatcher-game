package foxcatcher.controller;

import foxcatcher.model.FoxCatcherGameModel;
import foxcatcher.model.Position;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import org.tinylog.Logger;

/**
 * The {@link FoxGameMoveSelector} class manages the process of selecting and making moves in the FoxCatcher game.
 */
public class FoxGameMoveSelector {
    /**
     * Enum representing the phases of move selection.
     */
    public enum Phase {
        /**
         * {@link Phase} to select the piece to move.
         */
        SELECT_FROM,
        /**
         * {@link Phase} to select the piece to move.
         */
        SELECT_TO,
        /**
         * {@link Phase} indicating readiness to make a move.
         */
        READY_TO_MOVE
    }

    private final FoxCatcherGameModel model;
    private final ReadOnlyObjectWrapper<Phase> phase =new ReadOnlyObjectWrapper<>(Phase.SELECT_FROM);
    private boolean invalidSelection = false;
    private Position from, to;

    /**
     * Constructor for {@link FoxGameMoveSelector}
     *
     * @param model The game model to interact with.
     */
    public FoxGameMoveSelector(FoxCatcherGameModel model) {
        this.model = model;
    }

    /**
     * Gets the current {@link Phase} of the move selection.
     *
     * @return The current {@link Phase}.
     */
    public Phase getPhase() {
        return phase.get();
    }

    /**
     * Gets the {@link ReadOnlyObjectProperty} of the current phase.
     *
     * @return The {@link ReadOnlyObjectProperty} {@link Phase} property.
     */
    ReadOnlyObjectProperty<Phase> phaseProperty() {
        return phase.getReadOnlyProperty();
    }

    /**
     * Checks if the selector is ready to make a move.
     *
     * @return True if ready to move, false otherwise.
     */
    public boolean isReadyToMove() {
        return getPhase() == Phase.READY_TO_MOVE;
    }

    /**
     * Selects a {@link Position} on the board based on the current {@link Phase}.
     *
     * @param position The {@link Position} to select.
     */
    protected void select(Position position) {
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

    /**
     * Gets the starting {@link Position} of the move.
     *
     * @return The starting {@link Position}.
     * @throws IllegalStateException if the current phase is SELECT_FROM.
     */
    public Position getFrom() {
        if (phase.get() == Phase.SELECT_FROM) {
            throw new IllegalStateException();
        }
        return from;
    }

    /**
     * Gets the destination {@link Position} of the move.
     *
     * @return The destination {@link Position}.
     * @throws IllegalStateException if the current {@link Phase} is not READY_TO_MOVE.
     */
    public Position getTo() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        return to;
    }

    /**
     * Checks if the last selection was invalid.
     *
     * @return True if the selection was invalid, false otherwise.
     */
    public boolean isInvalidSelection() {
        return invalidSelection;
    }

    /**
     * Makes the move from the selected starting {@link Position} to the destination {@link Position}.
     *
     * @throws IllegalStateException if the current {@link Phase} is not READY_TO_MOVE.
     */
    public void makeMove() {
        if (getPhase() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        if (model.isLegalMove(from, to)){
            model.makeMove(from, to);
            reset();
        } else {
            Logger.error("Illegal move!");
        }
    }

    /**
     * Resets the move selector to its initial state.
     */
    public void reset() {
        from = null;
        to = null;
        phase.set(Phase.SELECT_FROM);
        invalidSelection = false;
    }

}
