package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OpenQuestionCtrlTest {

    private MyMainCtrl myMainCtrl;
    private OpenQuestionCtrl openQuestionCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        openQuestionCtrl = new OpenQuestionCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(openQuestionCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
