package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
    private int courseId;
    private String courseName;
    private String professorName;
    private int profesorId;

    // Constructori, getteri și setteri

    public Course() {
    }

    public StringProperty courseNameProperty() {
        return new SimpleStringProperty(courseName);
    }
    public StringProperty professorNameProperty() {
        return new SimpleStringProperty(professorName);
    }
    public Course(int courseId, String courseName, String professorName,int profesorId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.professorName = professorName;
        this.profesorId=profesorId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }
}
