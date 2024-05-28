package foxcatcher.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;


public class NameselectionController extends BaseController {

    @FXML
    public TextField playerOneName, playerTwoName;

    @FXML
    public void startGame(ActionEvent event) throws IOException {
        if (!(playerOneName.getText().isEmpty()) && !(playerTwoName.getText().isEmpty())) {
            Logger.debug("Starting new game. \n Player1: {} \n Playyer2: {}", playerOneName.getText(), playerTwoName.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newgame.fxml"));
            Parent root = fxmlLoader.load();
            NewGameController controller = fxmlLoader.getController();
            controller.setPlayer1Name(playerOneName.getText());
            controller.setPlayer2Name(playerTwoName.getText());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Logger.debug("There are no player names!");
            Platform.runLater(this::showGameAlert);
        }

    }

    @FXML
    public void onBack(ActionEvent actionEvent) {
        Logger.debug("Opening Home.");
        try {
            loadStage(actionEvent, "home");
        } catch (IOException e) {
            Logger.error("Failed to load Home page!: ", e.getMessage());
        }
    }

    private void showGameAlert() {
        var alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Warning");
        alert.setContentText("The player names could not be empty!");
        alert.showAndWait();
    }
}
