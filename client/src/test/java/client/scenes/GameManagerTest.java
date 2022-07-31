package client.scenes;


import org.junit.jupiter.api.BeforeEach;

public class GameManagerTest {

    private MainCtrl sut;
    private GameManager GameManager;

    @BeforeEach
    public void setUp() {
        sut = new MainCtrl();
        GameManager = new GameManager(sut);

    }

    // Every method with ONLY javafx cannot be tested as this would require mockito,
    // which is not part of this course.

    // Test all the logic in the game manager,
    // for example the show MC answer can be split in 2 methods and tested for the logic

    // Also, the cancel can be tested if you create a fake "main Ctrl"
    // Do this by creating a custom class extending from mainCtrl
    // and override the cancelAndRemovePlayer
}
