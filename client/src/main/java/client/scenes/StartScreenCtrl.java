package client.scenes;

import com.google.inject.Inject;
import javafx.scene.Scene;

public class StartScreenCtrl implements Ctrl {

    private final MainCtrl mainCtrl;


    /**
     * Constructor for the ctrl
     * @param mainCtrl the main ctrl it is connected to
     */
    @Inject
    public StartScreenCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;

    }

    /**
     * show the admin scene
     */
    public void showAdmin() { mainCtrl.showScene(14); }

    /**
     * Show the 'enter name' scene
     */
    public void multiplayerEnterName() {
        mainCtrl.showScene(1);
    }

    /**
     * Something temporary to help with questions
     */
    public void singleplayer() {
        mainCtrl.showScene(3);
    }

    /**
     * To load the scene
     *
     * @param scene the scene to load
     */
    @Override
    public void onLoad(Scene scene) {
        mainCtrl.setTitle("Start Screen");
    }

    /**
     * Show the help scene
     */
    public void Help() {
        mainCtrl.showScene(4);
    }

}
