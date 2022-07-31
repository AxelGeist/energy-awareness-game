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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;

public class LeaderboardMidGameCtrl extends GameManager implements Initializable, Ctrl {

    @FXML
    private TableColumn<Person, String> colRank;
    @FXML
    private TableColumn<Person, String> colPerson;
    @FXML
    private TableColumn<Person, String> colScore;

    /**
     * Constructor for the ctrl
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public LeaderboardMidGameCtrl(MainCtrl mainCtrl) {
        super(mainCtrl);
        timer = new Timer();
    }

    /**
     * Load the leaderboard midgame
     *
     * @param scene The scene to load
     */
    @Override
    public void onLoad(Scene scene) {
        start();
        this.mainCtrl.register(this);
        mainCtrl.setTitle("Game: Leaderboard");
        showLeaderboard();
    }

    /**
     * To initialize the leaderboard
     *
     * @param location  the location
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
}