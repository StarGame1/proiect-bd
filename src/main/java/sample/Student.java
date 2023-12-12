package sample;
public class Student {
    private int studentId;
    private int userId;
    private int anStudii;

    // Constructors
    public Student() {
    }

    public Student(int studentId, int userId, int anStudii) {
        this.studentId = studentId;
        this.userId = userId;
        this.anStudii = anStudii;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAnStudii() {
        return anStudii;
    }

    public void setAnStudii(int anStudii) {
        this.anStudii = anStudii;
    }

}
