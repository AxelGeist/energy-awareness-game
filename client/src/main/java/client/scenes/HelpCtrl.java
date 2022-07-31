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
import javafx.scene.Scene;

public class HelpCtrl implements Ctrl {

    public MainCtrl mainCtrl;

    /**
     * Constructor for the ctrl
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public HelpCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * To go back to the main menu
     */
    public void cancel() {
        mainCtrl.showScene(0);
    }

    /**
     * To load the scene
     * @param scene the scene to load
     */
    @Override
    public void onLoad(Scene scene) {

    }
}