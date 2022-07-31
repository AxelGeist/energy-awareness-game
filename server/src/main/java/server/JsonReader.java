package server;

import commons.Activity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import server.database.ActivityRepository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

@Controller
public class JsonReader {

    private final ActivityRepository repository;

    /**
     * Constructor is needed to initialize repository
     *
     * @param repository the repository to save the activities
     */
    public JsonReader(ActivityRepository repository) {
        this.repository = repository;
    }

    /**
     * Read and store all json objects from test.json to the database
     */
    public void read() {
        JSONParser jsonParser = new JSONParser();

        File dir = new File("../server/src/main/resources/ActivityBank/");
        dir = new File(dir, Objects.requireNonNull(dir.list())[0]);

        try (FileReader reader = new FileReader(dir + "/activities.json")) {
            JSONArray jasonObject = (JSONArray) jsonParser.parse(reader);

            for (Object o : jasonObject) {
                JSONObject obj = (JSONObject) o;

                Activity activity = new Activity(obj.get("title").toString(),
                        Long.parseLong(obj.get("consumption_in_wh").toString()),
                        obj.get("image_path").toString());

                repository.save(activity);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}