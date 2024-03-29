package sample;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerUI implements Initializable {
    @FXML
    private TableView<SearchModel> TableView;
    @FXML
    private TableColumn<SearchModel, String> numeColoana;
    @FXML
    private TableColumn<SearchModel, String> prenumeColoana;
    @FXML
    private TableColumn<SearchModel, String> rolColoana;
    @FXML
    private TextField searchField;
    ObservableList<SearchModel> SearchModelObservableList = FXCollections.observableArrayList();
    private Stage stage;
    private Scene scene;
    private FXMLLoader root;
    @FXML
    private Button logOutButton;
    @FXML
    private TextArea chatTextArea;
    @FXML
    private TextField messageTextField;
    private User loggedInUser;
    private int userId = 5;
    private DatabaseConnection databaseConnection = new DatabaseConnection();
    private ChatDAO chatDAO;
    @FXML
    private ListView<String> listOfCourses;




    public ControllerUI() {
        this.chatDAO = new ChatDAO(this.databaseConnection);
    }
    public void setLoggedInUser(User user)
    {
        this.loggedInUser = user;
    }

    public void initialize(URL url, ResourceBundle resource) {
        DatabaseConnection connect = new DatabaseConnection();
        Connection connectDB = connect.getConnection();

        String query = "SELECT nume, prenume, role from Users";
        loadGroupChatHistory();
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query);

            while (queryOutput.next()) {
                String numeQuery = queryOutput.getString("nume");
                String prenumeQuery = queryOutput.getString("prenume");
                String rolQuery = queryOutput.getString("role");
                this.SearchModelObservableList.add(new SearchModel(numeQuery, prenumeQuery, rolQuery));
            }
            String courseQuery = "SELECT course_name FROM Courses";
            Statement courseStatement = connectDB.createStatement();
            ResultSet courseQueryOutput = courseStatement.executeQuery(courseQuery);

            ObservableList<String> courseList = FXCollections.observableArrayList();
            while (courseQueryOutput.next()) {
                String courseName = courseQueryOutput.getString("course_name");
                courseList.add(courseName);
            }

            listOfCourses.setItems(courseList);

            this.numeColoana.setCellValueFactory(new PropertyValueFactory("nume"));
            this.prenumeColoana.setCellValueFactory(new PropertyValueFactory("prenume"));
            this.rolColoana.setCellValueFactory(new PropertyValueFactory("rol"));
            this.TableView.setItems(this.SearchModelObservableList);
            FilteredList<SearchModel> filteredData = new FilteredList(this.SearchModelObservableList, (b) -> {
                return true;
            });
            this.searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((SearchModel) -> {
                    if (!newValue.isEmpty() && !newValue.isBlank() && newValue != null) {
                        String searchKeyword = newValue.toLowerCase();
                        if (SearchModel.getNume().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else if (SearchModel.getRol().toLowerCase().indexOf(searchKeyword) > -1) {
                            return true;
                        } else {
                            return SearchModel.getPrenume().toLowerCase().indexOf(searchKeyword) > -1;
                        }
                    } else {
                        return true;
                    }
                });
            });
            SortedList<SearchModel> sortedData = new SortedList(filteredData);
            sortedData.comparatorProperty().bind(this.TableView.comparatorProperty());
            this.TableView.setItems(sortedData);

            // Load group chat history when initializing the UI
           // loadGroupChatHistory();
        } catch (SQLException var11) {
            Logger.getLogger(ControllerUI.class.getName()).log(Level.SEVERE, (String) null, var11);
            var11.printStackTrace();
        }
    }
    private String showUpdateDialog(String oldCourseName) {
        TextInputDialog dialog = new TextInputDialog(oldCourseName);
        dialog.setTitle("Update Course");
        dialog.setHeaderText("Update Course Name");
        dialog.setContentText("New Course Name:");
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
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
        try (Connection connectDB = connect.getConnection()) {
            String updateQuery = "UPDATE Courses SET course_name = ? WHERE course_name = ?";
            try (PreparedStatement preparedStatement = connectDB.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, newCourseName);
                preparedStatement.setString(2, oldCourseName);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            Logger.getLogger(ProfInterface.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

    @FXML
    public void onShowPersonalData() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Connection connection = DatabaseConnection.getConnection();
        String loggedInUserString = this.loggedInUser.getUsername();

        alert.setTitle("User Information");
        alert.setHeaderText("Username: " + loggedInUser.getUsername());
        alert.setContentText("Role: " + loggedInUser.getRole() + "\n" +
                "Nume: " + getSingleInfo(connection, "Users", loggedInUserString, "nume") + "\n" +
                "Prenume: " + getSingleInfo(connection, "Users", loggedInUserString, "prenume") + "\n"+
                "CNP: " + getSingleInfo(connection, "Users", loggedInUserString, "cnp") + "\n"+
                "Adresa: " + getSingleInfo(connection, "Users", loggedInUserString, "adresa") + "\n"+
                "Nr. Telefon: " + getSingleInfo(connection, "Users", loggedInUserString, "nr_telefon") + "\n"+
                "Email: " + getSingleInfo(connection, "Users", loggedInUserString, "email") + "\n"+
                "Iban: " + getSingleInfo(connection, "Users", loggedInUserString, "iban") + "\n"+
                "Nr. Contract: " + getSingleInfo(connection, "Users", loggedInUserString, "nr_contract") + "\n");


        // Get the main stage from any control within the current scene
        Stage mainStage = (Stage) TableView.getScene().getWindow();

        // Set the owner of the alert to the main stage
        alert.initOwner(mainStage);

        alert.showAndWait();
    }
    @FXML
    public int getid() {

        Connection connection = DatabaseConnection.getConnection();
        String loggedInUserString = this.loggedInUser.getUsername();
            int id = Integer.parseInt(getSingleInfo(connection, "Users", loggedInUserString, "user_id"));



                return id;

    }




    @FXML
    public void makeSearchAppear(KeyEvent event) {
        if (this.searchField.getText().isEmpty()) {
            this.TableView.setVisible(false);
        } else {
            this.TableView.setVisible(true);
        }
    }

    @FXML
    public void logOutAction(ActionEvent event) throws IOException {
        Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("login.fxml"));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.scene = new Scene(root);
        this.stage.setScene(this.scene);
        this.stage.show();
    }
    public void loadChatAction(ActionEvent ACTION){
        loadGroupChatHistory();
    }

    @FXML
    private void sendMessageAction() {
        String message = this.messageTextField.getText().trim();

        if (!message.isEmpty()) {
            int groupId = 901; // Replace with the actual group ID
            GroupChatMessage groupChatMessage = new GroupChatMessage();
            groupChatMessage.setSenderId(this.userId);
            groupChatMessage.setGroupId(groupId);
            groupChatMessage.setMessageText(message);
            this.saveGroupChatMessage(groupChatMessage);
            this.chatTextArea.appendText("You: " + message + "\n");
            this.messageTextField.clear();

        }
    }

    private void saveGroupChatMessage(GroupChatMessage groupChatMessage) {
        try {
            Connection connectDB = this.databaseConnection.getConnection();

            try {
                PreparedStatement statement = connectDB.prepareStatement("INSERT INTO GroupChatMessages (sender_id, group_id, message_text) VALUES (?, ?, ?)");

                try {
                    statement.setInt(1, groupChatMessage.getSenderId());
                    statement.setInt(2, groupChatMessage.getGroupId());
                    statement.setString(3, groupChatMessage.getMessageText());
                    statement.executeUpdate();
                } catch (Throwable var8) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }

                    throw var8;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var9) {
                if (connectDB != null) {
                    try {
                        connectDB.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                }

                throw var9;
            }

            if (connectDB != null) {
                connectDB.close();
            }
        } catch (SQLException var10) {
            var10.printStackTrace();
        }
    }

    private void loadGroupChatHistory() {
        int groupId = 901; // Replace with the actual group ID
        this.loadGroupChatHistory(groupId, this.chatTextArea);
    }

    public static String getSingleInfo(Connection connection, String tableName, String username, String info)
    {
        String query = "SELECT " + info + " FROM " + tableName + " WHERE username = ?";

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return resultSet.getString(info);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private void loadGroupChatHistory(int groupId, TextArea chatTextArea) {
        List<GroupChatMessage> groupChatHistory = new ArrayList<>();

        try {
            Connection connectDB = databaseConnection.getConnection();

            try {
                String query = "SELECT g.*, u.nume AS sender_name FROM GroupChatMessages g " +
                        "JOIN Users u ON g.sender_id = u.user_id " +
                        "WHERE g.group_id = ?";

                PreparedStatement statement = connectDB.prepareStatement(query);

                try {
                    statement.setInt(1, groupId);
                    ResultSet resultSet = statement.executeQuery();

                    try {
                        while (resultSet.next()) {
                            GroupChatMessage groupChatMessage = new GroupChatMessage();
                            groupChatMessage.setMessageId(resultSet.getInt("message_id"));
                            groupChatMessage.setSenderId(resultSet.getInt("sender_id"));
                            groupChatMessage.setGroupId(resultSet.getInt("group_id"));
                            groupChatMessage.setMessageText(resultSet.getString("message_text"));
                            groupChatMessage.setTimestamp(resultSet.getTimestamp("timestamp"));
                            groupChatMessage.setSenderName(resultSet.getString("sender_name"));

                            groupChatHistory.add(groupChatMessage);
                        }
                    } catch (Throwable var12) {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (Throwable var11) {
                                var12.addSuppressed(var11);
                            }
                        }

                        throw var12;
                    }

                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (Throwable var13) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var10) {
                            var13.addSuppressed(var10);
                        }
                    }

                    throw var13;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var14) {
                if (connectDB != null) {
                    try {
                        connectDB.close();
                    } catch (Throwable var9) {
                        var14.addSuppressed(var9);
                    }
                }

                throw var14;
            }

            if (connectDB != null) {
                connectDB.close();
            }

            Iterator<GroupChatMessage> iterator = groupChatHistory.iterator();

            while (iterator.hasNext()) {
                GroupChatMessage groupChatMessage = iterator.next();
                chatTextArea.appendText(groupChatMessage.getSenderName() + ": " +
                        groupChatMessage.getMessageText() + "\n");
            }
        } catch (SQLException var15) {
            var15.printStackTrace();
        }
    }



}
