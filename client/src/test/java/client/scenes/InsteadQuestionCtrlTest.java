package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InsteadQuestionCtrlTest {

    private MyMainCtrl myMainCtrl;
    private InsteadQuestionCtrl insteadQuestionCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        insteadQuestionCtrl = new InsteadQuestionCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(insteadQuestionCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
