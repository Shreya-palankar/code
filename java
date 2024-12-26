public class Student {
    private int id;
    private String name;
    private int age;

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

[26-12-2024 11:15 PM] Shrijitha Dsu: import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private List<Student> students = new ArrayList<>();

    public List<Student> getAllStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void deleteStudent(int id) {
        students.removeIf(student -> student.getId() == id);
    }

    public Student getStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }
}
[26-12-2024 11:15 PM] Shrijitha Dsu: import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    private StudentService studentService = new StudentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                request.setAttribute("students", studentService.getAllStudents());
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
            case "add":
                request.getRequestDispatcher("add-student.jsp").forward(request, response);
                break;
            case "delete":
                int id = Integer.parseInt(request.getParameter("id"));
                studentService.deleteStudent(id);
                response.sendRedirect("students");
                break;
            default:
                response.getWriter().println("Invalid action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        int id = studentService.getAllStudents().size() + 1;

        Student student = new Student(id, name, age);
        studentService.addStudent(student);

        response.sendRedirect("students");
    }
}
[26-12-2024 11:15 PM] Shrijitha Dsu: <!DOCTYPE html>
<html>
<head>
    <title>Student Management</title>
</head>
<body>
    <h1>Student List</h1>
    <a href="students?action=add">Add New Student</a>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Age</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student.id}</td>
                <td>${student.name}</td>
                <td>${student.age}</td>
                <td>
                    <a href="students?action=delete&id=${student.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
[26-12-2024 11:15 PM] Shrijitha Dsu: <!DOCTYPE html>
<html>
<head>
    <title>Add Student</title>
</head>
<body>
    <h1>Add Student</h1>
    <form action="students" method="post">
        <label for="name">Name:</label>
        <input type="text" name="name" required><br>
        <label for="age">Age:</label>
        <input type="number" name="age" required><br>
        <button type="submit">Add</button>
    </form>
</body>
</html>
[26-12-2024 11:15 PM] Shrijitha Dsu: import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private List<Student> students = new ArrayList<>();

    public List<Student> getAllStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void deleteStudent(int id) {
        students.removeIf(student -> student.getId() == id);
    }

    public Student getStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    public void updateStudent(int id, String name, int age) {
        Student student = getStudentById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
        }
    }
}
[26-12-2024 11:15 PM] Shrijitha Dsu: import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    private StudentService studentService = new StudentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                response.getWriter().println("Student List:");
                for (Student student : studentService.getAllStudents()) {
                    response.getWriter().println(student);
                }
                break;
            case "add":
                response.getWriter().println("To add a student, send a POST request.");
                break;
            case "delete":
                int id = Integer.parseInt(request.getParameter("id"));
                studentService.deleteStudent(id);
                response.getWriter().println("Student deleted.");
                break;
            case "update":
                response.getWriter().println("To update a student, send a POST request with 'update' action.");
                break;
            default:
                response.getWriter().println("Invalid action.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String name = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            int id = studentService.getAllStudents().size() + 1;

            Student student = new Student(id, name, age);
            studentService.addStudent(student);

            response.getWriter().println("Student added: " + student);
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));

            studentService.updateStudent(id, name, age);
            response.getWriter().println("Student updated: " + studentService.getStudentById(id));
        } else {
            response.getWriter().println("Invalid POST action.");
        }
    }
}
[26-12-2024 11:15 PM] Shrijitha Dsu: public class Main {
    public static void main(String[] args) {
        StudentService service = new StudentService();

        // Add Students
        service.addStudent(new Student(1, "Alice", 20));
        service.addStudent(new Student(2, "Bob", 22));
        service.addStudent(new Student(3, "Charlie", 21));

        // List Students
        System.out.println("Students List:");
        for (Student student : service.getAllStudents()) {
            System.out.println(student);
        }

        // Update Student
        service.updateStudent(2, "Bob Updated", 23);
        System.out.println("\nAfter Update:");
        for (Student student : service.getAllStudents()) {
            System.out.println(student);
        }

        // Delete Student
        service.deleteStudent(1);
        System.out.println("\nAfter Deletion:");
        for (Student student : service.getAllStudents()) {
            System.out.println(student);
        }
    }
}

