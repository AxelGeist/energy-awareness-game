package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HelpCtrlTest {

    private MyMainCtrl myMainCtrl;
    private HelpCtrl helpCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        helpCtrl = new HelpCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(helpCtrl);
    }

    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
