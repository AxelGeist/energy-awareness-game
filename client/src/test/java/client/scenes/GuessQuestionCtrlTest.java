package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GuessQuestionCtrlTest {

    private MyMainCtrl myMainCtrl;
    private GuessQuestionCtrl guessQuestionCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        guessQuestionCtrl = new GuessQuestionCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(guessQuestionCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
