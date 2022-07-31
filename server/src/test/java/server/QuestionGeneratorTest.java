package server;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.TestActivityRepository;
import server.api.TestQuestionRepository;
import server.database.ActivityRepository;
import server.database.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QuestionGeneratorTest {

    private List<Activity> activityList;
    private TestQuestionRepository questionRepo;
    private TestActivityRepository activityRepo;
    private QuestionGenerator generator;
    private MyQuestionGenerator myQuestionGenerator;

    @BeforeEach
    void setUp() {
        activityList = new ArrayList<>();
        activityList.add(new Activity("a", 500, "image"));
        activityList.add(new Activity("b", 500, "image"));
        activityList.add(new Activity("c", 500, "image"));
        activityList.add(new Activity("d", 500, "image"));

        questionRepo = new TestQuestionRepository();
        activityRepo = new TestActivityRepository();

        generator = new QuestionGenerator(activityRepo, questionRepo);
        myQuestionGenerator = new MyQuestionGenerator(activityRepo, questionRepo);
    }


    @Test
    public void generator() {
        generator.generator();
        assertTrue(activityRepo.calledMethods.contains("findAll"));
    }

    @Test
    public void generatorQuestionsTrue() {
        activityList.add(new Activity("e", 500, "image"));
        myQuestionGenerator.generatorQuestions(activityList);
        assertTrue(myQuestionGenerator.gottenAll);
    }

    @Test
    public void generatorQuestionsFalse() {
        myQuestionGenerator.generatorQuestions(activityList);
        assertFalse(myQuestionGenerator.gottenAll);
    }

    @Test
    void generateOpenQuestion() {
        generator.generateOpenQuestion(activityList);
        assertTrue(questionRepo.calledMethods.contains("save"));
    }

    @Test
    void generateMCQuestion() {
        generator.generateMCQuestion(activityList);
        assertTrue(questionRepo.calledMethods.contains("save"));
    }

    @Test
    void generateComparisonQuestion() {
        generator.generateComparisonQuestion(activityList);
        assertTrue(questionRepo.calledMethods.contains("save"));
    }

    @Test
    void generateGuessQuestion() {
        generator.generateGuessQuestion(activityList);
        assertTrue(questionRepo.calledMethods.contains("save"));
    }

    @Test
    void generateInsteadQuestion() {
        generator.generateInsteadQuestion(activityList);
        assertTrue(questionRepo.calledMethods.contains("save"));
    }

    @Test
    void generateClosestQuestion() {
        generator.generateClosestQuestion(activityList);
        assertTrue(questionRepo.calledMethods.contains("save"));
    }

    @Test
    public void generateRandomAnswers() {
        assertNotNull(generator.generateRandomAnswers(9L));
    }


    @Test
    void answersToString() {
        assertEquals("1," , generator.answersToString(Set.of(1L)));
    }

    public static class MyQuestionGenerator extends QuestionGenerator {

        public boolean openWasCalled = false;
        public boolean mcWasCalled = false;
        public boolean insteadWasCalled = false;
        public boolean comparisonWasCalled = false;
        public boolean guessWasCalled = false;
        public boolean closestWasCalled = false;

        public boolean gottenAll = false;

        /**
         * Constructor is needed to initialize repositories
         *
         * @param activityRepository activities repo
         * @param questionRepository questions repo
         */
        public MyQuestionGenerator(ActivityRepository activityRepository,
                                   QuestionRepository questionRepository) {
            super(activityRepository, questionRepository);
        }

        @Override
        public void generateOpenQuestion(List<Activity> activities) {
            if (gottenAllQuestion()) activities.clear();
            openWasCalled = true;
        }

        @Override
        public void generateMCQuestion(List<Activity> activities) {
            if (gottenAllQuestion()) activities.clear();
            mcWasCalled = true;
        }

        @Override
        public void generateComparisonQuestion(List<Activity> activities) {
            if (gottenAllQuestion()) activities.clear();
            comparisonWasCalled = true;
        }

        @Override
        public void generateGuessQuestion(List<Activity> activities) {
            if (gottenAllQuestion()) activities.clear();
            guessWasCalled = true;
        }

        @Override
        public void generateInsteadQuestion(List<Activity> activities) {
            if (gottenAllQuestion()) activities.clear();
            insteadWasCalled = true;
        }

        @Override
        public void generateClosestQuestion(List<Activity> activities) {
            if (gottenAllQuestion()) activities.clear();
            closestWasCalled = true;
        }

        public boolean gottenAllQuestion() {
            if (openWasCalled && mcWasCalled && comparisonWasCalled &&
                    guessWasCalled && insteadWasCalled && closestWasCalled) {
                gottenAll = true;
                return true;
            }
            return false;
        }

    }


}