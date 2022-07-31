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

import client.utils.GameThread;
import client.utils.ServerUtils;
import commons.Question;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.util.*;

public class MainCtrl {

    public Stage primaryStage;

    private List<Pair<Ctrl, Scene>> scenes;
    private long userID;
    private String username;
    private Question question;
    private long gameID;
    private int score;
    private GameManager listener;
    private ServerUtils server;
    private GameThread gameThread;
    private boolean isSingleplayer;
    private boolean usedDoublePoints;
    private boolean usedTimer;
    private boolean usedRemoveAnswer;
    private int questionLapCounter;



    /**
     * @param primaryStage This is the stage that will be shown.
     * @param scenes       This is a varargs parameter; put as many Pair<Ctrl, Parent> as needed.
     *                     The showScene method will show the first scene
     *                     of these varargs if called with showScene(0),
     *                     and the second if called with showScene(1), etc.
     */
    public void initialize(Stage primaryStage, Pair<Ctrl, Parent>... scenes) {
        this.primaryStage = primaryStage;
        this.scenes = new ArrayList<>();
        this.server = new ServerUtils();
        this.userID = -1;
        this.score = 0;
        this.isSingleplayer = false;
        this.questionLapCounter = 0;

        for (Pair<Ctrl, Parent> scene : scenes) {
            this.scenes.add(new Pair<>(scene.getKey(), new Scene(scene.getValue())));
        }

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                var alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText("Are you sure you want to close the application?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    if (userID != -1) {
                        server.leavePerson(userID);
                        if (gameThread.isAlive()) gameThread.interrupt();
                    }
                    Platform.exit();
                    System.exit(0);
                } else {
                    we.consume();
                }
            }
        });

        showScene(0);
        primaryStage.show();
    }

    /**
     * Show the scene corresponding to the index
     *
     * @param index The index of the scene u want to display
     */
    public void showScene(int index) {
        Pair<Ctrl, Scene> pair = scenes.get(index);
        scenes.get(index).getValue().getStylesheets().add("client/styling/style.css");
        primaryStage.setScene(pair.getValue());
        pair.getKey().onLoad(pair.getValue());
    }

    /**
     * Assign a listener which listens to the eventHappens() method
     *
     * @param listener The current displayed scene control
     */
    public void register(GameManager listener) {
        this.listener = listener;
    }

    /**
     * calls the onEvent on the assigned control.
     * For instance, when an emote is sent.
     *
     * @param emote    the emote to display
     * @param username the emote 's sender
     */
    public void showEmote(String emote, String username) {
        if (listener != null) listener.showEmote(emote, username);
    }

    /**
     * calls the onEvent on the assigned control.
     * For instance, when an emote is sent.
     */
    public void showAnswer() {
        if (listener == null) return;
        listener.showAnswer();
    }

    /**
     * Set title for the stage
     *
     * @param s The title to give to the primary stage
     */
    public void setTitle(String s) {
        primaryStage.setTitle(s);
    }

    /**
     * Return to main menu with alert message
     */
    public void cancel() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText("Are you sure you want to return to the main menu?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            showScene(0);
        }
    }

    /**
     * Method to set the server path
     * @param path the server path
     */
    public void setServer(String path) {
        this.server.setServer(path);
    }

    /**
     * Return to main menu with alert message
     */
    public void cancelAndRemovePlayer() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText("Are you sure you want to return to the main menu?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            gameThread.interrupt();
            server.leavePerson(this.userID);
            this.usedDoublePoints = false;
            this.usedTimer = false;
            this.usedRemoveAnswer = false;
            this.score = 0;
            this.gameID = -1;
            showScene(0);
        }
    }

    /**
     * To start the game thread for long polling
     */
    public void startLongPollingGame() {
        gameThread = new GameThread(this);
    }

    /**
     * To interrupt the game thread
     */
    public void stopLongPollingGame() {
        gameThread.interrupt();
    }

    /**
     * Method to call when a timer joker is used
     * @param startOfCurrentQuestion start of question
     */
    public void usedTimerJoker(long startOfCurrentQuestion) {
        if (listener != null) listener.disableJoker(startOfCurrentQuestion);
    }

    public boolean isSingleplayer() {
        return isSingleplayer;
    }

    public void setSingleplayer(boolean singleplayer) {
        isSingleplayer = singleplayer;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getGameID() {
        return gameID;
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getUsedDoublePoints() {
        return usedDoublePoints;
    }

    public void setUsedDoublePoints(boolean usedDoublePoints) {
        this.usedDoublePoints = usedDoublePoints;
    }

    public boolean getUsedTimer() {
        return usedTimer;
    }

    public void setUsedTimer(boolean usedTimer) {
        this.usedTimer = usedTimer;
    }

    public boolean getUsedRemoveAnswer() { return usedRemoveAnswer; }

    public void setUsedRemoveAnswer(boolean usedRemoveAnswer) {
        this.usedRemoveAnswer = usedRemoveAnswer;
    }

    public ServerUtils getServer() {
        return server;
    }

    public String getServerPath() {
        return server.getServer();
    }

    public int getQuestionLapCounter() {
        return this.questionLapCounter;
    }

    public void setQuestionLapCounter(int questionLap) {
        this.questionLapCounter = questionLap;
    }
}