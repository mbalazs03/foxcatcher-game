package foxcatcher.controller;

import foxcatcher.model.FoxCatcherGameModel;
import foxcatcher.model.Position;

import game.State;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class NewGameController {
    @FXML
    public Text nameField1, nameField2;
    private final StringProperty name1 = new SimpleStringProperty();
    private final StringProperty name2 = new SimpleStringProperty();
    @FXML
    private GridPane board;
    private final FoxCatcherGameModel model = new FoxCatcherGameModel();
    private final FoxGameMoveSelector selector = new FoxGameMoveSelector(model);


    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var field = createField(i, j);
                board.add(field, j, i);
            }
        }
        nameField1.textProperty().bind(Bindings.concat(name1));
        nameField2.textProperty().bind(Bindings.concat(name2));
    }

    public void setPlayer1Name(String name) {
        this.name1.set(name);
    }

    public void setPlayer2Name(String name) {
        this.name2.set(name);
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
            if (isEndGame()) {
                try {
                    showResult(event);
                } catch (IOException e) {
                    System.err.println("Error loading next stage.");
                }
            }
        }
    }

    private boolean isEndGame() {
        return model.isGameOver();
    }

    private Text winner() {
        if (isEndGame() && model.getStatus() == State.Status.PLAYER_1_WINS) {
            return nameField1;
        } else {
            return nameField2;
        }
    }

    private void showResult(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/leaderboard.fxml"));
        Parent root = fxmlLoader.load();
        LeaderboardController controller = fxmlLoader.getController();
        controller.setResultText(winner().getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    public void onNewGame(ActionEvent event) {
    }

    public void onSaveGame(ActionEvent event) {
    }

    public void onAbout(ActionEvent event) {
    }

    @FXML
    private void onQuit() {
        Platform.exit();
    }
}
