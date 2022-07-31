package client.scenes;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.Timer;

public class ClosestQuestionCtrl extends GameManager implements Ctrl {

    @FXML
    private ImageView rightImage;
    @FXML
    private ImageView leftImage;
    @FXML
    private Button leftAnswer;
    @FXML
    private Button rightAnswer;


    /**
     * Constructor for the ctrl
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public ClosestQuestionCtrl(MainCtrl mainCtrl) {
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
        this.mainCtrl.register(this);
        start();
        displaySingleplayerScreen(mainCtrl.isSingleplayer());
        disableButtons(false);
        rightImage.setOpacity(1);
        leftImage.setOpacity(1);
        displayImagesQuestion();
    }

    /**
     * Disables the answer buttons
     *
     * @param trigger whether the buttons should be disabled.
     */
    public void disableButtons(boolean trigger) {
        leftAnswer.setDisable(trigger);
        rightAnswer.setDisable(trigger);
    }

    /**
     * send answer to server
     */
    public void answerLeft() {
        disableButtons(true);
        rightImage.setOpacity(0.3);
        mainCtrl.getServer().sendAnswer(mainCtrl.getQuestion().getUsedActivities()
                .get(0).getConsumption(), mainCtrl.getUserID());
    }

    /**
     * send answer to server
     */
    public void answerRight() {
        disableButtons(true);
        leftImage.setOpacity(0.3);
        mainCtrl.getServer().sendAnswer(mainCtrl.getQuestion().getUsedActivities()
                .get(1).getConsumption(), mainCtrl.getUserID());
    }

}

