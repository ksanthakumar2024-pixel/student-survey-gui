import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

public class MainGUIDB {
    private JFrame frame;
    private EnrollmentSystemDB system = EnrollmentSystemDB.getInstance();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUIDB().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Lab5 - Patterns (DB)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);
        frame.setLayout(new BorderLayout());

        JTextArea output = new JTextArea();
        output.setEditable(false);
        JScrollPane scroll = new JScrollPane(output);

        JPanel controls = new JPanel(new GridLayout(0, 1, 4, 4));
        JButton btnAddStudent = new JButton("Add Student");
        JButton btnRemoveStudent = new JButton("Remove Student");
        JButton btnAddCourse = new JButton("Add Course");
        JButton btnEnroll = new JButton("Enroll Student");
        JButton btnNotify = new JButton("Notify Course Students");
        JButton btnShow = new JButton("Display Courses & Students");

        controls.add(btnAddStudent);
        controls.add(btnRemoveStudent);
        controls.add(btnAddCourse);
        controls.add(btnEnroll);
        controls.add(btnNotify);
        controls.add(btnShow);

        frame.add(controls, BorderLayout.WEST);
        frame.add(scroll, BorderLayout.CENTER);

        btnAddStudent.addActionListener((ActionEvent e) -> {
            String name = JOptionPane.showInputDialog(frame, "Student name:");
            String email = JOptionPane.showInputDialog(frame, "Student email:");
            if (name != null && email != null) {
                try {
                    int id = system.addStudent(name, email);
                    output.append("Added student id=" + id + " name=" + name + "\n");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "DB error: " + ex.getMessage());
                }
            }
        });

        btnRemoveStudent.addActionListener(e -> {
            try {
                List<Student> students = system.getAllStudents();
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No students to remove.");
                    return;
                }

                Student s = (Student) JOptionPane.showInputDialog(
                        frame,
                        "Select a student to remove completely:",
                        "Remove Student",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        students.toArray(),
                        students.get(0)
                );

                if (s != null) {
                    int confirm = JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you want to delete student " + s.getName() + "?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        system.removeStudent(s.getId());
                        JOptionPane.showMessageDialog(frame, "Student " + s.getName() + " removed successfully!");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        btnAddCourse.addActionListener(e -> {
            String cname = JOptionPane.showInputDialog(frame, "Course name:");
            if (cname != null) {
                try {
                    int cid = system.addCourse(cname);
                    output.append("Added course id=" + cid + " name=" + cname + "\n");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "DB error: " + ex.getMessage());
                }
            }
        });

        btnEnroll.addActionListener(e -> {
            try {
                List<Student> students = system.getAllStudents();
                List<Course> courses = system.getAllCourses();

                if (students.isEmpty() || courses.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Need at least one student and one course in DB.");
                    return;
                }

                Student s = (Student) JOptionPane.showInputDialog(
                        frame,
                        "Select student",
                        "Enroll",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        students.toArray(),
                        students.get(0)
                );

                Course c = (Course) JOptionPane.showInputDialog(
                        frame,
                        "Select course",
                        "Enroll",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        courses.toArray(),
                        courses.get(0)
                );

                if (s != null && c != null) {
                    system.enrollStudentInCourse(s.getId(), c.getCourseId());
                    output.append("Enrolled " + s.getName() + " to " + c.getCourseName() + "\n");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "DB error: " + ex.getMessage());
            }
        });

        btnNotify.addActionListener(e -> {
            try {
                List<Course> courses = system.getAllCourses();
                if (courses.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No courses available.");
                    return;
                }

                Course c = (Course) JOptionPane.showInputDialog(
                        frame,
                        "Select course to notify",
                        "Notify",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        courses.toArray(),
                        courses.get(0)
                );

                if (c != null) {
                    String msg = JOptionPane.showInputDialog(frame, "Message to notify:");
                    if (msg != null) {
                        system.notifyCourseStudents(c.getCourseId(), msg);
                        output.append("Notified students of course " + c.getCourseName() + "\n");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "DB error: " + ex.getMessage());
            }
        });

        btnShow.addActionListener(e -> {
            try {
                output.append("=== COURSES ===\n");
                for (Course c : system.getAllCourses()) {
                    output.append(c + "\n");
                }

                output.append("=== STUDENTS ===\n");
                for (Student s : system.getAllStudents()) {
                    output.append(s + "\n");
                }
                output.append("\n");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "DB error: " + ex.getMessage());
            }
        });

        JButton btnFactory = new JButton("Factory: Create Product");
        btnFactory.addActionListener(e -> {
            String[] options = {"A", "B"};
            String type = (String) JOptionPane.showInputDialog(
                    frame,
                    "Product type (A or B):",
                    "Factory",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (type != null) {
                String name = JOptionPane.showInputDialog(frame, "Product name:");
                if (name == null) {
                    return;
                }

                if ("A".equals(type)) {
                    String qStr = JOptionPane.showInputDialog(frame, "Quantity (integer):");
                    try {
                        Product p = ProductFactory.createProduct("A", name, Integer.parseInt(qStr));
                        output.append("Created: " + p.getDetails() + "\n");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid data: " + ex.getMessage());
                    }
                } else {
                    String color = JOptionPane.showInputDialog(frame, "Color:");
                    Product p = ProductFactory.createProduct("B", name, color);
                    output.append("Created: " + p.getDetails() + "\n");
                }
            }
        });

        JPanel bottom = new JPanel();
        bottom.add(btnFactory);
        frame.add(bottom, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}