package foxcatcher.controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class HomeController {

    @FXML
    public void onNewGame(ActionEvent actionEvent) {
        try {
            loadStage(actionEvent, "nameselection");
        } catch (IOException e) {
            System.err.println("Error loading next stage!");
        }
    }

    @FXML
    public void onLoadGame(ActionEvent actionEvent) {

    }

    @FXML
    public void onLeaderBoard(ActionEvent actionEvent) {
        try {
            loadStage(actionEvent, "leaderboard");
        } catch (IOException e) {
            System.err.println("Error loading next stage");
        }
    }

    @FXML
    private void onQuit() {
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
