package foxcatcher.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import org.tinylog.Logger;

import java.io.IOException;

public class HomeController extends BaseController {
    @FXML
    public void onNewGame(ActionEvent actionEvent) {
        Logger.debug("New Game created.");
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
    public void onAbout(ActionEvent actionEvent) {
        Logger.debug("Opening About page.");
        try {
            loadStage(actionEvent, "about");
        } catch (IOException e) {
            Logger.error("Failed to load About page!: ", e.getMessage());
        }
    }

    @FXML
    private void onQuit() {
        Logger.debug("Closing the Game! :(");
        Platform.exit();
    }


}
