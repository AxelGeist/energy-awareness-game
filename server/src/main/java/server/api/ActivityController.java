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
package server.api;

import commons.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api")
public class ActivityController {

    private final ActivityRepository repository;

    /**
     * Constructor for the question controller
     *
     * @param repository the activity repository
     */
    @Autowired
    public ActivityController(ActivityRepository repository) {
        this.repository = repository;
    }

    /**
     * Method to get a random activity
     *
     * @return a response containing the activity
     */
    @GetMapping("/activity/get")
    public ResponseEntity<Activity> get() {
        List<Activity> activities = repository.findAll();
        int x = (int) (Math.random() * activities.size());
        return ResponseEntity.ok(activities.get(x));
    }

    /**
     * Method to post an activity
     *
     * @param activity the activity to add
     * @return a response containing the activity
     */
    @PostMapping("/activity/add")
    public ResponseEntity<Activity> add(@RequestBody Activity activity) {
        if (activity.getPath() == null || activity.getTitle() == null)
            return ResponseEntity.badRequest().build();


        List<Activity> activities = repository.findAll();
        for (Activity q : activities) {
            if (q == activity)
                return ResponseEntity.badRequest().build();
        }

        repository.save(activity);

        return ResponseEntity.ok(activity);
    }

    /**
     * Method to get an Image
     *
     * @param folder folder location
     * @param file file name
     * @return a response containing the activity
     */
    @GetMapping("/images/{folder}/{file}")
    public ResponseEntity<byte[]> getImage(@PathVariable("folder") String folder,
                                           @PathVariable("file") String file) {
        try {
            File dir = new File("src/main/resources/ActivityBank/");
            dir = new File(dir, Objects.requireNonNull(dir.list())[0]);

            String path = dir + "/" + folder + "/" + file;
            return ResponseEntity.ok(Files.readAllBytes((new File(path)).toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
