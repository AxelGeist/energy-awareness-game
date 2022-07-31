package client.scenes;

import com.google.inject.Inject;
import commons.Question;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import java.util.Timer;

public class MCQuestionCtrl extends GameManager implements Ctrl {

    /**
     * Constructor for the ctrl
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public MCQuestionCtrl(MainCtrl mainCtrl) {
        super(mainCtrl);
        this.timer = new Timer();
    }

    /**
     * On load every text field is changed with the appropriate
     * string, and it also starts our progress bar.
     *
     * @param scene Our scene
     */
    @Override
    public void onLoad(Scene scene) {
        startScene();
        option1.setOnAction(e -> answer(1));
        option2.setOnAction(e -> answer(2));
        option3.setOnAction(e -> answer(3));
        option4.setOnAction(e -> answer(4));
    }

    /**
     * Method to start the scene
     */
    public void startScene() {
        this.mainCtrl.register(this);
        start();
        resetScene();
        displayQuestion();
    }

    /**
     * Display the question to the screen
     */
    public void displayQuestion() {
        Question q = mainCtrl.getQuestion();
        option1.setText(q.getUsedActivities().get(0).getTitle());
        option2.setText(q.getUsedActivities().get(1).getTitle());
        option3.setText(q.getUsedActivities().get(2).getTitle());
        option4.setText(q.getUsedActivities().get(3).getTitle());
    }

    /**
     * resets color, opacity, emotes and button functionality on scene
     */
    public void resetScene() {
        option1.setTextFill(Color.WHITE);
        option2.setTextFill(Color.WHITE);
        option3.setTextFill(Color.WHITE);
        option4.setTextFill(Color.WHITE);
        disableButtons(false);
    }


}
