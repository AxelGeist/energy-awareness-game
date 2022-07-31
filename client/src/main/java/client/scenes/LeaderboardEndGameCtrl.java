package client.scenes;

import com.google.inject.Inject;
import commons.Person;
import jakarta.ws.rs.WebApplicationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

public class LeaderboardEndGameCtrl extends GameManager implements Initializable, Ctrl {

    @FXML
    private TableColumn<Person, String> colRank;
    @FXML
    private TableColumn<Person, String> colPerson;
    @FXML
    private TableColumn<Person, String> colScore;
    @FXML
    private Label firstPlace;
    @FXML
    private Label secondPlace;
    @FXML
    private Label thirdPlace;

    /**
     * Inject method for leaderboard endgame
     * @param mainCtrl the main ctrl
     */
    @Inject
    public LeaderboardEndGameCtrl(MainCtrl mainCtrl) {
        super(mainCtrl);
    }

    /**
     * Load the leaderboard endgame
     * @param scene The scene to load
     */
    @Override
    public void onLoad(Scene scene) {
        start();
        this.timer = new Timer();
        this.mainCtrl.register(this);
        mainCtrl.setTitle("Game: Leaderboard");
        showLeaderboard();
        displayTopThree();
    }

    /**
     * To initialize the leaderboard
     * @param location the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colRank.setCellValueFactory(q ->
                new SimpleStringProperty(q.getValue().getPlace() + "."));
        colPerson.setCellValueFactory(q ->
                new SimpleStringProperty(q.getValue().getUsername()));
        colScore.setCellValueFactory(q ->
                new SimpleStringProperty(String.valueOf(q.getValue().getScore())));
    }

    /**
     * Display the top three players on leaderboard
     */
    public void displayTopThree() {
        firstPlace.setText(data.get(0).getUsername());
        if (data.size() > 1) {
            secondPlace.setText(data.get(1).getUsername());
        }
        if (data.size() > 2) {
            thirdPlace.setText(data.get(2).getUsername());
        }
    }

    /**
     * Show enter name screen to start another game.
     */
    public void playAgain() {
        try {
            this.mainCtrl.stopLongPollingGame();
            this.mainCtrl.setScore(0);
            this.mainCtrl.getServer().waitingRoom(mainCtrl.getUserID());
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        this.mainCtrl.showScene(5);
    }
}
