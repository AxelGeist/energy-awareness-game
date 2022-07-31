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
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.util.Timer;
import java.util.TimerTask;


public class AdminCtrl implements Ctrl {

    @FXML
    private TextField name;

    @FXML
    private TextField consumption;

    @FXML
    private Label added;

    public MainCtrl mainCtrl;
    private Timer timer;

    /**
     * Constructor for the ctrl
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public AdminCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.timer = new Timer();
    }

    /**
     * Will add an activity to the database.
     * It will check if the input makes sense, and if it doesn't,
     * then it will show a popup. It also has a small label popup
     * where you can see that u added an activity.
     */
    public void addActivity() {
        long amount;
        try {
            amount = Long.parseLong(consumption.getText());
        } catch (NumberFormatException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText(consumption.getText() + " is not a number.");
            alert.setContentText("Please enter an integer.");
            alert.setTitle("Invalid input");
            alert.showAndWait();
            return;
        }
        if (name.getText().length() <= 10) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText(name.getText() + " is not a valid title.");
            alert.setContentText("Please insert a title that is bigger than 10 characters.");
            alert.setTitle("Invalid input");
            alert.showAndWait();
            return;
        }
        mainCtrl.getServer().addActivity(name.getText(), amount);
        name.clear();
        consumption.clear();

        added.setVisible(true);

        timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                added.setVisible(false);
            }
        }, 3000L);

    }

    /**
     * Cancel calls the cancel in mainCtrl which closes the window
     */
    public void cancel() {
        timer.cancel();
        timer = new Timer();
        mainCtrl.showScene(0);
    }

    /**
     * Load the screen, set up a loading bar with 5 seconds and rotate the blades
     *
     * @param scene The scene that is loaded
     */
    @Override
    public void onLoad(Scene scene) {
        added.setVisible(false);
    }
}

