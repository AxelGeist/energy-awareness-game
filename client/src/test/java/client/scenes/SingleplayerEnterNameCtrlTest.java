package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SingleplayerEnterNameCtrlTest {

    private MyMainCtrl myMainCtrl;
    private SingleplayerEnterNameCtrl singleplayerEnterNameCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        singleplayerEnterNameCtrl = new SingleplayerEnterNameCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(singleplayerEnterNameCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
