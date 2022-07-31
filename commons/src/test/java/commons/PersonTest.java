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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    Person a;
    Person b;
    Person c;
    Person p;

    @BeforeEach
    void setUp() {
        a = new Person("a");
        b = new Person("a");
        c = new Person(1, "f", 100, 3);
        p = new Person("test");
    }

    @Test
    public void checkConstructor() {
        Person p2 = new Person();
        assertNotNull(p2);
        assertEquals("a", a.getUsername());
    }

    @Test
    public void checkSecondConstructor() {
        assertEquals(1, c.getId());
        assertEquals("f", c.getUsername());
        assertEquals(100, c.getScore());
        assertEquals(3, c.getPlace());
    }

    @Test
    public void reset() {
        c.reset();
        assertEquals(0, c.getScore());
        assertEquals(0, c.getCurrentAnswer());
        assertNull(c.getTimeAnswered());
        assertEquals(0, c.getPlace());
        assertFalse(c.getDoublePoints());
    }

    @Test
    void updateScore() {
        p.updateScore(5);
        assertEquals(p.getScore(), 5);
    }

    @Test
    void getCurrentAnswer() {
        Person a = new Person("a");
        a.setCurrentAnswer(10);
        assertEquals(a.getCurrentAnswer() , 10);
    }

    @Test
    void updateCurrentAnswer() {
        Person a = new Person("a");
        a.setCurrentAnswer(10);
        assertEquals(a.getCurrentAnswer() , 10);
    }

    @Test
    void getScore() {
        assertEquals(p.getScore(), 0);
    }

    @Test
    void setScore() {
        p.setScore(500);
        assertEquals(p.getScore(), 500);
    }

    @Test
    void getUsername() {
        assertEquals(p.getUsername(), "test");
    }

    @Test
    void getPlace() {
        assertEquals(3, c.getPlace());
    }

    @Test
    void setPlace() {
        c.setPlace(50);
        assertEquals(50, c.getPlace());
    }

    @Test
    void testEquals() {
        Person a = new Person("a");
        Person b = new Person("a");
        assertEquals(a, b);
    }

    @Test
    public void equalsHashCode() {
        Person a = new Person("a");
        Person b = new Person("a");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testToString() {
        assertTrue(p.toString().contains("name=test"));
        assertTrue(p.toString().contains("id=" + p.getId()));
    }

    @Test
    void getId() {
        assertEquals(p.getId(), 0);
    }

    @Test
    void setTimeAnswered() {
        c.setTimeAnswered(Date.from(Instant.parse("1980-04-09T10:15:30Z")));
        assertEquals(Date.from(Instant.parse("1980-04-09T10:15:30Z")), c.getTimeAnswered());
    }

    @Test
    void setTimeAnsweredNull() {
        c.setTimeAnswered(null);
        assertNull(c.getTimeAnswered());
    }

    @Test
    void getTimeAnswered() {
        c.setTimeAnswered(Date.from(Instant.parse("1980-04-09T10:15:30Z")));
        assertEquals(Date.from(Instant.parse("1980-04-09T10:15:30Z")), c.getTimeAnswered());
    }

    @Test
    void getID() {
        p.setId(1);
        assertEquals(1, p.getId());
    }

    @Test
    void getTimeAnsweredNull() {
        c.setTimeAnswered(null);
        assertNull(c.getTimeAnswered());
    }

    @Test
    void getDoublePoints() {
        c.setDoublePoints(true);
        assertTrue(c.getDoublePoints());
    }

    @Test
    void setDoublePoints() {
        c.setDoublePoints(true);
        assertTrue(c.getDoublePoints());
    }

    @Test
    void isSingleplayer() {
        c.setSingleplayer(true);
        assertTrue(c.isSingleplayer());
    }

    @Test
    void setSingleplayer() {
        c.setSingleplayer(true);
        assertTrue(c.isSingleplayer());
    }
}