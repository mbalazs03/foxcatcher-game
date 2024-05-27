package foxcatcher.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

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
}
