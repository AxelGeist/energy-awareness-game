package client.scenes;

import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;

public class GuessQuestionCtrl extends GameManager implements Ctrl {


    @FXML
    private Label activity;

    @FXML
    private Label activityShadow;

    private List<Long> list;

    /**
     * Constructor for the ctrl
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public GuessQuestionCtrl(MainCtrl mainCtrl) {
        super(mainCtrl);
        this.timer = new Timer();
        this.list = new ArrayList<>();
    }

    /**
     * On load every text field is changed with the appropriate
     * string, and it also starts our progress bar.
     *
     * @param scene Our scene
     */
    @Override
    public void onLoad(Scene scene) {
        this.mainCtrl.register(this);
        start();
        resetScene();
        displayQuestion();

        option1.setOnAction(e -> guess(0));
        option2.setOnAction(e -> guess(1));
        option3.setOnAction(e -> guess(2));
        option4.setOnAction(e -> guess(3));
    }

    /**
     * Enter the answer for guess question
     *
     * @param i the number of the answer
     */
    public void guess(int i) {
        disableButtons(true);
        if (i == 0) option1.setTextFill(Color.YELLOW);
        if (i == 1) option2.setTextFill(Color.YELLOW);
        if (i == 2) option3.setTextFill(Color.YELLOW);
        if (i == 3) option4.setTextFill(Color.YELLOW);
        mainCtrl.getServer().sendAnswer(list.get(i), mainCtrl.getUserID());
    }

    /**
     * shows the right & wrong answer on current scene
     */
    public void showMCAnswer() {
        int k = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == this.mainCtrl.getQuestion().getAnswer()) {
                k = i;
            }
        }

        if (k == 0)
            checkMark1.setVisible(true);
        if (k == 1)
            checkMark2.setVisible(true);
        if (k == 2)
            checkMark3.setVisible(true);
        if (k == 3)
            checkMark4.setVisible(true);

        if (k != 0)
            crossMark1.setVisible(true);
        if (k != 1)
            crossMark2.setVisible(true);
        if (k != 2)
            crossMark3.setVisible(true);
        if (k != 3)
            crossMark4.setVisible(true);

        disableAllJokers();
        refreshScoreAndTimer(5.0);
    }

    /**
     * Display the question to the screen
     */
    public void displayQuestion() {
        Question q = mainCtrl.getQuestion();
        activity.setText(q.getUsedActivities().get(0).getTitle());
        activityShadow.setText(q.getUsedActivities().get(0).getTitle());

        String[] values = q.getAnswers().split(",");
        list = Arrays.stream(values).map(Long::parseLong).collect(Collectors.toList());

        option1.setText(list.get(0) + " watt/h");
        option2.setText(list.get(1) + " watt/h");
        option3.setText(list.get(2) + " watt/h");
        option4.setText(list.get(3) + " watt/h");
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
