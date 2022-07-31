package server.api;

import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.QuestionRepository;

import java.util.List;


@RestController
@RequestMapping("/api")
public class QuestionController {

    private final QuestionRepository repository;

    /**
     * Constructor for the question controller
     * @param questionRepository the questions repository
     */
    @Autowired
    public QuestionController(QuestionRepository questionRepository) {
        this.repository = questionRepository;
    }

    /**
     * Method to get a random question
     *
     * @return a response containing the activity
     */
    @GetMapping("/question/get")
    public ResponseEntity<Question> get() {
        List<Question> questions = repository.findAll();
        int x = (int) (Math.random() * questions.size());
        return ResponseEntity.ok(questions.get(x));
    }

    /**
     * Method to post a question, it first look if the question
     * is null, after this we look if there is already a question
     * like that in the repo if it is not then we save the
     * question to the repo.
     *
     * @param question the question to add
     * @return a response containing the question
     */
    @PostMapping("/question/add")
    public ResponseEntity<Question> add(@RequestBody Question question) {
        if (question == null)
            return ResponseEntity.badRequest().build();

        List<Question> questions = repository.findAll();
        for (Question q : questions) {
            if (q == question)
                return ResponseEntity.badRequest().build();
        }

        repository.save(question);

        return ResponseEntity.ok(question);
    }

    /**
     * get a question by ID
     *
     * @param id the corresponding ID
     * @return The question with the ID
     */
    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getById(@PathVariable("id") long id) {
        if (id < 0 || !repository.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(repository.getById(id));
    }

}
