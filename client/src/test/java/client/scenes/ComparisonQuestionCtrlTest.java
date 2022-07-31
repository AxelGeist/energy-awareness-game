package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ComparisonQuestionCtrlTest {

    private MyMainCtrl myMainCtrl;
    private ComparisonQuestionCtrl comparisonQuestionCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        comparisonQuestionCtrl = new ComparisonQuestionCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(comparisonQuestionCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
