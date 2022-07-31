package client.scenes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LeaderboardEndGameCtrlTest {

    private MyMainCtrl myMainCtrl;
    private LeaderboardEndGameCtrl leaderboardEndGameCtrl;

    @BeforeEach
    public void setUp() {
        myMainCtrl = new MyMainCtrl();
        leaderboardEndGameCtrl = new LeaderboardEndGameCtrl(myMainCtrl);
    }


    @Test
    public void constructor() {
        assertNotNull(leaderboardEndGameCtrl);
    }

    public static class MyMainCtrl extends MainCtrl {

        public int wasCalled = 0;

        @Override
        public void register(GameManager listener) {
            wasCalled ++;
        }
    }

}
