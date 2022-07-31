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
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;


public class AddPersonCtrl implements Ctrl {

    private final MainCtrl mainCtrl;

    @FXML
    private TextField username;

    @FXML
    private TextField servername;

    @FXML
    private Label help;

    /**
     * Constructor for the ctrl
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public AddPersonCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Return to main menu with alert message
     */
    public void cancel() {
        mainCtrl.cancel();
    }


    /**
     * Try to add the person to the database if possible, otherwise
     * raise a flag
     */
    public void ok() {
        try {
            if (!servername.getText().equals("")) this.mainCtrl.setServer(servername.getText());

            this.mainCtrl.getServer().testConnection();
        } catch (Exception e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("The server path '" + servername.getText() + "' is invalid.");
            alert.setContentText("Please choose a different server path.");
            alert.setTitle("Invalid server");
            alert.showAndWait();
            this.mainCtrl.setServer("http://localhost:8080/");
            return;
        }

        try {
            Person p = mainCtrl.getServer().addPerson(username.getText());
            this.mainCtrl.setUsername(p.getUsername());
            this.mainCtrl.setUserID(p.getId());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("The username '" + username.getText() + "' is already in use.");
            alert.setContentText("Please choose a different username.");
            alert.setTitle("Invalid username");
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showScene(5);
    }


    /**
     * To clear the fields in the screen
     */
    private void clearFields() {
        servername.clear();
        username.clear();
    }

    /**
     * Detect if a key is pressed to call certain functions
     * @param e the pressed key
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * To load the scene, also set the max length for username to 13 characters
     * @param scene the scene to load
     */
    @Override
    public void onLoad(Scene scene) {
        clearFields();
        help.setVisible(false);
        mainCtrl.setTitle("Enter name");
        scene.setOnKeyPressed(this::keyPressed);
        username.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 13 ? change : null)
        );
    }

    /**
     * Method to show the help label
     */
    public void help() {
        this.help.setVisible(!help.isVisible());
    }
}