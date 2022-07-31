package server.entity;

import commons.Person;
import commons.Question;
import server.services.PlayerControllerService;

import java.util.*;

public class Game {

    private final int totalQuestions = 20;        // Change back to  20 when done debugging
    private final int loadingTime = 3;            // Change back to  3 when done debugging
    private final int answerTime = 5;             // Change back to  5 when done debugging
    private final int leaderboardTime = 10;        // Change back to  10 when done debugging

    private long id;
    private int counter;
    private final int questionTime;
    private Set<Person> players;
    private Timer gameTimer;
    private Date startOfCurrentQuestion;
    private Question currentQuestion;
    private boolean isSingleplayer;
    private List<Question> questions;
    private final PlayerControllerService playerControllerService;

    /**
     * Constructor to create a game
     *
     * @param players                 the players to add to the game
     * @param id                      the id of the game
     * @param questionTime            the maximum time players are allowed to answer
     * @param questions               the questions for the game
     * @param playerControllerService the controller to use
     * @param gameTimer               the timer that is used during the game,
     *                                given in this way to make it testable
     */
    public Game(List<Person> players, long id, int questionTime, List<Question> questions,
                PlayerControllerService playerControllerService, Timer gameTimer) {
        this.gameTimer = gameTimer;
        this.players = new HashSet<>(players);
        this.id = id;
        this.questionTime = questionTime;
        this.playerControllerService = playerControllerService;
        this.isSingleplayer = false;
        this.questions = questions;
    }

    /**
     * Constructor to create a game for tests, so it doesn't have the lists
     *
     * @param id                      the id of the game
     * @param questionTime            the maximum time players are allowed to answer
     * @param playerControllerService the controller to use
     */
    public Game(long id, int questionTime, PlayerControllerService playerControllerService) {
        this.gameTimer = new Timer();
        this.players = new HashSet<>();
        this.id = id;
        this.questionTime = questionTime;
        this.playerControllerService = playerControllerService;
        this.isSingleplayer = false;
        this.questions = new ArrayList<>();
    }

    /**
     * toString method for game
     *
     * @return a string with the game information
     */
    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", players=" + players +
                ", amountOfQuestions=" + totalQuestions +
                '}';
    }

    public Set<Person> getPlayers() {
        return players;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean getIsSingleplayer() {
        return isSingleplayer;
    }

    public void setSingleplayer(boolean singleplayer) {
        isSingleplayer = singleplayer;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    /**
     * Function to add a player
     *
     * @param player the player to add to the game
     */
    public void addPlayer(Person player) {
        this.players.add(player);
    }

    /**
     * Function to remove a player
     *
     * @param player the player to remove from the game
     */
    public void removePlayer(Person player) {
        this.players.remove(player);
    }

    /**
     * Method to start the game
     */
    public void start() {
        this.counter = 0;

        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showQuestion();
            }
        }, loadingTime * 1000L);
    }

    /**
     * Method to show the question
     */
    public void showQuestion() {
        counter++;
        playerControllerService.showQuestion(this);
        gameTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                showAnswer();
            }
        }, questionTime * 1000L);

    }

    /**
     * Method to show the answer
     */
    public void showAnswer() {
        playerControllerService.showAnswer(this);
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showLoadingScreen();
            }
        }, answerTime * 1000L);
    }

    /**
     * Method to show the loading screen,
     * if the counter is at the half of the questions, it shows the mid-game leaderboard
     * if the counter is at the end of the questions, it shows the end-game leaderboard
     * otherwise the next question is shown after the loading screen
     */
    public void showLoadingScreen() {
        playerControllerService.showLoadingScreen(this);
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showCorrectScreenAfterLoading();
            }
        }, loadingTime * 1000L);
    }

    /**
     * Helper method for show loading screen to make it testable
     */
    public void showCorrectScreenAfterLoading() {
        if (counter == (totalQuestions / 2) && !isSingleplayer) showMidGameLeaderboard();
        else if (counter >= totalQuestions) {
            if (isSingleplayer) {
                showSingleplayerLeaderboard();
            } else {
                showEndGameLeaderboard();
            }
        } else showQuestion();
    }

    /**
     * Method to show the end-game Singleplayer leaderboard
     */
    public void showSingleplayerLeaderboard() {
        playerControllerService.showSingleplayerLeaderboardEndGame(this);
    }

    /**
     * Method to show the mid-game leaderboard
     */
    public void showMidGameLeaderboard() {
        playerControllerService.showLeaderboardMidGame(this);
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showLoadingAfterLeaderboard();
            }
        }, leaderboardTime * 1000L);
    }

    /**
     * Show loading after leaderboard
     */
    public void showLoadingAfterLeaderboard() {
        playerControllerService.showLoadingScreen(this);
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showQuestion();
            }
        }, loadingTime * 1000L);
    }

    /**
     * Adds a question to the game
     *
     * @param question The question to add
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    /**
     * Method to show the end-game leaderboard
     */
    public void showEndGameLeaderboard() {
        playerControllerService.showLeaderboardEndGame(this);
    }


    public Date getStartOfCurrentQuestion() {
        return startOfCurrentQuestion;
    }


    public void setStartOfCurrentQuestion(Date startOfCurrentQuestion) {
        this.startOfCurrentQuestion = startOfCurrentQuestion;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    /**
     * This method will stop the game
     * if no players are left in the game
     */
    public void stop() {
        this.gameTimer.cancel();
    }

}
