package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AddPersonCtrlTest {

    private MyMainCtrl myMainCtrl;
    private AddPersonCtrl addPersonCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        addPersonCtrl = new AddPersonCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(addPersonCtrl);
    }

    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
