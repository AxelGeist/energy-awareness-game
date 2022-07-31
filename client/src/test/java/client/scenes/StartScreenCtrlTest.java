package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StartScreenCtrlTest {

    private MyMainCtrl myMainCtrl;
    private StartScreenCtrl startScreenCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        startScreenCtrl = new StartScreenCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(startScreenCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
