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
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class RelaxCtrl implements Ctrl {

    public MainCtrl mainCtrl;

    @FXML
    private ProgressBar progress;
    @FXML
    private ImageView blades;

    /**
     * Constructor for the ctrl
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public RelaxCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Cancel calls the cancel in mainCtrl which closes the window
     */
    public void cancel() {
        mainCtrl.cancelAndRemovePlayer();
    }

    /**
     * Load the screen, set up a loading bar with 5 seconds and rotate the blades
     * @param scene The scene that is loaded
     */
    @Override
    public void onLoad(Scene scene) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(this.progress.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3.0), e -> {
                }, new KeyValue(this.progress.progressProperty(), 1))
        );
        timeline.setCycleCount(1);
        timeline.play();
        mainCtrl.setTitle("Loading...");
        int rotateDuration = 1;
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(blades);
        rotate.setDuration(Duration.seconds(rotateDuration));
        rotate.setCycleCount(5);
        rotate.setByAngle(360);
        rotate.play();
    }
}

