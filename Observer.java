public interface Observer {
    void update(String message);
    int getId();
    String getEmail();
}