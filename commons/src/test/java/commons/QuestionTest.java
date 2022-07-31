package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    Question question;
    List<Activity> activities;

    @BeforeEach
    void setUp() {
        activities = new ArrayList<>();
        activities.add(new Activity("Having a shower", 1000, "image"));
        activities.add(new Activity("Dishwasher running for one hour", 1200, "image"));
        activities.add(new Activity("Having the computer turned on for one hour", 1100, "image"));
        activities.add(new Activity("Bake a cake", 800, "image"));
        question = new Question(100, "test", "test");
        question.setUsedActivities(activities);
    }

    @Test
    void OpenQuestionFormula() {
        question.setAnswer(84);
        Person person = new Person("Tijmen");
        person.setCurrentAnswer(62);
        List<Person> list = new ArrayList<>();
        list.add(person);
        assertEquals(0.7, question.whoAnsweredCorrectlyOpen(list).get(person));
    }

    @Test
    void testNullConstructor() {
        Question question = new Question();
        assertNotNull(question);
    }

    @Test
    void testConstructor() {
        assertNotNull(question);
    }

    @Test
    void Constructor() {
        question = new Question(100, "test", "test");
        assertEquals(new Question(100, "test", "test"), question);
    }

    @Test
    void equalsMethod() {
        Question question1 = new Question(100, "t", "test");
        Question question2 = new Question(100, "t", "test");
        assertEquals(question1, question2);
    }

    @Test
    void setCorrectAnswer() {
        question.setCorrectAnswer();
        assertEquals(1200, question.getAnswer());
    }

    @Test
    void setCorrectAnswerClosest() {
        activities.remove(2);
        question.setCorrectAnswerClosest();
        assertEquals(1000, question.getAnswer());
    }

    @Test
    void setCorrectAnswerClosest2() {
        activities.remove(3);
        question.setCorrectAnswerClosest();
        assertEquals(1200, question.getAnswer());
    }

    @Test
    void getUsedActivities() {
        assertEquals(activities, question.getUsedActivities());
    }

    @Test
    void setUsedActivities() {
        question.setUsedActivities(activities);
        assertEquals(activities, question.getUsedActivities());
    }

    @Test
    void getAnswer() {
        question.setAnswer(1000);
        assertEquals(1000, question.getAnswer());
    }

    @Test
    void setAnswer() {
        question.setAnswer(1000);
        assertEquals(1000, question.getAnswer());
    }

    @Test
    void equalsSameObjectMethod() {
        assertEquals(question, question);
    }

    @Test
    void notEqualsAnswer() {
        Question question1 = new Question(50, "test", "test");
        Question question2 = new Question(100, "test", "test");
        assertNotEquals(question1, question2);
    }

    @Test
    void notEqualsUsedActivities() {
        Question question1 = new Question(100, "test", "test");
        Question question2 = new Question(100, "test", "test");
        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity("Test Activity", 100, "test/path"));
        question2.setUsedActivities(activities);
        assertNotEquals(question1, question2);
    }

    @Test
    void notSameMethod() {
        assertNotEquals(null, question);
    }

    @Test
    void equalsNotSameClass() {
        Question question1 = new Question(50, "test", "test");
        Object o = new Object();
        assertNotEquals(question1, o);
    }

    @Test
    void whoAnsweredCorrectly() {
        List<Person> players = new ArrayList<>();
        Person player1 = new Person(1, "Vlad", 100, 1);
        Person player2 = new Person(2, "Tijmen", 100, 2);
        player1.setCurrentAnswer(1200);
        player2.setCurrentAnswer(1200);
        players.add(player1);
        players.add(player2);

        Person player3 = new Person(3, "Wout", 100, 3);
        players.add(player3);
        List<Person> correct = List.of(player1, player2);

        question.setCorrectAnswer();
        assertEquals(correct, question.whoAnsweredCorrectly(players));
    }

    @Test
    void testEmptyAnswer() {
        assertThrows(IllegalArgumentException.class,
                () -> question.whoAnsweredCorrectly(null));
    }

    @Test
    void testEmptyOpenAnswer() {
        assertThrows(IllegalArgumentException.class,
                () -> question.whoAnsweredCorrectlyOpen(null));
    }

    @Test
    void whoAnsweredCorrectlyOpen() {
        List<Person> players = new ArrayList<>();
        Person player1 = new Person(1, "Vlad", 100, 1);
        Person player2 = new Person(2, "Tijmen", 100, 2);
        Person player3 = new Person(2, "Thijs", 100, 2);
        player1.setCurrentAnswer(1000);
        player2.setCurrentAnswer(-10000);
        question.setCorrectAnswer();
        player3.setCurrentAnswer(question.getAnswer());
        players.add(player1);
        players.add(player2);
        players.add(player3);

        HashMap<Person, Double> map = new HashMap<>();
        map.put(player1, 0.8);
        map.put(player2, 0.0);
        map.put(player3, 1.0);
        assertEquals(map, question.whoAnsweredCorrectlyOpen(players));

    }

    @Test
    void whoAnsweredCorrectlyOpen2() {
        List<Person> players = new ArrayList<>();
        Person player1 = new Person(1, "Vlad", 100, 1);
        Person player2 = new Person(2, "Tijmen", 100, 2);
        Person player3 = new Person(2, "Thijs", 100, 2);
        player1.setCurrentAnswer(1000);
        question.setCorrectAnswer();
        player2.setCurrentAnswer(question.getAnswer() + 400);
        player3.setCurrentAnswer(question.getAnswer());
        players.add(player1);
        players.add(player2);
        players.add(player3);

        HashMap<Person, Double> map = new HashMap<>();
        map.put(player1, 0.8);
        map.put(player2, 0.7);
        map.put(player3, 1.0);
        assertEquals(map, question.whoAnsweredCorrectlyOpen(players));

    }

    @Test
    void updatePersonOpen() {
        Person player1 = new Person(1, "Vlad", 100, 1);
        player1.setDoublePoints(true);

        HashMap<Person, Double> map = new HashMap<>();
        player1.setScore(0);
        map.put(player1, 1.0);
        question.updatePersonOpen(map);

        assertEquals(2000, player1.getScore());
    }

    @Test
    void getID() {
        question.setId(1);
        assertEquals(1, question.getId());
    }

    @Test
    void getAnswers() {
        Question q = new Question(1, "test", "1");
        assertEquals("1", q.getAnswers());
    }

    @Test
    void achievedScore() {
        Person p = new Person("a");
        p.setTimeAnswered(Date.from(Instant.parse("1980-04-09T10:15:31Z")));
        assertEquals(1000 - 700 / 5, question.achievedScore(p,
                Date.from(Instant.parse("1980-04-09T10:15:30Z")), 5));
    }

    @Test
    void achieved2Score() {
        Person p = new Person("a");
        Date a = new Date();
        a.setTime(100000);

        Date b = new Date();
        b.setTime(101100);

        p.setTimeAnswered(b);

        assertEquals(846, question.achievedScore(p, a, 5));

    }

    @Test
    void achievedDoubleScore() {
        Person p = new Person("a");
        p.setDoublePoints(true);
        p.setTimeAnswered(Date.from(Instant.parse("1980-04-09T10:15:31Z")));
        assertEquals((1000 - 700 / 5) * 2, question.achievedScore(p,
                Date.from(Instant.parse("1980-04-09T10:15:30Z")), 5));
    }

    @Test
    void updateScore() {
        Person p = new Person("a");
        p.setTimeAnswered(Date.from(Instant.parse("1980-04-09T10:15:30Z")));
        List<Person> winners = new ArrayList<>();
        winners.add(p);
        question.updateScore(winners, Date.from(Instant.parse("1980-04-09T10:15:30Z")), 5);
        assertEquals(1000, p.getScore());
    }

    @Test
    void testToString() {
        String answer = "Question{" +
                "usedActivities=" + question.getUsedActivities() +
                ", answer=" + question.getAnswer() +
                '}';
        assertEquals(answer, question.toString());
    }

    @Test
    void getTypeTest() {
        assertEquals("test", question.getType());
    }
}