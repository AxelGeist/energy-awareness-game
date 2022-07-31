package client.scenes;

import com.google.inject.Inject;
import javafx.scene.Scene;

import java.util.Timer;

public class InsteadQuestionCtrl extends GameManager implements Ctrl {

    /**
     * Constructor for the ctrl
     *
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public InsteadQuestionCtrl(MainCtrl mainCtrl) {
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
        displayImagesQuestion();
        openQuestionSolution.setText("");
    }

}
