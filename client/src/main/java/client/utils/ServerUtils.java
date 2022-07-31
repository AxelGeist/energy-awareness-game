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
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import commons.Activity;
import commons.Person;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private String server = "http://localhost:8080/";

    /**
     * Request to the server to show the emote
     *
     * @param emote    the name of the emote
     * @param gameID   the id of the game
     * @param username the name of the person sending the emote
     */
    public void showEmote(String emote, long gameID, String username) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/emote/" + gameID + "/" + username) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(emote, APPLICATION_JSON));
    }

    /**
     * Add an activity to the database
     *
     * @param title       activity title
     * @param consumption activity consumption
     */
    public void addActivity(String title, long consumption) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/activity/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(new Activity(title, consumption,
                        "../../Empty.png"), APPLICATION_JSON));
    }

    /**
     * Request to the server to add a person
     *
     * @param username the name of the user to add
     * @return the added person
     */
    public Person addPersonSP(String username) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/playersSP/addSP") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(username, APPLICATION_JSON), Person.class);
    }

    /**
     * Request to the server to modify the score of a person
     *
     * @param id    the id of the user to modify
     * @param score the score we need to add to the leaderboard
     */
    public void modify(long id, int score) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/playersSP/modify/" + score) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(id, APPLICATION_JSON));
    }

    /**
     * Request to the server to get the list of person
     *
     * @return the list with persons
     */
    public List<Person> getPerson() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/players") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Request all the players in the database leaderboard
     *
     * @return the list with the people in the database
     */
    public List<Person> getAllPlayersInDatabase() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/databaseSP/get") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Request image from activity
     *
     * @param path image path
     * @return image
     */
    public byte[] getImage(String path) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/images/" + path) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Request to the server to add a person
     *
     * @param username the name of the user to add
     * @return the added person
     */
    public Person addPerson(String username) {
        return ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/players/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(username, APPLICATION_JSON), Person.class);
    }

    /**
     * Request to add the existing person to the waitingroom
     *
     * @param userID the id of the person to add
     */
    public void waitingRoom(long userID) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/players/waitingRoom") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(userID, APPLICATION_JSON));
    }

    /**
     * Request to the server to start the game
     */
    public void startGameMultiplayer() {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/startGameMultiplayer") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get();
    }

    /**
     * Request to the server to start the game singleplayer
     *
     * @param person the person to start the game with
     */
    public void startGameSingleplayer(Person person) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/startGameSingleplayer") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(person, APPLICATION_JSON));
    }

    /**
     * Request to the server to remove a person
     *
     * @param userID the id of the person to remove
     */
    public void leavePerson(long userID) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/players/leave") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(userID, APPLICATION_JSON));
    }

    /**
     * Request to update a person, but also tells the server to keep track
     * of when they answered.
     *
     * @param answer the answer of the person
     * @param userID the id of the user
     */
    public void sendAnswer(long answer, long userID) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/players/answer/" + userID) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(answer, APPLICATION_JSON));
    }

    /**
     * Request to the server to get the leaderboard
     *
     * @param gameID the id of the game
     * @return the list of person
     */
    public List<Person> getLeaderboard(long gameID) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("api/getLeaderboard/" + gameID) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {
                });
    }

    /**
     * Request to update a person's joker usage.
     *
     * @param gameID the id of the current game
     * @param name   the name of the joker
     * @param userID the name of the user id
     */
    public void sendJoker(String name, long gameID, long userID) {
        ClientBuilder.newClient(new ClientConfig())
                .target(server).path("api/players/joker/" + gameID + "/" + userID) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(name, APPLICATION_JSON));
    }

    /**
     * Method to test the server connection
     */
    public void testConnection() {
        ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get();
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }
}