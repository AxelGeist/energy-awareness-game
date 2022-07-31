package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RelaxCtrlTest {

    private MyMainCtrl myMainCtrl;
    private RelaxCtrl relaxCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        relaxCtrl = new RelaxCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(relaxCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
