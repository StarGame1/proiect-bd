package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerAdmin {
    @FXML
    public TableColumn useridColumn;
    @FXML
    public TextField searchField;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private Button addUserButton;
    @FXML
    private Button updateUserButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button allocateProfessorButton;
    @FXML
    private TextField usernameField;

    @FXML
    private ListView<String> listOfCourses;
    @FXML
    private TextField roleField;
    @FXML
    private TextField Course;

    private ObservableList<User> userList = FXCollections.observableArrayList();
    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableView<Professor> profesoriTable;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, String> professorColumn;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private ObservableList<Professor> professorList = FXCollections.observableArrayList();
    @FXML
    private Button editUserButton;
    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());

        // Inițializează tabela cu datele din baza de date
        loadUserData();
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editUserButton.setDisable(false); // Activează butonul de editare când este selectat un utilizator
            } else {
                editUserButton.setDisable(true);  // Dezactivează butonul de editare când nu este selectat niciun utilizator
            }
            Connection connectDB = DatabaseConnection.getConnection();
            String courseQuery;
            courseQuery = "SELECT course_name FROM Courses";
            Statement courseStatement = null;
            try {
                courseStatement = connectDB.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ResultSet courseQueryOutput = null;
            try {
                courseQueryOutput = courseStatement.executeQuery(courseQuery);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ObservableList<String> courseList = FXCollections.observableArrayList();

            while(true) {
                try {
                    if (!courseQueryOutput.next()) break;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                String courseName = null;
                try {
                    courseName = courseQueryOutput.getString("course_name");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                courseList.add(courseName);
            }

            this.listOfCourses.setItems(courseList);
        });
    }

    @FXML
    void addCourse(MouseEvent event) {
        String courseName = this.Course.getText();
        if (!courseName.isEmpty()) {
            this.addCourseToDatabase(courseName);
            this.listOfCourses.getItems().add(courseName);
            this.Course.clear();
        } else {
            this.showAlert("Numele cursului nu poate fi gol!");
        }

    }

    @FXML
    void removeCourse(MouseEvent event) {
        String selectedCourse = (String)this.listOfCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmare ștergere");
            alert.setHeaderText("Sigur doriți să ștergeți acest curs?");
            alert.setContentText("Curs: " + selectedCourse);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                this.removeCourseFromDatabase(selectedCourse);
                this.listOfCourses.getItems().remove(selectedCourse);
            }
        } else {
            this.showAlert("Selectați un curs pentru a îl șterge.");
        }

    }

    private void addCourseToDatabase(String courseName) {
        DatabaseConnection connect = new DatabaseConnection();

        try {
            Connection connectDB = DatabaseConnection.getConnection();

            try {
                String insertQuery = "INSERT INTO Courses (course_name) VALUES (?)";
                PreparedStatement preparedStatement = connectDB.prepareStatement(insertQuery);

                try {
                    preparedStatement.setString(1, courseName);
                    preparedStatement.executeUpdate();
                } catch (Throwable var10) {
                    if (preparedStatement != null) {
                        try {
                            preparedStatement.close();
                        } catch (Throwable var9) {
                            var10.addSuppressed(var9);
                        }
                    }

                    throw var10;
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Throwable var11) {
                if (connectDB != null) {
                    try {
                        connectDB.close();
                    } catch (Throwable var8) {
                        var11.addSuppressed(var8);
                    }
                }

                throw var11;
            }

            if (connectDB != null) {
                connectDB.close();
            }
        } catch (SQLException var12) {
            Logger.getLogger(ProfInterface.class.getName()).log(Level.SEVERE, (String)null, var12);
            var12.printStackTrace();
        }

    }

    private void removeCourseFromDatabase(String courseName) {
        DatabaseConnection connect = new DatabaseConnection();

        try {
            Connection connectDB = DatabaseConnection.getConnection();

            try {
                String deleteQuery = "DELETE FROM Courses WHERE course_name = ?";
                PreparedStatement preparedStatement = connectDB.prepareStatement(deleteQuery);

                try {
                    preparedStatement.setString(1, courseName);
                    preparedStatement.executeUpdate();
                } catch (Throwable var10) {
                    if (preparedStatement != null) {
                        try {
                            preparedStatement.close();
                        } catch (Throwable var9) {
                            var10.addSuppressed(var9);
                        }
                    }

                    throw var10;
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Throwable var11) {
                if (connectDB != null) {
                    try {
                        connectDB.close();
                    } catch (Throwable var8) {
                        var11.addSuppressed(var8);
                    }
                }

                throw var11;
            }

            if (connectDB != null) {
                connectDB.close();
            }
        } catch (SQLException var12) {
            Logger.getLogger(ProfInterface.class.getName()).log(Level.SEVERE, (String)null, var12);
            var12.printStackTrace();
        }

    }

    @FXML
    void updateCourse(MouseEvent event) {
        int selectedID = this.listOfCourses.getSelectionModel().getSelectedIndex();
        if (selectedID >= 0) {
            String newCourseName = this.showUpdateDialog((String)this.listOfCourses.getItems().get(selectedID));
            if (newCourseName != null && !newCourseName.isEmpty()) {
                this.updateCourseInDatabase((String)this.listOfCourses.getItems().get(selectedID), newCourseName);
                this.listOfCourses.getItems().set(selectedID, newCourseName);
            }
        }

    }

    private void updateCourseInDatabase(String oldCourseName, String newCourseName) {
        DatabaseConnection connect = new DatabaseConnection();

        try {
            Connection connectDB = DatabaseConnection.getConnection();

            try {
                String updateQuery = "UPDATE Courses SET course_name = ? WHERE course_name = ?";
                PreparedStatement preparedStatement = connectDB.prepareStatement(updateQuery);

                try {
                    preparedStatement.setString(1, newCourseName);
                    preparedStatement.setString(2, oldCourseName);
                    preparedStatement.executeUpdate();
                } catch (Throwable var11) {
                    if (preparedStatement != null) {
                        try {
                            preparedStatement.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }

                    throw var11;
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Throwable var12) {
                if (connectDB != null) {
                    try {
                        connectDB.close();
                    } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                    }
                }

                throw var12;
            }

            if (connectDB != null) {
                connectDB.close();
            }
        } catch (SQLException var13) {
            Logger.getLogger(ProfInterface.class.getName()).log(Level.SEVERE, (String)null, var13);
            var13.printStackTrace();
        }

    }
    private String showUpdateDialog(String oldCourseName) {
        TextInputDialog dialog = new TextInputDialog(oldCourseName);
        dialog.setTitle("Update Course");
        dialog.setHeaderText("Update Course Name");
        dialog.setContentText("New Course Name:");
        Optional<String> result = dialog.showAndWait();
        return (String)result.orElse(null);
    }


    private void loadUserData() {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            String query = "SELECT * FROM Users";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"),
                        resultSet.getString("cnp"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("adresa"),
                        resultSet.getString("nr_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("iban"),
                        resultSet.getInt("nr_contract")
                );
                userList.add(user);
            }

            userTable.setItems(userList);

            statement.close();
            resultSet.close();
            connectDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addUser(MouseEvent event) {
        try {
            // Deschide fereastra pentru adăugare utilizator
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddUserDialog.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            AddUserDialogController controllerAddUser = loader.getController();
            controllerAddUser.setParentController(this);
            //controllerAddUser.setDialogStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void vizualizareCurs(MouseEvent event){
        try {
            // Deschide fereastra pentru vizualizare cursuri
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewCourseDialog.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ViewCourseDialogController controllerViewCourse = loader.getController();
            controllerViewCourse.setParentController(this);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage stage;
    private Scene scene;
    private FXMLLoader root;
    @FXML
    public void logOutAction(ActionEvent event) throws IOException {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("login.fxml"));
        this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.scene = new Scene(root);
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    // Metodă pentru a adăuga un nou utilizator
    public void addNewUserWithDetails(String newUsername, String password, String newRole, String newCnp,
                                      String newNume, String newPrenume, String newAdresa, String newNrTelefon,
                                      String newEmail, String newIban, int newNrContract) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            userList.clear();

            String query = "INSERT INTO Users (username, password, role, cnp, nume, prenume, adresa, nr_telefon, email, iban, nr_contract) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, newRole);
            preparedStatement.setString(4, newCnp);
            preparedStatement.setString(5, newNume);
            preparedStatement.setString(6, newPrenume);
            preparedStatement.setString(7, newAdresa);
            preparedStatement.setString(8, newNrTelefon);
            preparedStatement.setString(9, newEmail);
            preparedStatement.setString(10, newIban);
            preparedStatement.setInt(11, newNrContract);

            preparedStatement.executeUpdate();

            // Reîncarcă datele și afișează-le în TableView
            loadUserData();

            // Actualizează conținutul tabelului
            userTable.setItems(userList);

            showAlert("User added successfully!");
            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            showAlert("Error adding user: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void updateUser(MouseEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();

                String newRole = roleField.getText();

                String query = "UPDATE Users SET role = ? WHERE user_id = ?";
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setString(1, newRole);
                preparedStatement.setInt(2, selectedUser.getUserId());

                preparedStatement.executeUpdate();

                // Reîncarcă datele și afișează-le în TableView
                loadUserData();

                showAlert("User updated successfully!");

                preparedStatement.close();
                connectDB.close();
            } catch (Exception e) {
                showAlert("Error updating user: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a user from the table.");
        }
    }
    @FXML
    public void makeSearchAppear(KeyEvent event) {
        String searchTerm = ((TextField) event.getSource()).getText().toLowerCase();

        ObservableList<User> filteredList = userList.filtered(user ->
                user.getUsername().toLowerCase().contains(searchTerm) ||
                        user.getRole().toLowerCase().contains(searchTerm)
        );

        userTable.setItems(filteredList);
    }


    @FXML
    public void deleteUser(MouseEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();

                String query = "DELETE FROM Users WHERE user_id = ?";
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setInt(1, selectedUser.getUserId());

                preparedStatement.executeUpdate();

                // Șterge utilizatorul din ObservableList
                userList.remove(selectedUser);

                // Reîncarcă datele și afișează-le în TableView
                showAlert("User deleted successfully!");

                preparedStatement.close();
                connectDB.close();
            } catch (Exception e) {
                showAlert("Error deleting user: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a user from the table.");
        }
    }
    public void editExistingUser(User user, String newUsername, String newPassword, String newRole, String newCnp,
                                 String newNume, String newPrenume, String newAdresa, String newNrTelefon,
                                 String newEmail, String newIban, int newNrContract) {
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            String query = "UPDATE Users SET username = ?, password = ?, role = ?, cnp = ?, nume = ?, prenume = ?, " +
                    "adresa = ?, nr_telefon = ?, email = ?, iban = ?, nr_contract = ? WHERE user_id = ?";
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, newUsername);
            preparedStatement.setString(2, newPassword);
            preparedStatement.setString(3, newRole);
            preparedStatement.setString(4, newCnp);
            preparedStatement.setString(5, newNume);
            preparedStatement.setString(6, newPrenume);
            preparedStatement.setString(7, newAdresa);
            preparedStatement.setString(8, newNrTelefon);
            preparedStatement.setString(9, newEmail);
            preparedStatement.setString(10, newIban);
            preparedStatement.setInt(11, newNrContract);
            preparedStatement.setInt(12, user.getUserId());

            preparedStatement.executeUpdate();

            // Actualizează direct utilizatorul în lista userList
            user.setUsername(newUsername);
            user.setPassword(newPassword);
            user.setRole(newRole);
            user.setCnp(newCnp);
            user.setNume(newNume);
            user.setPrenume(newPrenume);
            user.setAdresa(newAdresa);
            user.setNrTelefon(newNrTelefon);
            user.setEmail(newEmail);
            user.setIban(newIban);
            user.setNrContract(newNrContract);

            // Reîncarcă datele și afișează-le în TableView
            userTable.refresh();

            showAlert("User updated successfully!");

            preparedStatement.close();
            connectDB.close();
        } catch (Exception e) {
            showAlert("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void editUser() {
        // Verifică dacă un utilizator este selectat în tabel
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                // Deschide fereastra pentru editare utilizator
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditUserDialog.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                EditUserDialogController controllerEditUser = loader.getController();
                controllerEditUser.setParentController(this);
                controllerEditUser.setUserData(selectedUser); // Pasează informațiile utilizatorului selectat

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a user from the table.");
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
        // Implementează logica pentru a reveni la interfața anterioară
        // ...

        // Închide fereastra curentă
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
