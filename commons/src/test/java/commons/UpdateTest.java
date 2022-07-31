package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UpdateTest {

    private Person p;
    private Question q;
    private Update playerUpdate;
    private Update gameIDUpdate;
    private Update leaderboardUpdate;
    private Update scoreUpdate;
    private Update questionUpdate;
    private Update emptyUpdate;
    private Update emoteUpdate;

    @BeforeEach
    void setUp() {
        p = new Person("A");
        playerUpdate = new Update("addPlayer", p);
        gameIDUpdate = new Update("startGame", 0L);
        leaderboardUpdate = new Update("leaderboardUpdate", Set.of(p));
        scoreUpdate = new Update("updateScore", 10);
        q = new Question(100, "OpenQuestion", ",");
        questionUpdate = new Update("addQuestion", q);
        emptyUpdate = new Update("type");
        emoteUpdate = new Update("type", "cry", "name");
    }

    @Test
    void constructorTest() {
        assertNotNull(playerUpdate);
        assertNotNull(gameIDUpdate);
        assertNotNull(leaderboardUpdate);
        assertNotNull(scoreUpdate);
        assertNotNull(questionUpdate);
        assertNotNull(emptyUpdate);
        assertNotNull(emoteUpdate);
    }

    @Test
    void getQuestion() {
        assertEquals(q, questionUpdate.getQuestion());
    }

    @Test
    void getScoreToUpdate() {
        assertEquals(10, scoreUpdate.getScoreToUpdate());
    }

    @Test
    void getGameID() {
        assertEquals(0L, gameIDUpdate.getGameID());
    }

    @Test
    void setQuestion() {
        Question q2 = new Question(4, "test", ",");
        questionUpdate.setQuestion(q2);
        assertEquals(q2, questionUpdate.getQuestion());
    }

    @Test
    void setScoreToUpdate() {
        scoreUpdate.setScoreToUpdate(20);
        assertEquals(20, scoreUpdate.getScoreToUpdate());
    }

    @Test
    void getType() {
        assertEquals("addPlayer", playerUpdate.getType());
    }

    @Test
    void getPlayer() {
        assertEquals(p, playerUpdate.getPlayer());
    }

    @Test
    void testToString() {
        assertEquals("Update{" +
                        "type='addPlayer'" +
                        ", player=" + p +
                        "}"
                , playerUpdate.toString());
    }

    @Test
    void getEmote() {
        assertEquals("cry", emoteUpdate.getEmote());
    }

    @Test
    void getName() {
        assertEquals("name", emoteUpdate.getUsername());
    }
}