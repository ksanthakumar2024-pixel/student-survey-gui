import java.util.ArrayList;
import java.util.List;

public class Course implements Subject {
    private int courseId;
    private String courseName;
    private List<Observer> observers = new ArrayList<>();

    public Course(int courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    @Override
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update("Course [" + courseName + "]: " + message);
        }
    }

    @Override
    public String getCourseName() {
        return courseName;
    }

    @Override
    public int getCourseId() {
        return courseId;
    }

    @Override
    public String toString() {
        return String.format("Course[id=%d, name=%s, enrolled=%d]", courseId, courseName, observers.size());
    }
}
