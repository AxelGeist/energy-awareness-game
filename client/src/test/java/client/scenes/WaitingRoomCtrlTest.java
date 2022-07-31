package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WaitingRoomCtrlTest {

    private MyMainCtrl myMainCtrl;
    private WaitingRoomCtrl waitingRoomCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        waitingRoomCtrl = new WaitingRoomCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(waitingRoomCtrl);
    }

    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
