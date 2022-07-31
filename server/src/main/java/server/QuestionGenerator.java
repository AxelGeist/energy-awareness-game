package server;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import server.database.ActivityRepository;
import server.database.QuestionRepository;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class QuestionGenerator {


    private final ActivityRepository activityRepository;
    private final QuestionRepository questionRepository;

    /**
     * Constructor is needed to initialize repositories
     *
     * @param activityRepository activities repo
     * @param questionRepository questions repo
     */
    @Autowired
    public QuestionGenerator(ActivityRepository activityRepository,
                             QuestionRepository questionRepository) {
        this.activityRepository = activityRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Gets all the questions from the repository and
     * calls the function to generate questions from the imported activities
     */
    public void generator() {
        generatorQuestions(this.activityRepository.findAll());
    }

    /**
     * This is the question generator, and we firstly choose randomly from 3 numbers
     * to know which type of question we will generate and after that we pick random
     * activities from the given activities to add them to our question, after this we
     * add the question to the repo.
     *
     * @param activities the activities to generate questions with
     */
    public void generatorQuestions(List<Activity> activities) {
        if (activities.size() > 4) {
            int x = ThreadLocalRandom.current().nextInt(0, 6);
            switch (x) {
                case 0:
                    generateOpenQuestion(activities);
                    break;
                case 1:
                    generateMCQuestion(activities);
                    break;
                case 2:
                    generateComparisonQuestion(activities);
                    break;
                case 3:
                    generateGuessQuestion(activities);
                    break;
                case 4:
                    generateInsteadQuestion(activities);
                    break;
                case 5:
                    generateClosestQuestion(activities);
                    break;
                default:
                    break;
            }
            this.generatorQuestions(activities);
        }
    }

    /**
     * Generate open question
     *
     * @param activities all activities to use
     */
    public void generateOpenQuestion(List<Activity> activities) {
        Question openQuestion = new Question(0, "OpenQuestion", "NoAnswers");
        Activity activity = activities.get(
                ThreadLocalRandom.current().nextInt(0, activities.size()));
        List<Activity> activitiesO = new ArrayList<>();
        activitiesO.add(activity);
        openQuestion.setUsedActivities(activitiesO);
        openQuestion.setCorrectAnswer();
        activities.remove(activity);
        this.questionRepository.save(openQuestion);
    }

    /**
     * Generate Multiple choice question
     *
     * @param activities all activities to use
     */
    public void generateMCQuestion(List<Activity> activities) {
        Question mcQuestion = new Question(0, "MCQuestion", "NoAnswers");
        List<Activity> activitiesMC = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Activity random = activities.get(
                    ThreadLocalRandom.current().nextInt(0, activities.size()));
            activitiesMC.add(random);
            activities.remove(random);
        }
        mcQuestion.setUsedActivities(activitiesMC);
        mcQuestion.setCorrectAnswer();
        questionRepository.save(mcQuestion);
    }

    /**
     * Generate Multiple choice question
     *
     * @param activities all activities to use
     */
    public void generateComparisonQuestion(List<Activity> activities) {
        Question comparisonQuestion =
                new Question(0, "ComparisonQuestion", "NoAnswers");
        List<Activity> activitiesC = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Activity random = activities.get(
                    ThreadLocalRandom.current().nextInt(0, activities.size()));
            activitiesC.add(random);
            activities.remove(random);
        }
        comparisonQuestion.setUsedActivities(activitiesC);
        comparisonQuestion.setCorrectAnswer();
        questionRepository.save(comparisonQuestion);
    }

    /**
     * Generate closest to question
     *
     * @param activities all activities to use
     */
    public void generateClosestQuestion(List<Activity> activities) {
        Question closestQuestion =
                new Question(0, "ClosestQuestion", "NoAnswers");
        List<Activity> activitiesC = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Activity random = activities.get(
                    ThreadLocalRandom.current().nextInt(0, activities.size()));
            activitiesC.add(random);
            activities.remove(random);
        }
        closestQuestion.setUsedActivities(activitiesC);
        closestQuestion.setCorrectAnswerClosest();
        questionRepository.save(closestQuestion);
    }

    /**
     * Generate Guess question
     *
     * @param activities all activities to use
     */
    public void generateGuessQuestion(List<Activity> activities) {
        Activity activity1 = activities.get(
                ThreadLocalRandom.current().nextInt(0, activities.size()));

        Set<Long> answers = generateRandomAnswers(activity1.getConsumption());

        Question guessQuestion = new Question(activity1.getConsumption(),
                "GuessQuestion", answersToString(answers));
        guessQuestion.setUsedActivities(List.of(activity1));
        activities.remove(activity1);
        questionRepository.save(guessQuestion);
    }

    /**
     * Generate Instead question
     *
     * @param activities all activities to use
     */
    public void generateInsteadQuestion(List<Activity> activities) {
        List<Activity> GeneratedActivities = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Activity random = activities.get(
                    ThreadLocalRandom.current().nextInt(0, activities.size()));
            GeneratedActivities.add(random);
            activities.remove(random);
        }

        long biggest = Math.max(GeneratedActivities.get(0).getConsumption(),
                GeneratedActivities.get(1).getConsumption());

        long smallest = Math.min(GeneratedActivities.get(0).getConsumption(),
                GeneratedActivities.get(1).getConsumption());

        if (smallest <= 0) smallest = 1;

        Question insteadQuestion =
                new Question(biggest / smallest, "InsteadQuestion", "NoAnswers");

        // set the biggest activity first
        if (GeneratedActivities.get(0).getConsumption() == smallest) {
            Activity temp = GeneratedActivities.get(0);
            GeneratedActivities.set(0, GeneratedActivities.get(1));
            GeneratedActivities.set(1, temp);
        }

        insteadQuestion.setUsedActivities(GeneratedActivities);
        questionRepository.save(insteadQuestion);
    }

    /**
     * Generate random answers for the guess question
     *
     * @param correctAnswer The correct answer to the question
     * @return A set with all the generated answers
     */
    public Set<Long> generateRandomAnswers(long correctAnswer) {

        Set<Long> answers = new HashSet<>();
        answers.add(correctAnswer);

        int zeros = 0;
        long checkZeros = correctAnswer;
        while (checkZeros % 10 == 0) {
            zeros++;
            checkZeros /= 10;
        }
        while (answers.size() < 4) {
            if (correctAnswer <= 10) {
                long answer = (long) (10 * Math.random());
                if (!answers.contains(answer)) {
                    answers.add(answer);
                }
                continue;
            }
            Random random = new Random();

            long n = (long) ((random.nextDouble() * 4 - 3) * correctAnswer);
            long answer = (Math.round(Math.pow(10, -1 * zeros) *
                    (correctAnswer - n))) * (long) Math.pow(10, zeros);
            if (!answers.contains(answer) && answer != 0) {
                answers.add(answer);
            }
        }
        return answers;
    }

    /**
     * Method to change a set of long into a string
     *
     * @param answers the set of longs
     * @return a string presenting the set
     */
    public String answersToString(Set<Long> answers) {
        String result = "";

        for (Long a : answers) {
            result += a + ",";
        }

        return result;
    }
}
