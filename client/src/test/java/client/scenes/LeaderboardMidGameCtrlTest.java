package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LeaderboardMidGameCtrlTest {

    private MyMainCtrl myMainCtrl;
    private LeaderboardMidGameCtrl leaderboardMidGameCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        leaderboardMidGameCtrl = new LeaderboardMidGameCtrl(myMainCtrl);
    }

    @Test
    public void constructor() {
        assertNotNull(leaderboardMidGameCtrl);
    }


    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
