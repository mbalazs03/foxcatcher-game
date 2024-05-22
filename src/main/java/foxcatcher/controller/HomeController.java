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
            loadStage(actionEvent);
        } catch (IOException e) {
            System.err.println("Error creating new game!");
        }
    }

    @FXML
    private void onQuit() {
        Platform.exit();
    }

    private void loadStage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/nameselection.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
