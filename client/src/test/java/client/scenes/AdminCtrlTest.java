package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AdminCtrlTest {

    private MyMainCtrl myMainCtrl;
    private AdminCtrl adminCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        adminCtrl = new AdminCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(adminCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
