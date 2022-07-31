package client.scenes;

import com.google.inject.Inject;
import commons.Person;
import commons.Question;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.util.*;


public class GameManager {

    protected ObservableList<Person> data;

    protected static Date lastEmote;

    @FXML
    protected TableView<Person> table;

    @FXML
    protected Label questionLap;

    @FXML
    protected Label questionLapShadow;

    @FXML
    protected Label openQuestionSolution;

    @FXML
    protected Text checkMark1;

    @FXML
    protected Text checkMark2;

    @FXML
    protected Text checkMark3;

    @FXML
    protected Text checkMark4;

    @FXML
    protected Text crossMark1;

    @FXML
    protected Text crossMark2;

    @FXML
    protected Text crossMark3;

    @FXML
    protected Text crossMark4;

    @FXML
    protected Label scoreText;

    @FXML
    protected Label scoreShadow;

    @FXML
    protected ProgressBar myProgressBar;

    @FXML
    protected Button cry;

    @FXML
    protected Button muscle;

    @FXML
    protected Button poop;

    @FXML
    protected Button wink;

    @FXML
    protected Group emote;

    @FXML
    protected Button doublePointsButton;

    @FXML
    protected ImageView doublePointsImage;

    @FXML
    protected Button timerButton;

    @FXML
    protected ImageView timerImage;

    @FXML
    protected Button removeAnswerButton;

    @FXML
    protected ImageView removeAnswerImage;

    @FXML
    protected TextField emoteSender;

    @FXML
    protected ImageView emoteImage;

    @FXML
    private Pane RectanglesMP;

    @FXML
    private Pane RectanglesSP;

    @FXML
    private Pane RectanglesTopMP;

    @FXML
    private Pane RectanglesTopSP;

    @FXML
    private Pane InteractMP;

    @FXML
    protected Button option1;

    @FXML
    protected Button option2;

    @FXML
    protected Button option3;

    @FXML
    protected Button option4;

    @FXML
    private Label activity1_front;

    @FXML
    private Label activity2_front;

    @FXML
    private Label activity3_back;

    @FXML
    private Label activity3_front;

    @FXML
    private ImageView rightImage;

    @FXML
    private ImageView leftImage;

    @FXML
    protected TextField answer;


    protected Timer timer;
    protected MainCtrl mainCtrl;

