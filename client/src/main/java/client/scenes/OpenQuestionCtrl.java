package client.scenes;

import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.ByteArrayInputStream;
import java.util.Timer;

public class OpenQuestionCtrl extends GameManager implements Ctrl {

    @FXML
    private Label activity_front;

    @FXML
    private Label activity_back;

    @FXML
    private ImageView image;

    /**
     * Constructor for the ctrl
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public OpenQuestionCtrl(MainCtrl mainCtrl) {
        super(mainCtrl);
        timer = new Timer();
    }

    /**
     * On load every text field is changed with the appropriate
     * string, and it also starts our progress bar.
     *
     * @param scene Our scene
     */
    @Override
    public void onLoad(Scene scene) {
        answer.clear();
        start();
        this.mainCtrl.register(this);
        disableButtons(false);
        displayQuestion();
        openQuestionSolution.setText("");
    }


    /**
     * Display the question + image to the screen
     */
    public void displayQuestion() {
        Question q = mainCtrl.getQuestion();
        activity_front.setText(q.getUsedActivities().get(0).getTitle());
        activity_back.setText(q.getUsedActivities().get(0).getTitle());

        image.setImage(new Image(new ByteArrayInputStream(
                this.mainCtrl.getServer().getImage(q.getUsedActivities().get(0).getPath()))));
    }
}
