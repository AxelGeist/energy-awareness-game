package client.scenes;

import com.google.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;

import java.util.Timer;

public class ComparisonQuestionCtrl extends GameManager implements Ctrl {

    @FXML
    private ImageView rightImage;

    @FXML
    private ImageView leftImage;


    /**
     * Constructor for the ctrl
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public ComparisonQuestionCtrl(MainCtrl mainCtrl) {
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
        displayImagesQuestion();
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
