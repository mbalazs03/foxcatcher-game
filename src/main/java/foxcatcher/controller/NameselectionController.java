package foxcatcher.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class NameselectionController {

    @FXML
    public TextField playerOneName, playerTwoName;

    public void startGame(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newgame.fxml"));
        Parent root = fxmlLoader.load();
        NewGameController controller = fxmlLoader.getController();
        controller.setPlayer1Name(playerOneName.getText());
        controller.setPlayer2Name(playerTwoName.getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
