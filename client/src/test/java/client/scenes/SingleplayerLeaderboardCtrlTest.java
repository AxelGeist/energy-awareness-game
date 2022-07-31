package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SingleplayerLeaderboardCtrlTest {

    private MyMainCtrl myMainCtrl;
    private SingleplayerLeaderboardCtrl singleplayerLeaderboardCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        singleplayerLeaderboardCtrl = new SingleplayerLeaderboardCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(singleplayerLeaderboardCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
