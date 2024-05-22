package foxcatcher.controller;

import foxcatcher.model.FoxCatcherGameState;
import foxcatcher.model.Position;

import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class NewGameController {
    @FXML
    private GridPane board;
    private FoxCatcherGameState model = new FoxCatcherGameState();
    private FoxGameMoveSelector selector = new FoxGameMoveSelector(model);


    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var field = createField(i, j);
                board.add(field, j, i);
            }
        }
    }

    private StackPane createField(int i, int j) {
        var field = new StackPane();
        field.getStyleClass().add("square");
        var piece = new Circle(20);

        piece.fillProperty().bind(
                new ObjectBinding<>() {
                    {
                        super.bind(model.fieldProperty(i, j));
                    }
                    @Override
                    protected Paint computeValue() {
                        return switch (model.fieldProperty(i, j).get()) {
                            case EMPTY -> Color.TRANSPARENT;
                            case DARK -> Color.BLACK;
                            case LIGHT -> Color.BLUE;
                        };
                    }
                }
        );
        field.getChildren().add(piece);
        field.setOnMouseClicked(this::handleMouseClick);
        return field;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var field = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(field);
        var col = GridPane.getColumnIndex(field);
        System.out.printf("Click on field (%d,%d)%n", row, col);
        selector.select(new Position(row, col));
        if (selector.isReadyToMove() && !selector.isInvalidSelection()) {
            selector.makeMove();
        }
    }

}
