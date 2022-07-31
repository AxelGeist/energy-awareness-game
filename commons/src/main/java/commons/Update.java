package commons;

import java.util.Set;

public class Update {

    private String username;
    private String type;
    private Person player;
    private long gameID;
    private Question question;
    private int scoreToUpdate;
    private Set<Person> persons;
    private String emote;

    /**
     * Update the player
     *
     * @param type  the update type
     * @param player the player to update
     */
    public Update(String type, Person player) {
        this.type = type;
        this.player = player;
    }

    /**
     * Update the game id
     *
     * @param type  the update type
     * @param gameID the game ID
     */
    public Update(String type, long gameID) {
        this.type = type;
        this.gameID = gameID;
    }

    /**
     * Update the players their scores
     *
     * @param type    the update type
     * @param persons all persons which will be sent to the leaderboard
     */
    public Update(String type, Set<Person> persons) {
        this.type = type;
        this.persons = persons;
    }

    /**
     * Update the players their scores
     *
     * @param type  the update type
     * @param score the amount of points to increase
     */
    public Update(String type, int score) {
        this.type = type;
        this.scoreToUpdate = score;
    }

    /**
     * Update the clients when a emote needs to be shown
     *
     * @param type  the update type
     * @param emote the emote that needs to be shown
     * @param username the username of the person that sent the update
     */
    public Update(String type, String emote, String username) {
        this.type = type;
        this.emote = emote;
        this.username = username;
    }

    /**
     * Empty update
     *
     * @param type the update type
     */
    public Update(String type) {
        this.type = type;
    }

    /**
     * Update the question
     *
     * @param type  the update type
     * @param question the question to update
     */
    public Update(String type, Question question) {
        this.type = type;
        this.question = question;
    }


    public String getEmote() {
        return emote;
    }

    public String getUsername() {
        return username;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public long getGameID() {
        return gameID;
    }

    public String getType() {
        return type;
    }

    public Person getPlayer() {
        return player;
    }

    public int getScoreToUpdate() {
        return scoreToUpdate;
    }

    public void setScoreToUpdate(int scoreToUpdate) {
        this.scoreToUpdate = scoreToUpdate;
    }


    /**
     * To string for update
     * @return the string
     */
    @Override
    public String toString() {
        return "Update{" +
                "type='" + type + '\'' +
                ", player=" + player +
                '}';
    }
}
