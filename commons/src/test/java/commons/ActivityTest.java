package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    Activity activity;

    @BeforeEach
    void setUp() {
        activity = new Activity("Title", 100, "test/path");
    }

    @Test
    void testConstructor() {
        assertNotNull(activity);
    }

    @Test
    void getTitle() {
        assertEquals("Title", activity.getTitle());
    }

    @Test
    void getConsumption() {
        assertEquals(100, activity.getConsumption());
    }

    @Test
    void getPath() {
        assertEquals("test/path", activity.getPath());
    }

    @Test
    void setTitle() {
        activity.setTitle("Alt Title");
        assertEquals("Alt Title", activity.getTitle());
    }

    @Test
    void setConsumption() {
        activity.setConsumption(500);
        assertEquals(500, activity.getConsumption());
    }

    @Test
    void setPath() {
        activity.setPath("alt/test/path");
        assertEquals("alt/test/path", activity.getPath());
    }

    @Test
    void getId() {
        activity.setId(3);
        assertEquals(3, activity.getId());
    }

    @Test
    void toStringTest() {
        String answer = "Activity{" +
                "id=" + activity.getId() +
                ", title='" + activity.getTitle() + '\'' +
                ", consumption=" + activity.getConsumption() +
                ", path='" + activity.getPath() + '\'' +
                '}';
        assertEquals(answer, activity.toString());
    }
}