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
package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import server.database.ActivityRepository;
import server.database.QuestionRepository;

@SpringBootApplication
@EntityScan(basePackages = {"commons", "server"})
public class Main {

    private static ActivityRepository activityRepository;
    private static QuestionRepository questionRepository;

    /**
     * The main class
     *
     * @param activityRepository activity repository
     * @param questionRepository question repository
     */
    @Autowired
    public Main(ActivityRepository activityRepository,
                QuestionRepository questionRepository) {
        Main.activityRepository = activityRepository;
        Main.questionRepository = questionRepository;
    }

    /**
     * Method to run the server,
     * plus it reads the json file and generates question
     *
     * @param args the arguments to start
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        JsonReader reader = new JsonReader(activityRepository);
        reader.read();
        QuestionGenerator generator = new QuestionGenerator(activityRepository,
                questionRepository);
        generator.generator();
    }
}