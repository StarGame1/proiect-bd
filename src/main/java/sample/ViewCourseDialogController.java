package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewCourseDialogController {
    private ControllerAdmin parentController;
    public void setParentController(ControllerAdmin parentController) {
        this.parentController = parentController;
    }
    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableView<Professor> professorTable;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Professor, String> professorNameColumn;
    @FXML
    private TextField searchCourseField;
    @FXML
    private Button viewStudentsButton;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private ObservableList<Professor> professorList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Inițializează tabelul pentru cursuri și profesori
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().courseNameProperty());
        professorNameColumn.setCellValueFactory(cellData -> cellData.getValue().professorNameProperty());

        // Încarcă datele despre cursuri și profesori
        loadCourseData();
        loadProfessorData();
    }

    private void loadCourseData() {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String query = "SELECT * FROM Courses";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Course course = new Course(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("professor_name"),
                        resultSet.getInt("professor_id")
                );
                courseList.add(course);
            }
            courseTable.setItems(courseList);
            statement.close();
            resultSet.close();
            connectDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProfessorData() {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String query = "SELECT * FROM Profesori";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Professor professor = new Professor(
                        resultSet.getInt("professor_id"),
                        resultSet.getString("professor_name"),
                        resultSet.getInt("user_id"),
                        resultSet.getInt("min_hours"),
                        resultSet.getInt("max_hours"),
                        resultSet.getString("departament")
                );
                professorList.add(professor);
            }
            professorTable.setItems(professorList);
            statement.close();
            resultSet.close();
            connectDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void searchCourse(KeyEvent event) {
        String searchTerm = searchCourseField.getText().toLowerCase();

        ObservableList<Course> filteredList = courseList.filtered(course ->
                course.getCourseName().toLowerCase().contains(searchTerm)
        );

        courseTable.setItems(filteredList);
    }

    @FXML
    public void viewStudents(ActionEvent event) {
        // Implementează acțiunea pentru vizualizarea studenților înscriși la cursul selectat
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            // Deschide o fereastră sau afișează studenții înscriși la cursul selectat
            // Poți implementa această funcționalitate aici
            // ...
        } else {
            showAlert("Please select a course from the table.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goBack(ActionEvent event) {
        // Închide fereastra curentă
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
