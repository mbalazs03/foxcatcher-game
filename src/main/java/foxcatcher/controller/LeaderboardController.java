package foxcatcher.controller;

import foxcatcher.model.FoxCatcherGameModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.nio.file.Path;

import gameresult.manager.TwoPlayerGameResultManager;
import gameresult.manager.json.JsonTwoPlayerGameResultManager;
import org.tinylog.Logger;

public class LeaderboardController extends BaseController {
    private static final int NUMBER_OF_ROWS_TO_SHOW = 15;
    @FXML
    private TableView<TwoPlayerGameResultManager.Wins> leaderBoardTable;
    @FXML
    private TableColumn<TwoPlayerGameResultManager.Wins, String> playerColumn;
    @FXML
    private TableColumn<TwoPlayerGameResultManager.Wins, Integer> scoreColumn;
    private final FoxCatcherGameModel model = new FoxCatcherGameModel();

    @FXML
    private void initialize() throws IOException {
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfWins"));
        ObservableList<TwoPlayerGameResultManager.Wins> observableList = FXCollections.observableArrayList();
        observableList.addAll(new JsonTwoPlayerGameResultManager(Path.of("results.json")).getPlayersWithMostWins(NUMBER_OF_ROWS_TO_SHOW));
        leaderBoardTable.setItems(observableList);
    }

    @FXML
    public void onBack(ActionEvent actionEvent) {
        if (model.isGameOver()) {
            Logger.debug("Opening Result.");
            try {
                loadStage(actionEvent, "winner");
            } catch (IOException e) {
                Logger.error("Failed to load Result!: ", e.getMessage());
            }
        } else {
            Logger.debug("Opening Home page.");
            try {
                loadStage(actionEvent, "home");
            } catch (IOException e) {
                Logger.error("Failed to load Home page!: ", e.getMessage());
            }
        }
    }

}
