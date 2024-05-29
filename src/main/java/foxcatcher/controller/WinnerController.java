package foxcatcher.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import org.tinylog.Logger;

import java.io.IOException;

public class WinnerController extends BaseController {
    @FXML
    public Text resultText;
    private final StringProperty winner = new SimpleStringProperty();

    @FXML
    private void initialize() {
        resultText.textProperty().bind(winner);
    }

    public void setResultText(String winner) {
        this.winner.set("Winner: " + winner);
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
    public void onLeaderBoard(ActionEvent actionEvent) {
        Logger.debug("Opening leaderboard.");
        try {
            loadStage(actionEvent, "leaderboard");
        } catch (IOException e) {
            Logger.error("Failed to load Leaderboard!: ", e.getMessage());
        }
    }

    @FXML
    private void onQuit() {
        Logger.debug("Closing the Game! :(");
        Platform.exit();
    }

}
