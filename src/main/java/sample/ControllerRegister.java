package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ControllerRegister {
    @FXML
    private Button cancelButton;
    private Button loginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField cnpTextField;
    @FXML
    private RadioButton studentRole;
    @FXML
    private RadioButton profesorRole;
    @FXML
    private RadioButton adminRole;
    @FXML
    private TextField numeTextField;
    @FXML
    private TextField prenumeTextField;
    @FXML
    private TextField telTextField;
    @FXML
    private TextField adresaTextField;
    @FXML
    private TextField ibanTextField;
    @FXML
    private TextField contractTextField;

    public void cancelButtonAction(ActionEvent e){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    private boolean validateFields() {

        return !usernameTextField.getText().isEmpty() &&
                !passwordTextField.getText().isEmpty() &&
                !emailTextField.getText().isEmpty() &&
                !cnpTextField.getText().isEmpty() &&
                !numeTextField.getText().isEmpty() &&
                !prenumeTextField.getText().isEmpty() &&
                !telTextField.getText().isEmpty() &&
                !adresaTextField.getText().isEmpty() &&
                !ibanTextField.getText().isEmpty() &&
                !contractTextField.getText().isEmpty() &&
                (studentRole.isSelected() || profesorRole.isSelected() || adminRole.isSelected()) &&
                onlyOneRoleSelected();
    }

    private boolean onlyOneRoleSelected() {
        return (studentRole.isSelected() && !profesorRole.isSelected() && !adminRole.isSelected()) ||
                (!studentRole.isSelected() && profesorRole.isSelected() && !adminRole.isSelected()) ||
                (!studentRole.isSelected() && !profesorRole.isSelected() && adminRole.isSelected());
    }
    private String getSelectedRole() {
        if (studentRole.isSelected()) {
            return "student";
        } else if (profesorRole.isSelected()) {
            return "profesor";
        } else if (adminRole.isSelected()) {
            return "administrator";
        }
        return null; // None selected
    }

    @FXML
    public void loginButtonAction(ActionEvent event) throws IOException {
        if (validateFields()) {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String email = emailTextField.getText();
            String cnp = cnpTextField.getText();
            String nume = numeTextField.getText();
            String prenume = prenumeTextField.getText();
            String tel = telTextField.getText();
            String adresa = adresaTextField.getText();
            String iban = ibanTextField.getText();
            String contract = contractTextField.getText();
            String selectedRole = getSelectedRole();
            if(validateFields()) {
                createUser(username, password, email, cnp, nume, prenume, tel, adresa, iban, contract, selectedRole);
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }

            System.out.println("Login successful for user: " + username);
        } else {
            System.out.println("Please fill in all fields.");
        }
    }
    private void createUser(String username, String password, String email, String cnp, String nume, String prenume,
                            String tel, String adresa, String iban, String contract, String role) {
        try {
            DatabaseConnection connect = new DatabaseConnection();
            Connection connectDB = connect.getConnection();

            // Define the SQL query to insert a new user
            String insertUserQuery = "INSERT INTO Users (username, password, role, cnp, nume, prenume, adresa, nr_telefon, email, iban, nr_contract) " +
                    "VALUES ('" + username + "', '" + password + "', '" + role + "', '" + cnp + "', '" + nume + "', '" +
                    prenume + "', '" + adresa + "', '" + tel + "', '" + email + "', '" + iban + "', '" + contract + "')";

            // Create a statement and execute the query
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertUserQuery);

            // Close the statement and connection
            statement.close();
            connectDB.close();

            System.out.println("User created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    public void validateLogin(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB=connectNow.getConnection();

        String verifyLogin="SELECT count(1) FROM Users WHERE username='"+usernameTextField.getText()+"' AND password = '"+passwordTextField.getText()+"'";

        try{
            Statement statement=connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1)==1){

                }
            }
        }
        catch (Exception e){

        }
    }


    private Stage stage;
    private Scene scene;
    private FXMLLoader root;
    @FXML
    private Button switchToLogin;
    public void switchToLoginScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}