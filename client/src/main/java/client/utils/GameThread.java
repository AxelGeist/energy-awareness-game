package client.utils;

import client.scenes.MainCtrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commons.*;
import javafx.application.Platform;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class GameThread extends Thread implements Runnable {

    private final MainCtrl mainCtrl;

    /**
     * Constructor for the thread
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    public GameThread(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        Thread updateThread = new Thread(this);
        updateThread.start();
    }

    /**
     * This method sends a request to the server on the getUpdates path including the player id
     * Then the server keeps the request open until a new update needs to be done.
     * The server will send it back to this method and then the response will be handled.
     * Finally, the client directly sends back a new request.
     */
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            HttpRequest updateRequest = HttpRequest.newBuilder().GET()
                    .uri(URI.create(mainCtrl.getServerPath() + "api/players/getUpdates?playerId="
                            + this.mainCtrl.getUserID()))
                    .build();

            HttpResponse<String> updateResponse;
            try {
                updateResponse = HttpClient.newBuilder().build()
                        .send(updateRequest, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (updateResponse.statusCode() != 200) {
                System.out.println("An error occurred, the status code is: "
                        + updateResponse.statusCode());
            }

            this.doUpdate(updateResponse.body());
        }
    }


    /**
     * This method is to handle the updates from the server, it reads from Gson into the
     * list of updates. Then it checks the type of update.
     * For every update it handles it differently.
     * The "StartSP" starts the single-player game
     * The "loadingScreen" update contains the next question, it sets the question in the mainCtrl,
     * and it opens the loading screen
     * The "showAnswer" informs the client that the answer should be shown
     * The "showQuestion" informs the client that the question should be shown
     * The "showLeaderboardEndGame" informs the client that the leaderboard
     * at the end should be shown
     * The "showLeaderboardMidGame" informs the client that the leaderboard
     * at the middle should be shown
     * The "showEmote" informs the client that the should display the emote
     *
     * @param body the response from the server
     */
    private void doUpdate(String body) {
        Gson gson = new Gson();
        List<Update> updates = gson.fromJson(body, new TypeToken<List<Update>>() {
        }.getType());
        for (Update update : updates) {
            switch (update.getType()) {
                case "StartSP":
                    Platform.runLater(() -> {
                        this.mainCtrl.setGameID(update.getGameID());
                    });
                    break;
                case "loadingScreen":
                    Platform.runLater(() -> {
                        this.mainCtrl.showScene(6);
                    });
                    break;
                case "showAnswer":
                    Platform.runLater(() -> {
                        this.mainCtrl.setScore(update.getScoreToUpdate());
                        this.mainCtrl.showAnswer();
                    });
                    break;
                case "showQuestion":
                    Platform.runLater(() -> {
                        Question question = update.getQuestion();
                        this.mainCtrl.setQuestion(question);
                        this.mainCtrl.setQuestionLapCounter(mainCtrl.getQuestionLapCounter() + 1);
                        if (question.getType().equals("ComparisonQuestion"))
                            this.mainCtrl.showScene(7);
                        if (question.getType().equals("MCQuestion"))
                            this.mainCtrl.showScene(8);
                        if (question.getType().equals("OpenQuestion"))
                            this.mainCtrl.showScene(9);
                        if (question.getType().equals("GuessQuestion"))
                            this.mainCtrl.showScene(10);
                        if (question.getType().equals("InsteadQuestion"))
                            this.mainCtrl.showScene(13);
                        if (question.getType().equals("ClosestQuestion"))
                            this.mainCtrl.showScene(15);
                    });
                    break;
                case "showLeaderboardEndGame":
                    Platform.runLater(() -> {
                        this.mainCtrl.showScene(11);
                    });
                    break;
                case "showLeaderboardMidGame":
                    Platform.runLater(() -> {
                        this.mainCtrl.showScene(2);
                    });
                    break;
                case "showSingleplayerLeaderboardEndGame":
                    Platform.runLater(() -> {
                        this.mainCtrl.showScene(12);
                    });
                    break;
                case "showEmote":
                    Platform.runLater(() -> {
                        this.mainCtrl.showEmote(update.getEmote(), update.getUsername());
                    });
                    break;
                case "decreasetimeUpdate":
                    Platform.runLater(() -> {
                        this.mainCtrl.usedTimerJoker(update.getGameID());
                    });
                    break;
                default:
                    break;
            }
        }
    }


}
