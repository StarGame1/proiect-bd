package sample;

import javafx.beans.Observable;
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
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControllerUI implements Initializable {

    //SEARCH BAR
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

    ObservableList<SearchModel> SearchModelObservableList= FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource){
        DatabaseConnection connect  = new DatabaseConnection();
        Connection connectDB= connect.getConnection();
        String query="SELECT nume, prenume, role from Users";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput=statement.executeQuery(query);

            while (queryOutput.next()){
                String numeQuery=queryOutput.getString("nume");
                String prenumeQuery=queryOutput.getString("prenume");
                String rolQuery=queryOutput.getString("role");

                SearchModelObservableList.add(new SearchModel(numeQuery, prenumeQuery, rolQuery));
            }
            numeColoana.setCellValueFactory(new PropertyValueFactory<>("nume"));
            prenumeColoana.setCellValueFactory(new PropertyValueFactory<>("prenume"));
            rolColoana.setCellValueFactory(new PropertyValueFactory<>("rol"));

            TableView.setItems(SearchModelObservableList);
            FilteredList<SearchModel> filteredData = new FilteredList<>(SearchModelObservableList, b -> true);
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(SearchModel->{
                    if(newValue.isEmpty() || newValue.isBlank() || newValue==null){
                        return true;
                    }
                    String searchKeyword = newValue.toLowerCase();
                    if(SearchModel.getNume().toLowerCase().indexOf(searchKeyword)>-1){
                        return true;
                    } else if (SearchModel.getRol().toLowerCase().indexOf(searchKeyword)>-1) {
                        return true;
                        
                    } else if (SearchModel.getPrenume().toLowerCase().indexOf(searchKeyword)>-1) {
                        return true;
                    }else {
                        return false;
                    }
                });
            });
            SortedList<SearchModel> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(TableView.comparatorProperty());

            TableView.setItems(sortedData);
        }
        catch (SQLException e){
            Logger.getLogger(ControllerUI.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

    }

    @FXML
    public void makeSearchAppear(KeyEvent event){
        if (searchField.getText().isEmpty()) {
            TableView.setVisible(false);
        }else {
            TableView.setVisible(true);
        }
    }
    //END OF SEARCHBAR

    //LOG OUT BUTTON

    private Stage stage;
    private Scene scene;
    private FXMLLoader root;
    @FXML
    private Button logOutButton;
    @FXML
    public void logOutAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    //END OF LOG OUT BUTTON

    //chat

    @FXML
    private TextArea chatTextArea;

    @FXML
    private TextField messageTextField;

    private int userId = 5; // Replace with the actual user ID
    private DatabaseConnection databaseConnection = new DatabaseConnection();
    private ChatDAO chatDAO = new ChatDAO(databaseConnection);



    @FXML
    private void sendMessageAction() {
        String message = messageTextField.getText().trim();
        if (!message.isEmpty()) {
            // Replace with the actual group ID
            int groupId = 901; // Change this according to your group ID
            GroupChatMessage groupChatMessage = new GroupChatMessage();
            groupChatMessage.setSenderId(userId);
            groupChatMessage.setGroupId(groupId);
            groupChatMessage.setMessageText(message);

            // Save the group chat message to the database
            saveGroupChatMessage(groupChatMessage);

            // Update the UI (append the message to chatTextArea)
            chatTextArea.appendText("You: " + message + "\n");
            messageTextField.clear();
        }
    }

    private void saveGroupChatMessage(GroupChatMessage groupChatMessage) {
        try (Connection connectDB = databaseConnection.getConnection();
             PreparedStatement statement = connectDB.prepareStatement(
                     "INSERT INTO GroupChatMessages (sender_id, group_id, message_text) VALUES (?, ?, ?)")) {
            statement.setInt(1, groupChatMessage.getSenderId());
            statement.setInt(2, groupChatMessage.getGroupId());
            statement.setString(3, groupChatMessage.getMessageText());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadGroupChatHistory() {
        // Replace with the actual group ID
        int groupId = 901; // Change this according to your group ID
        loadGroupChatHistory(groupId, chatTextArea);
    }

    private void loadGroupChatHistory(int groupId, TextArea chatTextArea) {
        List<GroupChatMessage> groupChatHistory = new ArrayList<>();

        try (Connection connectDB = databaseConnection.getConnection();
             PreparedStatement statement = connectDB.prepareStatement(
                     "SELECT * FROM GroupChatMessages WHERE group_id = ?")) {
            statement.setInt(1, groupId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    GroupChatMessage groupChatMessage = new GroupChatMessage();
                    groupChatMessage.setMessageId(resultSet.getInt("message_id"));
                    groupChatMessage.setSenderId(resultSet.getInt("sender_id"));
                    groupChatMessage.setGroupId(resultSet.getInt("group_id"));
                    groupChatMessage.setMessageText(resultSet.getString("message_text"));
                    groupChatMessage.setTimestamp(resultSet.getTimestamp("timestamp"));

                    groupChatHistory.add(groupChatMessage);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (GroupChatMessage groupChatMessage : groupChatHistory) {
            chatTextArea.appendText(groupChatMessage.getSenderId() + ": " + groupChatMessage.getMessageText() + "\n");
        }
    }







    //end chat

}
