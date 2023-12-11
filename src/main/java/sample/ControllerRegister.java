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

    @FXML
    public void loginButtonAction(ActionEvent event) {
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
            validateLogin();

            System.out.println("Login successful for user: " + username);
        } else {
            System.out.println("Please fill in all fields.");
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