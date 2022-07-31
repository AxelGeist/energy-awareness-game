package server.services;

import commons.Person;
import commons.Question;
import commons.Update;
import org.springframework.beans.factory.annotation.Autowired;
import server.api.PlayerController;
import server.database.PlayerRepository;
import server.database.QuestionRepository;
import server.entity.Game;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerControllerService {

    private List<Person> waitingRoom;
    private final int questionTime = 10; // Change back to 10 when done debugging
    private List<Game> games;
    private final PlayerController playerController;
    private final PlayerRepository playerRepository;
    private final QuestionRepository questionRepository;

    /**
     * The constructor for the service
     *
     * @param playerController   the controller
     * @param playerRepository   the player repo
     * @param questionRepository the question repo
     */
    @Autowired
    public PlayerControllerService(PlayerController playerController,
                                   PlayerRepository playerRepository,
                                   QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.playerRepository = playerRepository;
        this.playerController = playerController;
        this.waitingRoom = new ArrayList<>();
        this.games = new ArrayList<>();
    }

    /**
     * Get the leaderboard
     *
     * @param id the id of the game
     * @return list of players
     * @throws Exception when game doesn't exist
     */
    public List<Person> getLeaderboard(long id) throws Exception {
        for (Game g : games) {
            if (g.getId() == id) return new ArrayList<>(g.getPlayers());
        }
        throw new Exception();
    }

    /**
     * Delete user from waiting room and the game
     *
     * @param userID the id of the user
     */
    public void deleteFromGamesAndWaitingRoom(long userID) {
        for (Game currGame : games) {
            for (Person currPerson : currGame.getPlayers()) {
                if (currPerson.getId() == userID) {
                    currGame.removePlayer(currPerson);
                    if (currGame.getPlayers().size() == 0) currGame.stop();
                    break;
                }
            }
        }

        for (Person p : waitingRoom) {
            if (p.getId() == userID) {
                waitingRoom.remove(p);
                Update update = new Update("leavePlayer", p);

                for (Person p2 : waitingRoom) {
                    playerController.processNewUpdate(update, p2.getId());
                }
                break;
            }
        }

        Update update = new Update("leave");
        playerController.processNewUpdate(update, userID);
    }

    /**
     * Give the client permission to show the answers on the screen
     *
     * @param game the game where the clients are assigned to
     */
    public void showSingleplayerLeaderboardEndGame(Game game) {
        Update update = new Update("showSingleplayerLeaderboardEndGame", game.getId());
        for (Person p : game.getPlayers()) {
            playerController.processNewUpdate(update, p.getId());
        }
    }

    /**
     * Give the client permission to show the question on the screen
     *
     * @param game the game where the clients are assigned to
     */
    public void showQuestion(Game game) {
        game.setStartOfCurrentQuestion(new Date());

        Question q = game.getQuestions().get(0);
        game.getQuestions().remove(q);
        game.setCurrentQuestion(q);

        Update update = new Update("showQuestion", q);
        for (Person p : game.getPlayers()) {
            playerController.processNewUpdate(update, p.getId());
        }
    }


    /**
     * Give the client permission to show the answers on the screen
     *
     * @param game the game where the clients are assigned to
     */
    public void showAnswer(Game game) {
        List<Person> listPlayers = new ArrayList<>(game.getPlayers());

        Question question = game.getCurrentQuestion();

        if (question.getType().equals("OpenQuestion")
                || question.getType().equals("InsteadQuestion")) {
            question.updatePersonOpen(question.whoAnsweredCorrectlyOpen(listPlayers));
        } else {
            question.updateScore(question.whoAnsweredCorrectly(listPlayers),
                    game.getStartOfCurrentQuestion(), questionTime);
        }

        for (Person p : listPlayers) {
            p.setCurrentAnswer(Long.MIN_VALUE);

            Update update = new Update("showAnswer", p.getScore());
            playerController.processNewUpdate(update, p.getId());

            playerRepository.save(p);
        }
    }


    /**
     * Method to start the multiplayer game
     */
    public void startGameMultiplayer() {
        List<Question> questions = new ArrayList<>();
        List<Question> list = questionRepository.findAll();
        for (int i = 0; i < 20; i++) {

            int random = (int) (Math.random() * list.size());
            Question q = list.get(random);
            questions.add(q);
        }
        Game g = new Game(waitingRoom, games.size() + 1L, questionTime,
                questions, this, new Timer());
        games.add(g);

        Update newUpdate = new Update("startGame", g.getId());
        for (Person p : g.getPlayers()) {
            playerController.processNewUpdate(newUpdate, p.getId());
        }

        g.start();
        waitingRoom.clear();
    }

    /**
     * Method to start the single player game
     *
     * @param player the player of the game
     */
    public void startGameSingleplayer(Person player) {
        List<Question> questions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            List<Question> list = questionRepository.findAll();
            int random = (int) (Math.random() * list.size());
            Question q = list.get(random);
            questions.add(q);
        }
        Game g = new Game(List.of(player), games.size() + 1L, questionTime,
                questions, this, new Timer());


        g.setSingleplayer(true);
        games.add(g);

        Update newUpdate = new Update("StartSP", g.getId());

        playerController.processNewUpdate(newUpdate, player.getId());

        g.start();
    }

    /**
     * Method to show an emote
     *
     * @param gameID   the game id
     * @param username the username
     * @param emote    the name of the emote
     */
    public void showEmote(long gameID, String username, String emote) {
        Update update = new Update("showEmote", emote, username);
        for (Game g : games) {
            if (g.getId() == gameID) {
                for (Person p : g.getPlayers()) {
                    playerController.processNewUpdate(update, p.getId());
                }
            }
        }
    }

    /**
     * Method to handle the joker logic
     *
     * @param userId the id of the person who used the joker
     * @param name   the name of the joker
     * @param gameID the game id
     */
    public void handleJoker(long userId, String name, long gameID) {
        if (name.equals("double-point")) {
            for (Game g : games) {
                if (g.getId() == gameID) {
                    for (Person player : g.getPlayers()) {
                        if (player.getId() == userId) {
                            player.setDoublePoints(true);
                            playerRepository.save(player);
                        }
                    }
                }
            }
        } else if (name.equals("timer")) {
            for (Game g : games) {
                if (g.getId() == gameID) {
                    for (Person player : g.getPlayers()) {
                        if (player.getId() != userId) {
                            Update update = new Update("decreasetimeUpdate",
                                    g.getStartOfCurrentQuestion().getTime());
                            playerController.processNewUpdate(update, player.getId());
                        }
                    }
                }
            }
        }

        Optional<Person> p = playerRepository.findById(userId);
        if (p.isPresent()) {
            String username = p.get().getUsername();
            playerController.showEmote(gameID, username, name);
        }

    }

    /**
     * Show the loadingScreen to the client and send the question
     *
     * @param game the game where the clients are assigned to
     */
    public void showLoadingScreen(Game game) {
        Update update = new Update("loadingScreen");
        for (Person p : game.getPlayers()) {
            playerController.processNewUpdate(update, p.getId());
        }
    }

    /**
     * Give the client permission to show the answers on the screen
     *
     * @param game the game where the clients are assigned to
     */
    public void showLeaderboardMidGame(Game game) {
        Set<Person> players = game.getPlayers();
        Update update = new Update("showLeaderboardMidGame", players);
        for (Person p : players) {
            playerController.processNewUpdate(update, p.getId());
        }
    }

    /**
     * Give the client permission to show the answers on the screen
     *
     * @param game the game where the clients are assigned to
     */
    public void showLeaderboardEndGame(Game game) {
        Set<Person> players = game.getPlayers();
        Update update = new Update("showLeaderboardEndGame", players);
        for (Person p : players) {
            playerController.processNewUpdate(update, p.getId());
        }
    }

    /**
     * Method to add a player
     *
     * @param username the username of the player
     * @return the added player
     */
    public Person addPlayer(String username) {
        List<Game> list = getGame()
                .stream().filter(x -> !x.getIsSingleplayer())
                .collect(Collectors.toList());
        for (Game g : list) {
            if (g.getPlayers().stream().anyMatch(x ->
                    Objects.equals(x.getUsername(), username))) {
                return null;
            }
        }
        if (getWaitingRoom()
                .stream().anyMatch(x -> x.getUsername()
                        .equals(username)))
            return null;
        Person newPerson = new Person(username);
        Person saved = playerRepository.save(newPerson);

        waitingRoom.add(saved);

        Update update = new Update("addPlayer", saved);

        for (Person p : waitingRoom) {
            playerController.processNewUpdate(update, p.getId());
        }

        return saved;
    }

    /**
     * Method to join the waiting room and update everyone that someone joined
     *
     * @param person the person who wants to join
     * @return the person that joined
     */
    public Person joinWaitingRoom(Person person) {
        person.reset();
        waitingRoom.add(person);

        Update update = new Update("addPlayer", person);
        for (Person p : waitingRoom) {
            playerController.processNewUpdate(update, p.getId());
        }
        return person;
    }

    /**
     * Receive answer
     *
     * @param answer the answer
     * @param userId the id of the user
     */
    public void receiveAnswer(long userId, long answer) {
        for (Game g : games) {
            for (Person p : g.getPlayers()) {
                if (userId == p.getId()) {
                    p.setCurrentAnswer(answer);
                    p.setTimeAnswered(new Date());

                    playerRepository.save(p);
                }
            }
        }
    }

    /**
     * Method to add to the waiting room
     *
     * @param p the person to add
     */
    public void addWaitingRoom(Person p) {
        this.waitingRoom.add(p);
    }

    /**
     * Add a game to the games
     *
     * @param game the game to add
     */
    public void addGame(Game game) {
        this.games.add(game);
    }

    public List<Person> getWaitingRoom() {
        return this.waitingRoom;
    }

    public void setWaitingRoom(List<Person> waitingRoom) {
        this.waitingRoom = waitingRoom;
    }

    public List<Game> getGame() {
        return this.games;
    }

    public void setGame(List<Game> games) {
        this.games = games;
    }


}
