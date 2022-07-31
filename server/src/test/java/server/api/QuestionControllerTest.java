package server.api;

import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionControllerTest {

    private QuestionController questionController;
    private TestQuestionRepository testQuestionRepository;
    private Question q;

    @BeforeEach
    public void setup() {
        q = new Question(2, "type", "test");

        testQuestionRepository = new TestQuestionRepository();
        testQuestionRepository.save(q);

        questionController = new QuestionController(testQuestionRepository);
    }

    @Test
    public void get() {
        assertEquals(ResponseEntity.ok(q), questionController.get());
        assertTrue(testQuestionRepository.calledMethods.contains("findAll"));
    }

    @Test
    public void add() {
        Question q2 = new Question(2, "test", "test");
        assertEquals(ResponseEntity.ok(q2), questionController.add(q2));
        assertTrue(testQuestionRepository.calledMethods.contains("findAll"));
        assertTrue(testQuestionRepository.calledMethods.contains("save"));
    }

    @Test
    public void addFalse() {
        assertEquals(ResponseEntity.badRequest().build(), questionController.add(q));
        assertTrue(testQuestionRepository.calledMethods.contains("findAll"));
    }

    @Test
    public void addNull() {
        assertEquals(ResponseEntity.badRequest().build(), questionController.add(null));
        assertFalse(testQuestionRepository.calledMethods.contains("findAll"));
    }

    @Test
    public void getById() {
        q.setId(4L);
        assertEquals(ResponseEntity.ok(q), questionController.getById(4L));
        assertTrue(testQuestionRepository.calledMethods.contains("existsById"));
        assertTrue(testQuestionRepository.calledMethods.contains("getById"));
    }

    @Test
    public void getByIdFalse() {
        assertEquals(ResponseEntity.badRequest().build(), questionController.getById(24L));
        assertTrue(testQuestionRepository.calledMethods.contains("existsById"));
        assertFalse(testQuestionRepository.calledMethods.contains("getById"));
    }



}
