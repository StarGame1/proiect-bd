package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentGrade {
    private final String studentName;
    private final double grade;

    public StudentGrade(String studentName, double grade) {
        this.studentName = studentName;
        this.grade = grade;
    }

    public String studentNameProperty() {
        return studentName;
    }

    public double gradeProperty() {
        return grade;
    }
}

