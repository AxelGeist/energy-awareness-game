/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import java.net.URL;
import java.util.*;

import client.utils.WaitingRoomThread;
import com.google.inject.Inject;

import commons.Person;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;

public class WaitingRoomCtrl implements Initializable, Ctrl {

    private final MainCtrl mainCtrl;
    private Thread updatesThread;
    private ObservableList<Person> data;

    @FXML
    private TableView<Person> table1;
    @FXML
    private TableView<Person> table2;
    @FXML
    private TableView<Person> table3;
    @FXML
    private TableColumn<Person, String> colPerson1;
    @FXML
    private TableColumn<Person, String> colPerson2;
    @FXML
    private TableColumn<Person, String> colPerson3;
    @FXML
    private Text text;

    /**
     * Constructor for the ctrl
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public WaitingRoomCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table1.setPlaceholder(new Label(""));
        table2.setPlaceholder(new Label(""));
        table3.setPlaceholder(new Label(""));

        colPerson1.setCellValueFactory(q ->
                new SimpleStringProperty(q.getValue().getUsername()));
        colPerson2.setCellValueFactory(q ->
                new SimpleStringProperty(q.getValue().getUsername()));
        colPerson3.setCellValueFactory(q ->
                new SimpleStringProperty(q.getValue().getUsername()));
    }


    /**
     * Method to stop the waiting room thread and start a game thread
     */
    public void ok() {
        updatesThread.interrupt();
        mainCtrl.setQuestionLapCounter(0);
        mainCtrl.setUsedDoublePoints(false);
        mainCtrl.setUsedRemoveAnswer(false);
        mainCtrl.setUsedTimer(false);
        mainCtrl.startLongPollingGame();
        mainCtrl.showScene(6);
    }

    /**
     * The function that is called when the start game is pressed
     */
    public void startGameMultiplayer() {
        mainCtrl.getServer().startGameMultiplayer();
    }


    public ObservableList<Person> getPlayers() {
        return data;
    }

    /**
     * refresh the WaitingRoom
     * split the player list into 3 sublists and divide them over
     * all the columns
     */
    public void refresh() {
        List<Person> person = mainCtrl.getServer().getPerson();
        data = FXCollections.observableList(person);
        data.sort(Comparator.comparing(Person::getScore).reversed());

        ObservableList<Person> l1 = FXCollections.observableArrayList(new ArrayList<>());
        ObservableList<Person> l2 = FXCollections.observableArrayList(new ArrayList<>());
        ObservableList<Person> l3 = FXCollections.observableArrayList(new ArrayList<>());

        l1.removeAll();
        l2.removeAll();
        l3.removeAll();

        for (int i = 0; i < data.size(); i++) {
            switch (i % 3) {
                case 0:
                    l1.add(data.get(i));
                    break;
                case 1:
                    l2.add(data.get(i));
                    break;
                case 2:
                    l3.add(data.get(i));
                    break;
            }
        }

        table1.setItems(l1);
        table2.setItems(l2);
        table3.setItems(l3);

        String textInput = "";
        if (data.size() == 1) {
            textInput += "You're alone in the waiting room...";
        } else {
            textInput += data.size() + " players in the waiting room...";
        }
        text.setText(textInput);
    }

    /**
     * To load the scene
     *
     * @param scene the scene to load
     */
    @Override
    public void onLoad(Scene scene) {
        mainCtrl.setSingleplayer(false);
        mainCtrl.setTitle("Waiting Room");
        this.checkUpdates();
        refresh();
    }

    /**
     * Method to go back to main menu
     */
    public void cancel() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText("Are you sure you want to return to the main menu?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            mainCtrl.getServer().leavePerson(this.mainCtrl.getUserID());
            this.mainCtrl.setUserID(-1L);
            this.mainCtrl.setUsername("");
            this.updatesThread.interrupt();
            this.mainCtrl.showScene(0);
        }
    }

    /**
     * Method to start the long polling
     */
    public void checkUpdates() {
        this.updatesThread = new WaitingRoomThread(this, mainCtrl);
    }

}