    /**
     * Constructor of game-scene controller
     *
     * @param mainCtrl main control
     */
    @Inject
    public GameManager(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * This method is called on the start of every game-scene
     */
    public void start() {
        refreshScoreAndTimer(10);

        startEmotesSystem();

        displayJokers();

        turnOffChecksAndCrosses();

        disableButtons(false);

        displaySingleplayerScreen(mainCtrl.isSingleplayer());

        updateQuestionLapOnScene();
    }

    /**
     * Start the emote system
     */
    public void startEmotesSystem() {
        emote.setVisible(false);
        cry.setOnAction(e -> mainCtrl.getServer().showEmote("cry", mainCtrl.getGameID(),
                mainCtrl.getUsername()));
        muscle.setOnAction(e -> mainCtrl.getServer().showEmote("muscle", mainCtrl.getGameID(),
                mainCtrl.getUsername()));
        poop.setOnAction(e -> mainCtrl.getServer().showEmote("poop", mainCtrl.getGameID(),
                mainCtrl.getUsername()));
        wink.setOnAction(e -> mainCtrl.getServer().showEmote("wink", mainCtrl.getGameID(),
                mainCtrl.getUsername()));
    }

    /**
     * Sets all the cross marks and check marks to invisible
     */
    public void turnOffChecksAndCrosses() {
        if (crossMark1 != null) crossMark1.setVisible(false);
        if (crossMark2 != null) crossMark2.setVisible(false);
        if (checkMark1 != null) checkMark1.setVisible(false);
        if (checkMark2 != null) checkMark2.setVisible(false);
        if (crossMark3 != null) crossMark3.setVisible(false);
        if (crossMark3 != null) crossMark4.setVisible(false);
        if (checkMark3 != null) checkMark3.setVisible(false);
        if (checkMark4 != null) checkMark4.setVisible(false);
    }

    /**
     * Display jokers the correct way
     */
    public void displayJokers() {
        if (doublePointsButton != null) {
            if (mainCtrl.getUsedDoublePoints()) {
                doublePointsButton.setDisable(true);
                doublePointsImage.setOpacity(0.3);
            } else {
                doublePointsButton.setDisable(false);
                doublePointsImage.setOpacity(1);
            }
        }
        if (timerButton != null) {
            if (mainCtrl.getUsedTimer()) {
                timerButton.setDisable(true);
                timerImage.setOpacity(0.3);
            } else {
                timerButton.setDisable(false);
                timerImage.setOpacity(1);
            }
        }

        if (removeAnswerButton != null) {
            if (mainCtrl.getUsedRemoveAnswer() ||
                    !(this instanceof GuessQuestionCtrl || this instanceof MCQuestionCtrl)) {
                removeAnswerButton.setDisable(true);
                removeAnswerImage.setOpacity(0.3);
            } else {
                removeAnswerButton.setDisable(false);
                removeAnswerImage.setOpacity(1);
            }
        }
    }


    /**
     * Show answer for multiple choice type
     *
     * @param i the number of the answer
     */
    public void answer(int i) {
        disableButtons(true);
        if (i == 1) option1.setTextFill(Color.YELLOW);
        if (i == 2) option2.setTextFill(Color.YELLOW);
        if (i == 3) option3.setTextFill(Color.YELLOW);
        if (i == 4) option4.setTextFill(Color.YELLOW);
        mainCtrl.getServer().sendAnswer(mainCtrl.getQuestion()
                .getUsedActivities().get(i - 1).getConsumption(), mainCtrl.getUserID());
    }

    /**
     * Disables the answer buttons
     *
     * @param trigger sets the buttons to be visible or invisible
     */
    public void disableButtons(boolean trigger) {
        if (answer != null) answer.setDisable(trigger);
        if (option1 != null) option1.setDisable(trigger);
        if (option2 != null) option2.setDisable(trigger);
        if (option3 != null) option3.setDisable(trigger);
        if (option4 != null) option4.setDisable(trigger);
    }

    /**
     * Show emote on screen
     *
     * @param emoji the emote to show
     * @param name  the name of the user
     */
    public void showEmote(String emoji, String name) {

        lastEmote = new Date();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Date now = new Date();
                if (now.getTime() - lastEmote.getTime() >= 3000) emote.setVisible(false);
            }
        }, 3000L);

        emote.setVisible(true);
        if (emoji.equals("cry") || emoji.equals("muscle") ||
                emoji.equals("poop") || emoji.equals("wink")) {
            emoteImage.setImage(new Image("client/images/emotes/" + emoji + ".png"));
        } else emoteImage.setImage(new Image("client/images/jokers/" + emoji + ".png"));
        emoteSender.setText(name);
        emoteSender.disabledProperty();
        emoteSender.requestLayout();
        emoteSender.setStyle("-fx-text-fill: white; -fx-font-size: 25px; " +
                "-fx-background-color: transparent");
    }

    /**
     * Show the leaderboard
     */
    public void showLeaderboard() {
        List<Person> person = mainCtrl.getServer().getLeaderboard(mainCtrl.getGameID());
        person.sort(Comparator.comparing(Person::getScore).reversed());
        List<Person> goodList = new ArrayList<>();
        for (int i = 0; i < person.size(); i++) {
            Person p = person.get(i);
            p.setPlace(i + 1);
            goodList.add(p);
        }
        data = FXCollections.observableList(goodList);
        table.setItems(data);
    }

    /**
     * Refresh the scene
     *
     * @param seconds the length of the timer
     */
    public void refreshScoreAndTimer(double seconds) {
        int score = mainCtrl.getScore();
        if (scoreShadow != null && scoreText != null) {
            scoreShadow.setText("Score:  " + score);
            scoreText.setText("Score:  " + score);
        }

        if (myProgressBar != null) {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO,
                            new KeyValue(this.myProgressBar.progressProperty(), 0)),
                    new KeyFrame(Duration.seconds(seconds), e -> {
                    }, new KeyValue(this.myProgressBar.progressProperty(), 1))
            );
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    /**
     * Activate double points joker
     */
    public void doublePoints() {
        mainCtrl.setUsedDoublePoints(true);
        doublePointsButton.setDisable(true);
        doublePointsImage.setOpacity(0.3);
        mainCtrl.getServer().sendJoker("double-point", mainCtrl.getGameID(), mainCtrl.getUserID());
    }

    /**
     * Activate decrease time joker
     */
    public void decreaseTime() {
        mainCtrl.setUsedTimer(true);
        timerButton.setDisable(true);
        timerImage.setOpacity(0.3);
        mainCtrl.getServer().sendJoker("timer", mainCtrl.getGameID(), mainCtrl.getUserID());
    }

    /**
     * Activate remove answer joker
     */
    public void removeAnswer() {
        mainCtrl.setUsedRemoveAnswer(true);
        removeAnswerButton.setDisable(true);
        removeAnswerImage.setOpacity(0.3);
        mainCtrl.getServer().sendJoker("eraser", mainCtrl.getGameID(), mainCtrl.getUserID());

        Question q = mainCtrl.getQuestion();
        int amountOfAnswers = q.getUsedActivities().size();
        int k = 0;
        for (int i = 0; i < amountOfAnswers; i++) {
            if (q.getUsedActivities().get(i).getConsumption() == q.getAnswer()) {
                k = i;
            }
        }
        int randomNum = k;
        while (randomNum == k) {
            Random r = new Random();
            randomNum = r.nextInt((3 - 0) + 1) + 0;
        }

        if (randomNum == 0 && option1 != null) {
            option1.setDisable(true);
        } else if (randomNum == 1 && option2 != null) {
            option2.setDisable(true);
        } else if (randomNum == 2 && option3 != null) {
            option3.setDisable(true);
        } else if (randomNum == 3 && option4 != null) {
            option4.setDisable(true);
        }
    }

    /**
     * This method hides the objects used in multiplayer if it is a singleplayer game
     * and vice versa
     *
     * @param trigger it allows us to know if it a singleplayer or multiplayer game
     */
    public void displaySingleplayerScreen(Boolean trigger) {

        if (doublePointsButton != null) {
            RectanglesTopMP.setVisible(!trigger);
            RectanglesMP.setVisible(!trigger);
            InteractMP.setVisible(!trigger);
            RectanglesSP.setVisible(trigger);
            RectanglesTopSP.setVisible(trigger);
            if (trigger) {
                doublePointsButton.setDisable(true);
                timerButton.setDisable(true);
                removeAnswerButton.setDisable(true);
            }
            doublePointsImage.setVisible(!trigger);
            timerImage.setVisible(!trigger);
            removeAnswerImage.setVisible(!trigger);
        }
    }

    /**
     * Method to call when the joker is used
     *
     * @param startOfCurrentQuestion the start of the question
     */
    public void disableJoker(long startOfCurrentQuestion) {
        this.timerButton.setDisable(true);
        this.timerImage.setOpacity(0.3);

        Date currentTime = new Date();

        long spentTime = (currentTime.getTime() - startOfCurrentQuestion) / 1000;
        if (spentTime > 5) {
            disableButtons(true);
        } else {
            long newTime = 10 - spentTime - 5;
            Timer gameTimer = new Timer();
            System.out.println(newTime);
            gameTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    disableButtons(true);
                }
            }, newTime * 1000L);
        }
    }

    /**
     * Display the question + image to the screen
     */
    public void displayImagesQuestion() {
        rightImage.setOpacity(1);
        leftImage.setOpacity(1);

        Question q = mainCtrl.getQuestion();
        activity1_front.setText(q.getUsedActivities().get(0).getTitle());
        activity2_front.setText(q.getUsedActivities().get(1).getTitle());
        if (q.getType().equals("ClosestQuestion")) {
            activity3_front.setText(q.getUsedActivities().get(2).getTitle());
            activity3_back.setText(q.getUsedActivities().get(2).getTitle());
        }

        leftImage.setImage(new Image(new ByteArrayInputStream(
                this.mainCtrl.getServer().getImage(q.getUsedActivities().get(0).getPath()))));

        rightImage.setImage(new Image(new ByteArrayInputStream(
                this.mainCtrl.getServer().getImage(q.getUsedActivities().get(1).getPath()))));
    }


    /**
     * Function called when entering input on input field.
     * It will check if the input is an integer before it submits.
     */
    public void answer() {
        long s;
        try {
            s = Long.parseLong(answer.getText());
        } catch (IllegalArgumentException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText("Your input type is not correct.");
            alert.setContentText("Please use a number as an input.");
            alert.setTitle("Invalid input");
            alert.showAndWait();
            return;
        }
        disableButtons(true);
        mainCtrl.getServer().sendAnswer(s, mainCtrl.getUserID());
    }

    /**
     * show the answer on the screen
     */
    public void showAnswer() {
        disableButtons(true);
        refreshScoreAndTimer(5.0);
        disableAllJokers();
        if (openQuestionSolution != null) {
            if (leftImage == null) {
                openQuestionSolution.setText(mainCtrl.getQuestion().getUsedActivities()
                        .get(0).getConsumption() + " watt-hour");
            } else {
                openQuestionSolution.setText(mainCtrl.getQuestion().getAnswer() + "x");
            }

        } else {
            showMCAnswer();
        }

        if (leftImage != null && rightImage != null && answer == null) {
            if (leftImage.getOpacity() == 1 && rightImage.getOpacity() == 1) {
                leftImage.setOpacity(0.3);
                rightImage.setOpacity(0.3);
            }
        }
    }


    /**
     * Show the Answers to the scene
     */
    public void showMCAnswer() {

        Question q = mainCtrl.getQuestion();
        int amountOfAnswers = q.getUsedActivities().size();

        int k = 0;
        for (int i = 0; i < amountOfAnswers; i++)
            if (q.getUsedActivities().get(i).getConsumption() == q.getAnswer()) {
                k = i;
            }

        if (k == 0)
            checkMark1.setVisible(true);
        if (k == 1)
            checkMark2.setVisible(true);
        if (k != 0)
            crossMark1.setVisible(true);
        if (k != 1)
            crossMark2.setVisible(true);

        if (amountOfAnswers == 4) {
            if (k != 2)
                crossMark3.setVisible(true);
            if (k != 3)
                crossMark4.setVisible(true);
            if (k == 2)
                checkMark3.setVisible(true);
            if (k == 3)
                checkMark4.setVisible(true);
        }
        refreshScoreAndTimer(5.0);
        disableAllJokers();
    }

    /**
     * Disable the jokers
     */
    public void disableAllJokers() {
        doublePointsButton.setDisable(true);
        timerButton.setDisable(true);
        removeAnswerButton.setDisable(true);
    }


    /**
     * updates question number on the current question scene
     */
    public void updateQuestionLapOnScene() {
        if (questionLap != null && questionLapShadow != null) {
            questionLap.setText(mainCtrl.getQuestionLapCounter() + "/20");
            questionLapShadow.setText(mainCtrl.getQuestionLapCounter() + "/20");
        }
    }

    /**
     * This method returns us the main screen.
     */
    public void cancel() {
        mainCtrl.cancelAndRemovePlayer();
    }
}
