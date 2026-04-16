public class Student implements Observer {
    private int id;
    private String name;
    private String email;

    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Student(String name, String email) {
        this(-1, name, email);
    }

    @Override
    public void update(String message) {
        System.out.printf("Notification to %s (%s): %s%n", name, email, message);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Student[id=%d, name=%s, email=%s]", id, name, email);
    }
}
