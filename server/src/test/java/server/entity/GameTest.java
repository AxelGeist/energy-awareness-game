package server.entity;

import commons.Person;
import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.PlayerController;
import server.api.TestDatabaseRepository;
import server.api.TestPersonRepository;
import server.api.TestQuestionRepository;
import server.database.PlayerRepository;
import server.database.QuestionRepository;
import server.services.PlayerControllerService;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Person p;
    private Person q;
    private Set<Person> l;
    private Game g;
    private MyTimer t;
    private MyPlayerControllerService playerControllerService;

    @BeforeEach
    void setup() {
        p = new Person("Vlad");
        q = new Person("Thijs");
        l = new HashSet<>();
        t = new MyTimer();
        TestPersonRepository personRepository = new TestPersonRepository();
        TestQuestionRepository questionRepository = new TestQuestionRepository();
        TestDatabaseRepository databaseRepository = new TestDatabaseRepository();
        PlayerController playerController = new PlayerController(personRepository,
                questionRepository, databaseRepository);
        playerControllerService = new MyPlayerControllerService(playerController,
                personRepository, questionRepository);
        g = new Game(List.of(q), 2L, 5, List.of(new Question(50, "test", "."))
                , playerControllerService, t);

    }

    @Test
    public void checkConstructor() {
        assertTrue(l.isEmpty());
    }

    @Test
    public void getPlayersTest() {
        l.add(q);
        assertEquals(g.getPlayers(), l);
    }

    @Test
    public void addPlayerTest() {
        g.addPlayer(p);
        l.add(p);
        l.add(q);
        assertEquals(g.getPlayers(), l);
    }

    @Test
    public void toStringTest() {
        l.add(p);
        l.add(q);
        Game g = new Game(new ArrayList<>(l), 2L, 5, List.of(new Question(4, "test", ".")),
                playerControllerService, new Timer());
        assertEquals("Game{id=" + g.getId() + ", players=" + l + ", amountOfQuestions=20}"
                , g.toString());
    }

    @Test
    public void getIdTest() {
        g.setId(123);
        assertEquals(123, g.getId());
    }

    @Test
    void getStartOfCurrentQuestion() {
        assertNull(g.getStartOfCurrentQuestion());
    }

    @Test
    void setStartOfCurrentQuestion() {
        g.setStartOfCurrentQuestion(
                Date.from(Instant.parse("1980-04-09T10:15:30Z")));
        assertEquals(Date.from(Instant.parse("1980-04-09T10:15:30Z")),
                g.getStartOfCurrentQuestion());
    }

    @Test
    void setStartOfCurrentQuestionNull() {
        g.setStartOfCurrentQuestion(null);
        assertNull(g.getStartOfCurrentQuestion());
    }

    @Test
    void getCurrentQuestion() {
        Question question = new Question(100, "test", ".");
        g.setCurrentQuestion(question);
        assertEquals(question, g.getCurrentQuestion());
    }

    @Test
    void setCurrentQuestion() {
        Question question = new Question(100, "test", ".");
        g.setCurrentQuestion(question);
        assertEquals(question, g.getCurrentQuestion());
    }

    @Test
    void getIsSingleplayer() {
        g.setSingleplayer(true);
        assertTrue(g.getIsSingleplayer());
    }

    @Test
    void setSingleplayer() {
        g.setSingleplayer(true);
        assertTrue(g.getIsSingleplayer());
    }

    @Test
    void setCounter() {
        g.setCounter(3);
        assertEquals(3, g.getCounter());
    }

    @Test
    public void removePlayerTest() {
        g.removePlayer(q);
        assertEquals(Set.of(), g.getPlayers());
    }

    @Test
    public void getQuestionsTest() {
        assertEquals(List.of(new Question(50, "test", ".")), g.getQuestions());
    }

    @Test
    public void start() {
        g.start();
        assertTrue(t.wasCalled);
    }

    @Test
    public void showQuestion() {
        g.showQuestion();
        assertTrue(t.wasCalled);
        assertEquals(1, playerControllerService.wasCalled);
    }

    @Test
    public void showAnswer() {
        g.showAnswer();
        assertTrue(t.wasCalled);
        assertEquals(2, playerControllerService.wasCalled);
    }

    @Test
    public void showLoadingScreen() {
        g.showLoadingScreen();
        assertTrue(t.wasCalled);
        assertEquals(3, playerControllerService.wasCalled);
    }

    @Test
    public void showSingleplayerLeaderboard() {
        g.showSingleplayerLeaderboard();
        assertEquals(4, playerControllerService.wasCalled);
    }

    @Test
    public void showMidGameLeaderboard() {
        g.showMidGameLeaderboard();
        assertTrue(t.wasCalled);
        assertEquals(5, playerControllerService.wasCalled);
    }

    @Test
    public void showLoadingAfterLeaderboard() {
        g.showLoadingAfterLeaderboard();
        assertTrue(t.wasCalled);
        assertEquals(3, playerControllerService.wasCalled);
    }

    @Test
    public void showEndGameLeaderboard() {
        g.showEndGameLeaderboard();
        assertEquals(6, playerControllerService.wasCalled);
    }

    @Test
    public void stop() {
        g.stop();
        assertTrue(t.wasCalled);
    }

    @Test
    public void showCorrectScreenTestMidGame() {
        g.setCounter(10);
        g.setSingleplayer(false);
        g.showCorrectScreenAfterLoading();
        assertTrue(t.wasCalled);
        assertEquals(5, playerControllerService.wasCalled);
    }

    @Test
    public void showCorrectScreenTestSinglePlayer() {
        g.setCounter(20);
        g.setSingleplayer(true);
        g.showCorrectScreenAfterLoading();
        assertEquals(4, playerControllerService.wasCalled);
    }

    @Test
    public void showCorrectScreenTestEndGame() {
        g.setCounter(20);
        g.setSingleplayer(false);
        g.showCorrectScreenAfterLoading();
        assertEquals(6, playerControllerService.wasCalled);
    }

    @Test
    public void showCorrectScreenTestQuestion() {
        g.setCounter(6);
        g.setSingleplayer(false);
        g.showCorrectScreenAfterLoading();
        assertEquals(1, playerControllerService.wasCalled);
    }


    public static class MyTimer extends Timer {

        public boolean wasCalled = false;

        @Override
        public void schedule(TimerTask t, long delay)  {
            wasCalled = true;

        }

        @Override
        public void cancel() {
            wasCalled = true;
        }
    }

    public static class MyPlayerControllerService extends PlayerControllerService {

        public int wasCalled = 0;
        /**
         * The api endpoint for the players
         *
         * @param playerRepository   The player repository
         * @param questionRepository The question repository
         * @param playerController The controller of the service
         */
        public MyPlayerControllerService(PlayerController playerController,
                                         PlayerRepository playerRepository,
                                         QuestionRepository questionRepository) {
            super(playerController, playerRepository, questionRepository);
        }

        @Override
        public void showQuestion(Game g) {
            wasCalled = 1;
        }

        @Override
        public void showAnswer(Game g) {
            wasCalled = 2;
        }

        @Override
        public void showLoadingScreen(Game g) {
            wasCalled = 3;
        }

        @Override
        public void showSingleplayerLeaderboardEndGame(Game g) {
            wasCalled = 4;
        }

        @Override
        public void showLeaderboardMidGame(Game g) {
            wasCalled = 5;
        }

        @Override
        public void showLeaderboardEndGame(Game g) {
            wasCalled = 6;
        }
    }


}
