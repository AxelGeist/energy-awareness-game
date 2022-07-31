package commons;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private long consumption;
    private String path;

    @SuppressWarnings("unused")
    private Activity() {
        // for object mapper
    }

    /**
     * This is the activity class we have a title for the title provided in the JSON
     * file, then we have the consumption provided by the JSON file and last we have
     * the path to the image associated to the JSON file
     * @param title
     * @param consumption
     * @param path
     */
    public Activity(String title, long consumption, String path) {
        this.title = title;
        this.consumption = consumption;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getConsumption() {
        return consumption;
    }

    public void setConsumption(int consumption) {
        this.consumption = consumption;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", consumption=" + consumption +
                ", path='" + path + '\'' +
                '}';
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
