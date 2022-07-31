package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MCQuestionCtrlTest {

    private MyMainCtrl myMainCtrl;
    private MyMCQuestionCtrl mcQuestionCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        mcQuestionCtrl = new MyMCQuestionCtrl(myMainCtrl);

    }

    // The rest of this file is not testable, as it is only logic on javafx components
    // Which means that this file is tested to the maximum of our capabilities

    @Test
    public void constructor() {
        assertNotNull(mcQuestionCtrl);
    }

    @Test
    public void startScene() {
        mcQuestionCtrl.startScene();
        assertEquals(3, mcQuestionCtrl.wasCalled);
        assertEquals(1, myMainCtrl.wasCalled);
    }

    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }

    }

    public static class MyMCQuestionCtrl extends MCQuestionCtrl {

        public int wasCalled = 0;

        /**
         * Constructor for the ctrl
         *
         * @param mainCtrl the main ctrl it is connected to
         */
        public MyMCQuestionCtrl(MainCtrl mainCtrl) {
            super(mainCtrl);
        }

        @Override
        public void start() {
            wasCalled++;
        }

        @Override
        public void resetScene() {
            wasCalled++;
        }

        @Override
        public void displayQuestion() {
            wasCalled++;
        }

    }
}
