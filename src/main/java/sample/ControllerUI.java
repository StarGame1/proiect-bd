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

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

}
