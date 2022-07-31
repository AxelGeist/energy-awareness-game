package client.scenes;

import javafx.scene.Scene;
import java.util.EventListener;

public interface Ctrl extends EventListener {
    
    /**
     * A method to load the scene
     * @param scene the scene to load
     */
    void onLoad(Scene scene);
}
