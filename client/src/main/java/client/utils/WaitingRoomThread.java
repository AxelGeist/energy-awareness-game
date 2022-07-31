package client.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import client.scenes.MainCtrl;
import client.scenes.WaitingRoomCtrl;
import commons.Update;
import javafx.application.Platform;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public class WaitingRoomThread extends Thread implements Runnable {

    private final WaitingRoomCtrl waitingRoomCtrl;
    private final MainCtrl mainCtrl;

    /**
     * Constructor for the thread
     * @param waitingRoomCtrl the waiting room it is connected to
     * @param mainCtrl the main ctrl it is connected to
     */
    public WaitingRoomThread(WaitingRoomCtrl waitingRoomCtrl, MainCtrl mainCtrl) {
        this.waitingRoomCtrl = waitingRoomCtrl;
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
     * The "addPlayer" updates checks if the player is the client itself,
     * if not it adds it to the list of players
     * The "startGame" informs the client when the game is started,
     * it sets the gameId, and it opens the game screen.
     *
     * @param body the response from the server
     */
    private void doUpdate(String body) {
        Gson gson = new Gson();
        List<Update> updates = gson.fromJson(body, new TypeToken<List<Update>>() { } .getType());
        for (Update update : updates) {
            switch (update.getType()) {
                case "addPlayer":
                    Platform.runLater(() -> {
                        if (!update.getPlayer().getUsername()
                                .equals(mainCtrl.getUsername())) {
                            this.waitingRoomCtrl.getPlayers().addAll(update.getPlayer());
                            this.waitingRoomCtrl.refresh();
                        }
                    });
                    break;
                case "startGame":
                    Platform.runLater(() -> {
                        this.mainCtrl.setGameID(update.getGameID());
                        this.waitingRoomCtrl.ok();
                    });
                    break;
                case "leavePlayer":
                    Platform.runLater(() -> {
                        this.waitingRoomCtrl.getPlayers().remove(update.getPlayer());
                        this.waitingRoomCtrl.refresh();
                    });
                    break;
                default:
                    break;
            }
        }
    }

}
