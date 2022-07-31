package server.service;

import commons.Person;
import commons.Question;
import commons.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.*;
import server.database.DatabaseRepository;
import server.database.PlayerRepository;
import server.database.QuestionRepository;
import server.entity.Game;
import server.services.PlayerControllerService;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerControllerServiceTest {

    private TestPersonRepository personRepository;
    private TestQuestionRepository questionRepository;
    private TestDatabaseRepository databaseRepository;
    private PlayerControllerService playerControllerService;

    private MyPlayerController myPlayerController;
    private Person p1;
    private Person p2;
    private Game game;
    private Game emptyGame;

    @BeforeEach
    public void setup() {
        personRepository = new TestPersonRepository();
        personRepository.save(new Person("player1"));

        questionRepository = new TestQuestionRepository();
        questionRepository.save(new Question(300, "test", "test"));

        databaseRepository = new TestDatabaseRepository();
        databaseRepository.save(new Person("player1"));

        myPlayerController = new MyPlayerController(personRepository, questionRepository,
                databaseRepository);

        playerControllerService = new PlayerControllerService(myPlayerController,
                personRepository, questionRepository);

        p1 = new Person(1, "Tijmen", 0, 0);
        p2 = new Person(2, "Wout", 0, 0);

        Question q1 = new Question(1111, "Test", "Test");
        Question q2 = new Question(1111, "OpenQuestion", "Test");


        game = new Game(1, 10, playerControllerService);
        emptyGame = new Game(2, 10, playerControllerService);

        playerControllerService.addWaitingRoom(p1);
        playerControllerService.addWaitingRoom(p2);

        playerControllerService.addGame(game);

        game.addQuestion(q1);
        game.setCurrentQuestion(q1);

        game.addPlayer(p1);
        game.addPlayer(p2);

        emptyGame.addQuestion(q2);
        emptyGame.setCurrentQuestion(q1);
        emptyGame.addQuestion(q2);
    }

    @Test
    public void getLeaderboard() throws Exception {
        assertEquals(List.of(p1, p2), playerControllerService.getLeaderboard(1));
    }

    @Test
    public void getLeaderboardError() {
        assertThrows(Exception.class, () -> playerControllerService.getLeaderboard(3));
    }

    @Test
    public void deleteFromGames() {
        long playerID = p1.getId();
        playerControllerService.deleteFromGamesAndWaitingRoom(playerID);
        assertFalse(game.getPlayers().contains(p1));
    }

    @Test
    public void deleteFromWaitingRoom() {
        long playerID = p1.getId();
        playerControllerService.deleteFromGamesAndWaitingRoom(playerID);
        assertFalse(playerControllerService.getWaitingRoom().contains(p1));
    }

    @Test
    public void deleteFromEmptyWaitingRoom() {
        long playerID = 10L;
        playerControllerService.deleteFromGamesAndWaitingRoom(playerID);
        assertEquals(2, playerControllerService.getWaitingRoom().size());
    }

    @Test
    public void deleteNotExistingWaitingRoom() {
        long playerID = 10L;
        playerControllerService.setWaitingRoom(List.of());
        playerControllerService.deleteFromGamesAndWaitingRoom(playerID);
        assertEquals(0, playerControllerService.getWaitingRoom().size());
    }

    @Test
    public void showSinglePlayerLeaderboardEndGame() {
        playerControllerService.showSingleplayerLeaderboardEndGame(game);
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void showAnswer() {
        playerControllerService.showAnswer(game);
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void showAnswerOpenQuestion() {
        emptyGame.setCurrentQuestion(new Question(100L, "OpenQuestion", "a"));
        playerControllerService.showAnswer(emptyGame);
        assertFalse(myPlayerController.wasCalled);
    }

    @Test
    public void showAnswerEmptyGame() {
        playerControllerService.showAnswer(emptyGame);
        assertFalse(myPlayerController.wasCalled);
    }

    @Test
    public void showQuestion() {
        playerControllerService.showQuestion(game);
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void setGame() {
        playerControllerService.setGame(List.of(game));
        assertEquals(List.of(game), playerControllerService.getGame());
    }

    @Test
    public void getGame() {
        assertEquals(List.of(game), playerControllerService.getGame());
    }

    @Test
    public void startGameMultiplayer() {
        playerControllerService.startGameMultiplayer();
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void startGameSinglePlayer() {
        playerControllerService.startGameSingleplayer(p1);
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void showEmote() {
        playerControllerService.showEmote(1, "Wout", "cry");
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void handleJokerDoublePointsJoker() {
        playerControllerService.handleJoker(1, "double-point", 1);
        assertTrue(p1.getDoublePoints());
    }

    @Test
    public void handleDecreaseTimeJoker() {
        game.setStartOfCurrentQuestion(
                Date.from(Instant.parse("1980-04-09T10:15:30Z")));
        playerControllerService.handleJoker(1, "timer", 1);
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void showLoadingScreen() {
        playerControllerService.showLoadingScreen(game);
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void showLeaderboardMidGame() {
        playerControllerService.showLeaderboardMidGame(game);
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void showLeaderboardEndGame() {
        playerControllerService.showLeaderboardEndGame(game);
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void addPlayer() {
        playerControllerService.addPlayer("Axel");
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void joinWaitingRoom() {
        playerControllerService.joinWaitingRoom(p1);
        assertTrue(myPlayerController.wasCalled);
    }

    @Test
    public void testReceivedAnswer() {
        playerControllerService.receiveAnswer(1, 100L);
        assertEquals(100L, p1.getCurrentAnswer());
    }

    @Test
    public void deleteFromGame() {
        MyGame myGame = new MyGame(3, 10, playerControllerService);
        Person p3 = new Person(14, "43", 3, 3);
        myGame.addPlayer(p3);
        playerControllerService.addGame(myGame);
        playerControllerService.deleteFromGamesAndWaitingRoom(14);
        assertTrue(myGame.wasCalled);
    }

    public static class MyPlayerController extends PlayerController {

        public boolean wasCalled = false;

        /**
         * Method for the player controller
         *
         * @param playerRepo   the player repo
         * @param questionRepo the question repo
         * @param databaseRepo the database repo
         */
        public MyPlayerController(PlayerRepository playerRepo,
                                  QuestionRepository questionRepo,
                                  DatabaseRepository databaseRepo) {
            super(playerRepo, questionRepo, databaseRepo);
        }

        @Override
        public void processNewUpdate(Update update, long id) {
            wasCalled = true;
        }
    }


    public static class MyGame extends Game {

        public boolean wasCalled = false;

        /**
         * Constructor
         * @param id the id
         * @param questionTime the question time
         * @param playerControllerService the playerControllerService
         */
        public MyGame(long id, int questionTime, PlayerControllerService playerControllerService) {
            super(id, questionTime, playerControllerService);
        }

        @Override
        public void stop() {
            wasCalled = true;
        }
    }
}
