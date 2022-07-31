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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import com.google.inject.Injector;

import client.scenes.MainCtrl;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * Main function start the application
     *
     * @param args the arguments
     * @throws URISyntaxException an exception that can be thrown when something goes wrong
     * @throws IOException        an exception that can be thrown when something goes wrong
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     * Method to load all the different scenes and initialize the mainCtrl
     *
     * @param primaryStage The stage to load on
     * @throws IOException is thrown if something went wrong
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        var startScreen = FXML.load("client", "scenes", "StartScreen.fxml"); // 0
        var add = FXML.load("client", "scenes", "MultiplayerEnterName.fxml"); // 1
        var overview = FXML.load("client", "scenes", "MultiplayerLeaderboard.fxml"); // 2
        var addSingleplayer = FXML.load("client", "scenes", "SingleplayerEnterName.fxml"); // 3
        var help = FXML.load("client", "scenes", "helpScreen.fxml"); // 4
        var waitingRoom = FXML.load("client", "scenes", "WaitingRoom.fxml"); // 5
        var relax = FXML.load("client", "scenes", "RelaxScreen.fxml"); // 6
        var comparisonQuestion = FXML.load("client", "scenes", "ComparisonQuestion.fxml"); // 7
        var mcQuestion = FXML.load("client", "scenes", "MCQuestion.fxml"); // 8
        var openQuestion = FXML.load("client", "scenes", "OpenQuestion.fxml"); // 9
        var guessQuestion = FXML.load("client", "scenes", "GuessQuestion.fxml"); // 10
        var leaderboardEndGameMP =
                FXML.load("client", "scenes", "MultiplayerLeaderboardEndGame.fxml"); // 11
        var singleplayerLeaderboard =
                FXML.load("client", "scenes", "SingleplayerLeaderboard.fxml"); // 12
        var insteadQuestion = FXML.load("client", "scenes", "insteadQuestion.fxml"); // 13
        var adminPanel = FXML.load("client", "scenes", "AdminPanel.fxml"); // 14
        var closestQuestion = FXML.load("client", "scenes", "ClosestQuestion.fxml"); // 15


        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

        mainCtrl.initialize(primaryStage, startScreen, add, overview,
                addSingleplayer, help, waitingRoom, relax,
                comparisonQuestion, mcQuestion, openQuestion, guessQuestion,
                leaderboardEndGameMP, singleplayerLeaderboard, insteadQuestion,
                adminPanel, closestQuestion);
        primaryStage.setResizable(false);
    }
}