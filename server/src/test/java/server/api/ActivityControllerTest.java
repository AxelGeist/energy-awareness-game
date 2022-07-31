package server.api;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ActivityControllerTest {

    private ActivityController activityController;
    private TestActivityRepository testActivityRepository;

    private Activity a;

    @BeforeEach
    public void setup() {
        a = new Activity("title", 1, "test");

        testActivityRepository = new TestActivityRepository();
        testActivityRepository.save(a);

        activityController = new ActivityController(testActivityRepository);
    }

    @Test
    public void get() {
        assertEquals(ResponseEntity.ok(a), activityController.get());
        assertTrue(testActivityRepository.calledMethods.contains("findAll"));
    }

    @Test
    public void add() {
        Activity a2 = new Activity("test2", 3210, "test");
        assertEquals(ResponseEntity.ok(a2), activityController.add(a2));
        assertTrue(testActivityRepository.calledMethods.contains("findAll"));
        assertTrue(testActivityRepository.calledMethods.contains("save"));
    }

    @Test
    public void addFalse() {
        assertEquals(ResponseEntity.badRequest().build(), activityController.add(a));
        assertTrue(testActivityRepository.calledMethods.contains("findAll"));
    }

    @Test
    public void addNull() {
        Activity a2 = new Activity("test2", 3210, null);
        assertEquals(ResponseEntity.badRequest().build(), activityController.add(a2));
        assertFalse(testActivityRepository.calledMethods.contains("findAll"));
    }

    @Test
    public void getImageWrong() {
        assertEquals(ResponseEntity.badRequest().build(),
                activityController.getImage("fake", "fake"));
    }

    @Test
    public void getImage() throws IOException {
        File dir = new File("src/main/resources/ActivityBank/");
        dir = new File(dir, Objects.requireNonNull(dir.list())[0]);
        String path = dir + "/" + "00" + "/" + "fridge.png";
        byte[] result = Files.readAllBytes((new File(path)).toPath());

        assertEquals(ResponseEntity.ok(result),
                activityController.getImage("00", "fridge.png"));
    }




}
