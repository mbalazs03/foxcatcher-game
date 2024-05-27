package foxcatcher.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.tinylog.Logger;

public class WinnerController {

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

    public void onQuit(ActionEvent actionEvent) {
        Logger.debug("Closing Game. :(");
        Platform.exit();
    }
}
