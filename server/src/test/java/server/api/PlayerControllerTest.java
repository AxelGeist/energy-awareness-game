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
package server.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import commons.Person;
import commons.Question;
import commons.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.database.PlayerRepository;
import server.database.QuestionRepository;
import server.entity.Game;
import server.services.PlayerControllerService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PlayerControllerTest {

    private TestPersonRepository personRepository;
    private TestQuestionRepository questionRepository;
    private TestDatabaseRepository databaseRepository;

    private MyPlayerControllerService myPlayerControllerService;
    private PlayerController myPlayerController;

    private Person p1;
    private Person p2;

    @BeforeEach
    public void setup() {
        Person person = new  Person("player1");
        personRepository = new TestPersonRepository();
        personRepository.save(person);

        questionRepository = new TestQuestionRepository();
        questionRepository.save(new Question(300, "test", "test"));

        person.setSingleplayer(true);
        databaseRepository = new TestDatabaseRepository();
        databaseRepository.save(person);

        myPlayerController = new PlayerController(personRepository, questionRepository,
                databaseRepository);
        myPlayerControllerService = new MyPlayerControllerService(myPlayerController,
                personRepository, questionRepository);

        myPlayerController.setPlayerControllerService(myPlayerControllerService);

        p1 = new Person(1, "Tijmen", 0, 0);
        p2 = new Person(2, "Wout", 0, 0);

        Question q1 = new Question(1111, "Test", "Test");
        Question q2 = new Question(1111, "OpenQuestion", "Test");


        Game game = new Game(1, 10, myPlayerControllerService);
        Game emptyGame = new Game(2, 10, myPlayerControllerService);

        myPlayerControllerService.addWaitingRoom(p1);
        myPlayerControllerService.addWaitingRoom(p2);

        myPlayerControllerService.addGame(game);

        game.addQuestion(q1);
        game.setCurrentQuestion(q1);

        game.addPlayer(p1);
        game.addPlayer(p2);

        emptyGame.addQuestion(q2);
        emptyGame.setCurrentQuestion(q1);
        emptyGame.addQuestion(q2);

    }

    @Test
    public void cannotAddNullPerson() {
        var actual = myPlayerController.add(null);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void personDatabaseIsUsed() {
        assertTrue(personRepository.calledMethods.contains("save"));
    }

    @Test
    public void databaseDatabaseIsUsed() {
        assertTrue(databaseRepository.calledMethods.contains("save"));
    }

    @Test
    public void questionDatabaseIsUsed() {
        assertTrue(questionRepository.calledMethods.contains("save"));
    }

    @Test
    public void getAllPlayersStatusTest() {
        assertEquals(OK, myPlayerController.getAllPlayers().getStatusCode());
    }

    @Test
    public void getAllPlayersTest() {
        Person person = new Person("player1");
        person.setSingleplayer(true);
        assertEquals(ResponseEntity.ok(List.of(person)),
                myPlayerController.getAllPlayers());
    }

    @Test
    public void getAllTest() {
        Person p = new Person("Axel");
        myPlayerControllerService.setWaitingRoom(List.of(p));
        assertEquals(List.of(p), myPlayerController.getAll());
    }

    @Test
    public void leavePersonTest() {
        myPlayerController.leavePerson(1);
        assertEquals(7, myPlayerControllerService.wasCalled);
        assertTrue(personRepository.calledMethods.contains("findById"));
        assertTrue(personRepository.calledMethods.contains("deleteById"));
    }

    @Test
    public void getLeaderboard() throws Exception {
        assertEquals(List.of(p1, p2), myPlayerController.getLeaderboard(1L));
    }

    @Test
    public void getLeaderboardThrow() {
        assertThrows(Exception.class,
                () -> {
                    myPlayerController.getLeaderboard(2);
                });
    }

    @Test
    public void receiveAnswer() {
        myPlayerController.receiveAnswer(1L, 1L);
        assertEquals(9, myPlayerControllerService.wasCalled);
    }

    @Test
    public void receiveJoker() {
        myPlayerController.receiveJoker("DoublePoints", 1L, 1L);
        assertEquals(10, myPlayerControllerService.wasCalled);
    }

    @Test
    public void startGameSinglePlayer() {
        myPlayerController.startGameSingleplayer(p1);
        assertTrue(databaseRepository.calledMethods.contains("save"));
    }

    @Test
    public void getUpdate() {
        List<Update> updates = new ArrayList<>();
        updates.add(new Update("test"));

        myPlayerController.getNewUpdates().put(1L, new ArrayList<>(updates));

        assertEquals(updates, myPlayerController.getUpdates(1).getResult());
    }

    @Test
    public void addNull() {
        assertEquals(ResponseEntity.badRequest().build(), myPlayerController.add(null));
    }

    @Test
    public void addDuplicate() {
        myPlayerController.add(p1.getUsername());
        assertEquals(ResponseEntity.badRequest().build(),
                myPlayerController.add(p1.getUsername()));
    }

    @Test
    public void setDeferredUpdate() {
        myPlayerController.setDeferredUpdates(new HashMap<>());
        assertNotNull(myPlayerController.getDeferredUpdates());
    }

    @Test
    public void showEmote() {
        myPlayerController.showEmote(1, "e", "e");
        assertEquals(11, myPlayerControllerService.wasCalled);
    }

    @Test
    public void add() {
        assertEquals(ResponseEntity.badRequest().build(), myPlayerController.add(p1.getUsername()));
    }

    @Test
    public void modify() {
        databaseRepository.save(p1);
        assertEquals(ResponseEntity.ok(p1), myPlayerController.modify(1, 2));
    }

    @Test
    public void modifyEmpty() {
        assertEquals(ResponseEntity.badRequest().build(),
                myPlayerController.modify(-69, 2));
    }

    @Test
    public void getNoUpdate() {
        myPlayerController.setNewUpdates(new HashMap<>());
        assertTrue(myPlayerController.getNewUpdates().isEmpty());
    }

    @Test
    public void addSPNull() {
        assertEquals(ResponseEntity.badRequest().build(),
                myPlayerController.addSP(null));
    }

    @Test
    public void addSP() {
        p1.setSingleplayer(true);
        assertEquals(ResponseEntity.ok(p1),
                myPlayerController.addSP(p1.getUsername()));
    }

    @Test
    public void waitingRoomEmpty() {
        assertEquals(ResponseEntity.badRequest().build(),
                myPlayerController.waitingRoom(-4));
    }

    @Test
    public void waitingRoom() {
        myPlayerController.waitingRoom(1L);
        assertTrue(databaseRepository.calledMethods.contains("save"));
    }

    @Test
    public void startGameMultiplayer() {
        myPlayerController.startGameMultiplayer();
        assertEquals(8, myPlayerControllerService.wasCalled);
    }


    public static class MyPlayerControllerService extends PlayerControllerService {

        public int wasCalled = 0;

        /**
         * The api endpoint for the players
         *
         * @param playerRepository   The player repository
         * @param questionRepository The question repository
         * @param playerController   The controller of the service
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

        @Override
        public void deleteFromGamesAndWaitingRoom(long userID) {
            wasCalled = 7;
        }

        @Override
        public void startGameMultiplayer() {
            wasCalled = 8;
        }

        @Override
        public void receiveAnswer(long answer, long userId) {
            wasCalled = 9;
        }

        @Override
        public void handleJoker(long userId, String name, long gameID) {
            wasCalled = 10;
        }

        @Override
        public void showEmote(long gameID, String username, String emote) {
            wasCalled = 11;
        }
    }


}