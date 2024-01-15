package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Professor {
    private int professorId;


    private String professorName;
    private int userId;
    private int minHours;
    private int maxHours;
    private String departament;

    public Professor() {
    }
    public StringProperty professorNameProperty() {
        return new SimpleStringProperty(professorName);
    }
    public Professor(int professorId, String professorName,int userId, int minHours, int maxHours, String departament) {
        this.professorId = professorId;
        this.professorName=professorName;
        this.userId = userId;
        this.minHours = minHours;
        this.maxHours = maxHours;
        this.departament = departament;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }
    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMinHours() {
        return minHours;
    }

    public void setMinHours(int minHours) {
        this.minHours = minHours;
    }

    public int getMaxHours() {
        return maxHours;
    }

    public void setMaxHours(int maxHours) {
        this.maxHours = maxHours;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }
}