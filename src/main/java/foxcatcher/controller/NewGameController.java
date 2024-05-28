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
import javafx.beans.value.ObservableValue;
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

import org.tinylog.Logger;
import util.javafx.EnumImageStorage;
import util.javafx.ImageStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.ZonedDateTime;

import gameresult.TwoPlayerGameResult;
import gameresult.manager.TwoPlayerGameResultManager;
import gameresult.manager.json.JsonTwoPlayerGameResultManager;

public class NewGameController extends BaseController {
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
        model.startGameTime();
        selector.phaseProperty().addListener(this::showSelectionPhaseChange);
        Logger.debug("Game Started.");
    }

    @FXML
    public void onNewGame(ActionEvent actionEvent) {
        Logger.debug("New Game started.");
        try {
            loadStage(actionEvent, "nameselection");
        } catch (IOException e) {
            Logger.error("Failed to start the game!: ", e.getMessage());
        }
    }

    @FXML
    private void onQuit() {
        Logger.debug("Closing the Game! :(");
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
        Logger.debug("Click on field ({}, {})", row, col);
        selector.select(new Position(row, col));
        if (selector.isReadyToMove() && !selector.isInvalidSelection()) {
            selector.makeMove();
            if (isEndGame()) {
                try {
                    Logger.debug("Game Over, {} wins", winner().getText());
                    model.endGameTime();
                    manager.add(createGameResult());
                    showResult(event);
                } catch (IOException e) {
                    Logger.error("Error loading next stage: {}", e.getMessage());
                }
            }
        }
    }

    private void showSelectionPhaseChange(ObservableValue<? extends FoxGameMoveSelector.Phase> value, FoxGameMoveSelector.Phase oldPhase, FoxGameMoveSelector.Phase newPhase) {
        switch (newPhase) {
            case SELECT_FROM -> {}
            case SELECT_TO -> showSelection(selector.getFrom());
            case READY_TO_MOVE -> hideSelection(selector.getFrom());
        }
    }

    private void showSelection(Position position) {
        var square = getField(position);
        square.getStyleClass().add("selected");
    }

    private void hideSelection(Position position) {
        var square = getField(position);
        square.getStyleClass().remove("selected");
    }

    private StackPane getField(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError();
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
        Logger.debug("Result generated.");
        return TwoPlayerGameResult.builder()
                .player1Name(name1.get())
                .player2Name(name2.get())
                .status(model.getStatus())
                .numberOfTurns(model.getTurns())
                .duration(Duration.ofSeconds(model.getGameDurationInSeconds()))
                .created(ZonedDateTime.now())
                .build();
    }

    public void onBack(ActionEvent actionEvent) {
        Logger.debug("Opening Home page.");
        try {
            loadStage(actionEvent, "home");
        } catch (IOException e) {
            Logger.error("Failed to load Home page!: ", e.getMessage());
        }
    }
}
