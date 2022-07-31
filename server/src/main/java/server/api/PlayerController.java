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

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.PlayerRepository;
import server.database.QuestionRepository;
import server.database.DatabaseRepository;
import server.services.PlayerControllerService;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class PlayerController {

    private HashMap<Long, DeferredResult<List<Update>>> deferredUpdates = new HashMap<>();
    private HashMap<Long, List<Update>> newUpdates = new HashMap<>();

    private PlayerControllerService playerControllerService;
    private final PlayerRepository playerRepo;
    private final DatabaseRepository databaseRepo;

    /**
     * The api endpoint for the players
     *
     * @param playerRepo   The player repository
     * @param questionRepo The question repository
     * @param databaseRepo The database repository for singleplayer games
     */
    @Autowired
    public PlayerController(PlayerRepository playerRepo, QuestionRepository questionRepo
            , DatabaseRepository databaseRepo) {
        this.playerRepo = playerRepo;
        this.playerControllerService = new PlayerControllerService(this, playerRepo,
                questionRepo);
        this.databaseRepo = databaseRepo;
    }

    /**
     * Returns all the players that have participated in a singleplayer game.
     *
     * @return all the players in the singleplayer game
     */
    @GetMapping("/databaseSP/get")
    public ResponseEntity<List<Person>> getAllPlayers() {
        return ResponseEntity.ok(
                databaseRepo.findAll().stream()
                        .filter(Person::isSingleplayer)
                        .collect(Collectors.toList()));
    }

    /**
     * To this endpoint, the thread, either the waiting room thread or the game thread
     * makes requests. Then the result are set with an empty list and a time-out value.
     * After this time-out value it sends back an empty result.
     * If an update is added during the time-out value the time-out is stopped
     * and the update is sent back
     *
     * @param id - the id of the player which made the request
     * @return - deferred list of updates, deferred results are a built-in spring feature
     * which allows the server to wait until something needs to be sent to multiple clients.
     */
    @GetMapping("/players/getUpdates")
    @ResponseBody
    public DeferredResult<List<Update>> getUpdates(@RequestParam("playerId") long id) {
        DeferredResult<List<Update>> deferredResult =
                new DeferredResult<>(2000L, new ArrayList<>());

        deferredUpdates.put(id, deferredResult);

        if (newUpdates.get(id) != null
                && newUpdates.get(id).size() != 0) {
            deferredUpdates.get(id).setResult(newUpdates.get(id));
        }

        deferredResult.onCompletion(() -> {
            deferredUpdates.remove(id);
            newUpdates.remove(id);
        });

        return deferredResult;
    }

    /**
     * @return all persons
     */
    @GetMapping("/players")
    public List<Person> getAll() {
        return this.playerControllerService.getWaitingRoom();
    }


    /**
     * @param id the corresponding ID
     * @return all persons from the leaderboard
     */
    @GetMapping("/getLeaderboard/{id}")
    public List<Person> getLeaderboard(@PathVariable("id") long id) throws Exception {
        return this.playerControllerService.getLeaderboard(id);
    }


    /**
     * update the person
     *
     * @param userID The id of the person
     */
    @PostMapping("/players/leave")
    public void leavePerson(@RequestBody long userID) {
        Optional<Person> optionalPerson = playerRepo.findById(userID);
        if (optionalPerson.isEmpty()) return;
        Optional<Person> p = playerRepo.findById(userID);
        if (!p.get().isSingleplayer())
            playerRepo.deleteById(userID);

        playerControllerService.deleteFromGamesAndWaitingRoom(userID);
    }

    /**
     * The method to start the game, it starts the skeleton for the game
     */
    @GetMapping("/startGameMultiplayer")
    public void startGameMultiplayer() {
        this.playerControllerService.startGameMultiplayer();
    }

    /**
     * Add person singleplayer
     *
     * @param player the player in singleplayer
     */
    @PostMapping("/startGameSingleplayer")
    public void startGameSingleplayer(@RequestBody Person player) {
        this.playerControllerService.startGameSingleplayer(player);
    }

    /**
     * endpoint to add a person to the server
     *
     * @param username the name of the person
     * @return 200 ok
     */
    @PostMapping("/playersSP/addSP")
    public ResponseEntity<Person> addSP(@RequestBody String username) {
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }

        Person newPerson = new Person(username);
        newPerson.setSingleplayer(true);
        Person saved = databaseRepo.save(newPerson);

        return ResponseEntity.ok(saved);
    }

    /**
     * endpoint to modify a person in the server
     *
     * @param id    the name of the person
     * @param score the score we need to add to the leaderboard
     * @return 200 ok
     */
    @PostMapping("/playersSP/modify/{score}")
    public ResponseEntity<Person> modify(@RequestBody long id,
                                         @PathVariable("score") int score) {
        Optional<Person> p = databaseRepo.findById(id);
        if (p.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Person person = p.get();
        person.setScore(score);

        return ResponseEntity.ok(databaseRepo.save(person));
    }

    /**
     * Show the emote to all the clients
     *
     * @param gameID   the corresponding gameID
     * @param emote    the String of the emote to display
     * @param username the name of the user that sent the emote
     */
    @PostMapping("/emote/{gameID}/{name}")
    public void showEmote(@PathVariable("gameID") long gameID,
                          @PathVariable("name") String username, @RequestBody String emote) {
        this.playerControllerService.showEmote(gameID, username, emote);
    }


    /**
     * Receive answers from players (works almost the same as updatePlayer
     * but also keeps track of when they answered.)
     *
     * @param answer the current answer of the player
     * @param userId the id of the current user
     */
    @PostMapping("/players/answer/{userId}")
    public void receiveAnswer(@RequestBody long answer,
                              @PathVariable("userId") long userId) {
        this.playerControllerService.receiveAnswer(userId, answer);
    }

    /**
     * Receive jokers from players (works almost the same as updatePlayer
     * but also keeps track of when they answered.)
     *
     * @param gameID the id of the current game
     * @param name   the name of the joker
     * @param userID the id of the user
     */
    @PostMapping("/players/joker/{gameID}/{userID}")
    public void receiveJoker(@RequestBody String name,
                             @PathVariable("gameID") long gameID,
                             @PathVariable("userID") long userID) {
        this.playerControllerService.handleJoker(userID, name, gameID);
    }

    /**
     * endpoint to add a person to the server
     *
     * @param username the name of the person
     * @return 200 ok
     */
    @PostMapping("/players/add")
    public ResponseEntity<Person> add(@RequestBody String username) {
        if (username == null) {
            return ResponseEntity.badRequest().build();
        }

        Person saved = this.playerControllerService.addPlayer(username);
        if (saved == null)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(saved);
    }

    /**
     * Adds a person directly to the waiting room
     *
     * @param userID the person to be added
     * @return the added person
     */
    @PostMapping("/players/waitingRoom")
    public ResponseEntity<Person> waitingRoom(@RequestBody long userID) {
        Optional<Person> optionalPerson = playerRepo.findById(userID);
        if (optionalPerson.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Person person = this.playerControllerService.joinWaitingRoom(optionalPerson.get());

        return ResponseEntity.ok(playerRepo.save(person));
    }


    /**
     * Handle new update, inform the long polling requests that new update is
     * available and if no request exists from the user (for example because
     * of slow internet or slow computer),
     * then add the update to the list of awaited updates for the user.
     *
     * @param update - the new update to inform users about
     * @param id     - the id of the user to send the request to
     */
    public void processNewUpdate(Update update, long id) {
        List<Update> updates = new ArrayList<>();
        updates.add(update);

        if (deferredUpdates.get(id) != null) deferredUpdates.get(id).setResult(updates);
        else {
            if (newUpdates.get(id) == null) newUpdates.put(id, updates);
            else newUpdates.get(id).addAll(updates);
        }
    }

    public void setPlayerControllerService(PlayerControllerService playerControllerService) {
        this.playerControllerService = playerControllerService;
    }

    public HashMap<Long, DeferredResult<List<Update>>> getDeferredUpdates() {
        return deferredUpdates;
    }

    public void setDeferredUpdates(HashMap<Long, DeferredResult<List<Update>>> deferredUpdates) {
        this.deferredUpdates = deferredUpdates;
    }

    public HashMap<Long, List<Update>> getNewUpdates() {
        return newUpdates;
    }

    public void setNewUpdates(HashMap<Long, List<Update>> newUpdates) {
        this.newUpdates = newUpdates;
    }
}
