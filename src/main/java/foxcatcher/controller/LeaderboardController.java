package foxcatcher.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.nio.file.Path;

import gameresult.manager.TwoPlayerGameResultManager;
import gameresult.manager.json.JsonTwoPlayerGameResultManager;

public class LeaderboardController {
    private static final int NUMBER_OF_ROWS_TO_SHOW = 15;

    @FXML
    private TableView<TwoPlayerGameResultManager.Wins> tableView;

    @FXML
    private TableColumn<TwoPlayerGameResultManager.Wins, String> playerName;

    @FXML
    private TableColumn<TwoPlayerGameResultManager.Wins, Integer> numberOfWins;

    @FXML
    private void initialize() throws IOException {
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        numberOfWins.setCellValueFactory(new PropertyValueFactory<>("numberOfWins"));
        ObservableList<TwoPlayerGameResultManager.Wins> observableList = FXCollections.observableArrayList();
        observableList.addAll(new JsonTwoPlayerGameResultManager(Path.of("results.json")).getPlayersWithMostWins(NUMBER_OF_ROWS_TO_SHOW));
        tableView.setItems(observableList);
    }
}
