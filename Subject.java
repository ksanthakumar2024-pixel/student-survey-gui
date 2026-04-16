public interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String message);
    String getCourseName();
    int getCourseId();
}
