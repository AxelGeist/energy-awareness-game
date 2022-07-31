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

import com.google.inject.Inject;
import commons.Person;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;


public class SingleplayerLeaderboardCtrl implements Ctrl, Initializable {

    private final MainCtrl mainCtrl;
    private ObservableList<Person> data;

    @FXML
    private Pane group;
    @FXML
    private Label no;
    @FXML
    private Label noShadow;
    @FXML
    private Label score;
    @FXML
    private Label scoreShadow;
    @FXML
    private Label name;
    @FXML
    private Label nameShadow;
    @FXML
    private Label place;
    @FXML
    private Label place_shadow;
    @FXML
    private TableView<Person> table;
    @FXML
    private TableColumn<Person, String> colRank;
    @FXML
    private TableColumn<Person, String> colPerson;
    @FXML
    private TableColumn<Person, String> colScore;

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
     * Constructor for the leaderboard
     *
     * @param mainCtrl mainCtrl communication
     */
    @Inject
    public SingleplayerLeaderboardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * @param scene The scene to load
     */
    @Override
    public void onLoad(Scene scene) {
        mainCtrl.getServer().modify(mainCtrl.getUserID(), mainCtrl.getScore());
        setVisible(false);
        List<Person> person = mainCtrl.getServer().getAllPlayersInDatabase();
        person.sort(Comparator.comparing(Person::getScore).reversed());
        List<Person> goodList = new ArrayList<>();
        for (int i = 0; i < person.size() && i < 5; i++) {
            if (mainCtrl.getUserID() == person.get(i).getId()) {
                if (person.get(i).getId() == mainCtrl.getUserID()) {
                    firstFive(i);
                }
            }
            Person p = person.get(i);
            p.setPlace(i + 1);
            goodList.add(p);
        }
        for (int i = 5; i < person.size(); i++) {
            Person p = person.get(i);
            if (p.getId() == mainCtrl.getUserID()) {
                int k = i + 1;
                setVisible(true);
                no.setText(k + ".");
                noShadow.setText(k + ".");
                name.setText(p.getUsername());
                nameShadow.setText(p.getUsername());
                score.setText(String.valueOf(p.getScore()));
                scoreShadow.setText(String.valueOf(p.getScore()));
            }
        }
        data = FXCollections.observableList(goodList);
        table.setItems(data);
        mainCtrl.setTitle("Game: Leaderboard");
    }

    /**
     * Set visible for the people outside top 5
     *
     * @param trigger to set the visibility
     */
    public void setVisible(boolean trigger) {
        group.setVisible(trigger);
        place_shadow.setVisible(!trigger);
        place.setVisible(!trigger);
    }


    /**
     * Helps us with setting the text for the first 5 players
     *
     * @param i the number of the player
     */
    public void firstFive(int i) {
        String[] ranks = {"st", "nd", "rd", "th", "th"};
        place.setText("You achieved " + (i + 1) + ranks[i] + " place!");
        place_shadow.setText("You achieved " + (i + 1) + ranks[i] + " place!");
    }


    /**
     * To clear the fields in the screen
     */
    public void cancel() {
        mainCtrl.cancel();
    }


}