[26-12-2024 11:15 PM] Shrijitha Dsu: public class Student {
    private int id;
    private String name;
    private int age;

    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
[26-12-2024 11:15 PM] Shrijitha Dsu: import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private List<Student> students = new ArrayList<>();

    public List<Student> getAllStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void deleteStudent(int id) {
        students.removeIf(student -> student.getId() == id);
    }

    public Student getStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }
}
[26-12-2024 11:15 PM] Shrijitha Dsu: import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    private StudentService studentService = new StudentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                request.setAttribute("students", studentService.getAllStudents());
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
            case "add":
                request.getRequestDispatcher("add-student.jsp").forward(request, response);
                break;
            case "delete":
                int id = Integer.parseInt(request.getParameter("id"));
                studentService.deleteStudent(id);
                response.sendRedirect("students");
                break;
            default:
                response.getWriter().println("Invalid action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        int id = studentService.getAllStudents().size() + 1;

        Student student = new Student(id, name, age);
        studentService.addStudent(student);

        response.sendRedirect("students");
    }
}
[26-12-2024 11:15 PM] Shrijitha Dsu: <!DOCTYPE html>
<html>
<head>
    <title>Student Management</title>
</head>
<body>
    <h1>Student List</h1>
    <a href="students?action=add">Add New Student</a>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Age</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student.id}</td>
                <td>${student.name}</td>
                <td>${student.age}</td>
                <td>
                    <a href="students?action=delete&id=${student.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
[26-12-2024 11:15 PM] Shrijitha Dsu: <!DOCTYPE html>
<html>
<head>
    <title>Add Student</title>
</head>
<body>
    <h1>Add Student</h1>
    <form action="students" method="post">
        <label for="name">Name:</label>
        <input type="text" name="name" required><br>
        <label for="age">Age:</label>
        <input type="number" name="age" required><br>
        <button type="submit">Add</button>
    </form>
</body>
</html>
[26-12-2024 11:15 PM] Shrijitha Dsu: import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private List<Student> students = new ArrayList<>();

    public List<Student> getAllStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void deleteStudent(int id) {
        students.removeIf(student -> student.getId() == id);
    }

    public Student getStudentById(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    public void updateStudent(int id, String name, int age) {
        Student student = getStudentById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
        }
    }
}
[26-12-2024 11:15 PM] Shrijitha Dsu: import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    private StudentService studentService = new StudentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                response.getWriter().println("Student List:");
                for (Student student : studentService.getAllStudents()) {
                    response.getWriter().println(student);
                }
                break;
            case "add":
                response.getWriter().println("To add a student, send a POST request.");
                break;
            case "delete":
                int id = Integer.parseInt(request.getParameter("id"));
                studentService.deleteStudent(id);
                response.getWriter().println("Student deleted.");
                break;
            case "update":
                response.getWriter().println("To update a student, send a POST request with 'update' action.");
                break;
            default:
                response.getWriter().println("Invalid action.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String name = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            int id = studentService.getAllStudents().size() + 1;

            Student student = new Student(id, name, age);
            studentService.addStudent(student);

            response.getWriter().println("Student added: " + student);
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));

            studentService.updateStudent(id, name, age);
            response.getWriter().println("Student updated: " + studentService.getStudentById(id));
        } else {
            response.getWriter().println("Invalid POST action.");
        }
    }
}
[26-12-2024 11:15 PM] Shrijitha Dsu: public class Main {
    public static void main(String[] args) {
        StudentService service = new StudentService();

        // Add Students
        service.addStudent(new Student(1, "Alice", 20));
        service.addStudent(new Student(2, "Bob", 22));
        service.addStudent(new Student(3, "Charlie", 21));

        // List Students
        System.out.println("Students List:");
        for (Student student : service.getAllStudents()) {
            System.out.println(student);
        }

        // Update Student
        service.updateStudent(2, "Bob Updated", 23);
        System.out.println("\nAfter Update:");
        for (Student student : service.getAllStudents()) {
            System.out.println(student);
        }

        // Delete Student
        service.deleteStudent(1);
        System.out.println("\nAfter Deletion:");
        for (Student student : service.getAllStudents()) {
            System.out.println(student);
        }
    }
}

