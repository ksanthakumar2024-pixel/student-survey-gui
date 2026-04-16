import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentSystemDB {
    private static EnrollmentSystemDB instance;

    private EnrollmentSystemDB() {
    }

    public static synchronized EnrollmentSystemDB getInstance() {
        if (instance == null) {
            instance = new EnrollmentSystemDB();
        }
        return instance;
    }

    public int addStudent(String name, String email) throws SQLException {
        String sql = "INSERT INTO students (name, email) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Student getStudentById(int id) throws SQLException {
        String sql = "SELECT id, name, email FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                }
            }
        }
        return null;
    }

    public int addCourse(String courseName) throws SQLException {
        String sql = "INSERT INTO courses (name) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, courseName);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Course getCourseById(int courseId) throws SQLException {
        String sql = "SELECT id, name FROM courses WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            rs.getInt("id"),
                            rs.getString("name")
                    );
                }
            }
        }
        return null;
    }

    public void enrollStudentInCourse(int studentId, int courseId) throws SQLException {
        String sql = "INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        }
    }

    public void removeEnrollment(int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.executeUpdate();
        }
    }

    public void removeStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.executeUpdate();
        }
    }

    public List<Student> getStudentsByCourse(int courseId) throws SQLException {
        String sql = "SELECT s.id, s.name, s.email FROM students s " +
                     "JOIN enrollment e ON s.id = e.student_id WHERE e.course_id = ?";
        List<Student> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    ));
                }
            }
        }
        return list;
    }

    public void notifyCourseStudents(int courseId, String message) throws SQLException {
        Course course = getCourseById(courseId);
        if (course == null) {
            return;
        }

        List<Student> students = getStudentsByCourse(courseId);
        for (Student s : students) {
            course.registerObserver(s);
        }
        course.notifyObservers(message);
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT id, name FROM courses";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Course(rs.getInt("id"), rs.getString("name")));
            }
        }
        return list;
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, name, email FROM students";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        }
        return list;
    }
}