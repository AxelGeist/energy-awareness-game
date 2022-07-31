/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainCtrlTest {

    private MainCtrl sut;
    private MyGameManager myGameManager;

    @BeforeEach
    public void setUp() {
        sut = new MainCtrl();
        myGameManager = new MyGameManager(sut);
        sut.register(myGameManager);
    }

    @Test
    void getGameID() {
        assertEquals(0, sut.getGameID());
    }

    @Test
    void setAndGetGameID() {
        long ID = 10;
        sut.setGameID(ID);
        assertEquals(ID, sut.getGameID());
    }

    @Test
    public void showEmote() {
        sut.showEmote("test", "test");
        assertEquals(1, myGameManager.wasCalled);
    }

    @Test
    public void showAnswer() {
        sut.showAnswer();
        assertEquals(2, myGameManager.wasCalled);
    }

    @Test
    void getQuestion() {
        assertNull(sut.getQuestion());
    }

    @Test
    void setAndGetQuestion() {
        Question q = new Question(1, "test", "test");
        sut.setQuestion(q);
        assertEquals(q, sut.getQuestion());
    }

    @Test
    void getSingleplayer() {
        assertFalse(sut.isSingleplayer());
    }

    @Test
    void setAndGetSingleplayer() {
        sut.setSingleplayer(true);
        assertTrue(sut.isSingleplayer());
    }

    @Test
    void getScore() {
        assertEquals(0, sut.getScore());
    }

    @Test
    void setAndGetScore() {
        sut.setScore(1);
        assertEquals(1, sut.getScore());
    }

    @Test
    void getUserId() {
        assertEquals(0, sut.getUserID());
    }

    @Test
    void setAndGetUserId() {
        sut.setUserID(6L);
        assertEquals(6L, sut.getUserID());
    }

    @Test
    void getUsername() {
        assertNull(sut.getUsername());
    }

    @Test
    void setAndGetUsername() {
        sut.setUsername("test");
        assertEquals("test", sut.getUsername());
    }

    @Test
    void getUsedDoublePoints() {
        assertFalse(sut.getUsedDoublePoints());
    }

    @Test
    void setAndGetgetUsedDoublePoints() {
        sut.setUsedDoublePoints(true);
        assertTrue(sut.getUsedDoublePoints());
    }

    @Test
    void getUsedTimer() {
        assertFalse(sut.getUsedTimer());
    }

    @Test
    void setAndgetUsedTimer() {
        sut.setUsedTimer(true);
        assertTrue(sut.getUsedTimer());
    }

    @Test
    void getUsedRemoveAnswer() {
        assertFalse(sut.getUsedRemoveAnswer());
    }

    @Test
    void setAndgetUsedRemoveAnswer() {
        sut.setUsedRemoveAnswer(true);
        assertTrue(sut.getUsedRemoveAnswer());
    }

    @Test
    void getQuestionLapCounter() {
        assertEquals(0, sut.getQuestionLapCounter());
    }

    @Test
    void setAndgetQuestionLapCounter() {
        sut.setQuestionLapCounter(1);
        assertEquals(1, sut.getQuestionLapCounter());
    }



    public static class MyGameManager extends GameManager {

        public int wasCalled = 0;

        /**
         * Constructor of game-scene controller
         *
         * @param mainCtrl main control
         */
        public MyGameManager(MainCtrl mainCtrl) {
            super(mainCtrl);
        }

        @Override
        public void showEmote(String emote, String username) {
            wasCalled = 1;
        }

        @Override
        public void showAnswer() {
            wasCalled = 2;
        }
    }

}