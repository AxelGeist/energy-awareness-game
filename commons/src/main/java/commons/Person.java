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
package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private int score;
    private long currentAnswer;
    private Date timeAnswered;
    private int place;
    private boolean doublePoints = false;
    private boolean isSingleplayer = false;

    /**
     * Empty constructor
     */
    @SuppressWarnings("unused")
    public Person() {
        // for object mapper
    }

    /**
     * Constructor for person, it sets the score and place to 0;
     * @param username the username of the person
     */
    public Person(String username) {
        this.username = username;
        this.score = 0;
        this.place = 0;
    }

    /**
     * A constructor for testing purposes
     * @param id the id of the person
     * @param username the username of the person
     * @param score the score of the person
     * @param place the place of the person
     */
    public Person(long id, String username, int score, int place) {
        this.id = id;
        this.username = username;
        this.score = score;
        this.place = place;
    }


    /**
     * Resets the player so all attributes except username and id are set ti default
     */
    public void reset() {
        score = 0;
        currentAnswer = 0;
        timeAnswered = null;
        place = 0;
        doublePoints = false;
    }

    /**
     * Update the score with an int, it adds it to the total
     * @param score score to add to the total
     */
    public void updateScore( int score ) {
        this.score += score;
    }

    public long getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswer(long newAnswer) {
        this.currentAnswer = newAnswer;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) { this.score = score; }

    public String getUsername() {
        return username;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimeAnswered(Date timeAnswered) {
        this.timeAnswered = timeAnswered;
    }

    public Date getTimeAnswered() {
        return timeAnswered;
    }

    public boolean getDoublePoints() {
        return doublePoints;
    }

    public void setDoublePoints(boolean doublePoints) {
        this.doublePoints = doublePoints;
    }

    public boolean isSingleplayer() {
        return isSingleplayer;
    }

    public void setSingleplayer(boolean singleplayer) {
        isSingleplayer = singleplayer;
    }

}