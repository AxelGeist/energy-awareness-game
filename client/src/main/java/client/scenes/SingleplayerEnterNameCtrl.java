package client.scenes;

import com.google.inject.Inject;
import commons.Person;
import jakarta.ws.rs.WebApplicationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;


public class SingleplayerEnterNameCtrl implements Ctrl, Initializable {

    private final MainCtrl mainCtrl;
    private ObservableList<Person> data;

    @FXML
    private TextField title;
    @FXML
    private TableView<Person> table;
    @FXML
    private TableColumn<Person, String> colRank;
    @FXML
    private TableColumn<Person, String> colPerson;
    @FXML
    private TableColumn<Person, String> colScore;


    @FXML
    private TextField username;

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
     * Constructor for the ctrl
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public SingleplayerEnterNameCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Return to main menu with alert message
     */
    public void cancel() {
        clearFields();
        mainCtrl.cancel();
    }

    /**
     * Do the action required to start the game
     * Show an error message if the username picked is already in use
     */
    public void ok() {
        try {
            this.mainCtrl.setServer("http://localhost:8080/");
            Person person = mainCtrl.getServer().addPersonSP(username.getText());
            this.mainCtrl.setUsername(person.getUsername());
            this.mainCtrl.setUserID(person.getId());
            mainCtrl.setQuestionLapCounter(0);
            mainCtrl.startLongPollingGame();
            mainCtrl.getServer().startGameSingleplayer(person);
            mainCtrl.showScene(6);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("The username " + username.getText() + " is already in use.");
            alert.setContentText("Please choose a different username.");
            alert.setTitle("Invalid username");
            alert.showAndWait();
            return;
        }

        clearFields();
    }

    /**
     * To clear the fields
     */
    private void clearFields() {
        username.clear();
    }

    /**
     * Detect if a key is pressed to call certain functions
     *
     * @param e the pressed key
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * To load the scene
     *
     * @param scene the scene to load
     */
    @Override
    public void onLoad(Scene scene) {
        title.setEditable(false);
        mainCtrl.setSingleplayer(true);
        table.setPlaceholder(new Label(""));
        List<Person> person = mainCtrl.getServer().getAllPlayersInDatabase();
        person.sort(Comparator.comparing(Person::getScore).reversed());
        List<Person> goodList = new ArrayList<>();
        for (int i = 0; i < person.size() && i < 5; i++) {
            Person p = person.get(i);
            p.setPlace(i + 1);
            goodList.add(p);
        }
        data = FXCollections.observableList(goodList);
        table.setItems(data);
        mainCtrl.setTitle("Enter name");
        scene.setOnKeyPressed(this::keyPressed);
    }


}
