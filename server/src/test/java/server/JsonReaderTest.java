package server;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestActivityRepository;



import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonReaderTest {

    TestActivityRepository activityRepository;
    JsonReader jsonReader;

    @BeforeEach
    void setUp() {
        activityRepository = new TestActivityRepository();
        jsonReader = new JsonReader(activityRepository);
    }

    @Test
    public void constructorTest() {
        assertNotNull(jsonReader);
    }

    @Test
    public void read() {
        jsonReader.read();
        assertTrue(activityRepository.calledMethods.contains("save"));
    }
}
