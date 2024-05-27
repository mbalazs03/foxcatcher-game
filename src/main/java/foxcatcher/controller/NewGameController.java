package foxcatcher.controller;

import foxcatcher.model.Field;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import util.javafx.EnumImageStorage;
import util.javafx.ImageStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZonedDateTime;

import gameresult.TwoPlayerGameResult;
import gameresult.manager.TwoPlayerGameResultManager;
import gameresult.manager.json.JsonTwoPlayerGameResultManager;


public class NewGameController {

    @FXML
    public Text nameField1, nameField2;
    @FXML
    private GridPane board;
    private final StringProperty name1 = new SimpleStringProperty();
    private final StringProperty name2 = new SimpleStringProperty();
    private final FoxCatcherGameModel model = new FoxCatcherGameModel();
    private final FoxGameMoveSelector selector = new FoxGameMoveSelector(model);
    private final ImageStorage<Field> imageStorage = new EnumImageStorage<>(Field.class);


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

    @FXML
    public void onNewGame(ActionEvent event) {
    }

    @FXML
    public void onSaveGame(ActionEvent event) {
    }

    @FXML
    public void onAbout(ActionEvent event) {
    }

    @FXML
    private void onQuit() {
        Platform.exit();
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
        if ((i + j) % 2 == 0) {
            field.getStyleClass().add("light");
        } else {
            field.getStyleClass().add("dark");
        }
        var imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.imageProperty().bind(
                new ObjectBinding<>() {
                    {
                        super.bind(model.fieldProperty(i, j));
                    }

                    @Override
                    protected Image computeValue() {
                        return imageStorage.get(model.fieldProperty(i, j).get()).orElse(null);
                    }
                }
        );
        field.getChildren().add(imageView);
        field.setOnMouseClicked(this::handleMouseClick);
        return field;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var field = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(field);
        var col = GridPane.getColumnIndex(field);
        TwoPlayerGameResultManager manager = new JsonTwoPlayerGameResultManager(Path.of("results.json"));
        System.out.printf("Click on field (%d,%d)%n", row, col);
        selector.select(new Position(row, col));
        if (selector.isReadyToMove() && !selector.isInvalidSelection()) {
            selector.makeMove();
            if (isEndGame()) {
                try {
                    manager.add(createGameResult());
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/winner.fxml"));
        Parent root = fxmlLoader.load();
        WinnerController controller = fxmlLoader.getController();
        controller.setResultText(winner().getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private TwoPlayerGameResult createGameResult() {
        return TwoPlayerGameResult.builder()
                .player1Name(name1.get())
                .player2Name(name2.get())
                .status(model.getStatus())
                .numberOfTurns(model.getTurns())
                .duration(Duration.ofSeconds(model.getGameDurationInSeconds()))
                .created(ZonedDateTime.now())
                .build();
    }

}
