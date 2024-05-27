package foxcatcher.controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;


public class HomeController {

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
    public void onLoadGame(ActionEvent actionEvent) {
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
    private void loadStage(ActionEvent event, String file) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/"+ file +".fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
