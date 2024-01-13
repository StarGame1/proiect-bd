package sample;

import javafx.event.ActionEvent;
import  javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class ControllerLogin {
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label labelNotSuccesful;
    @FXML
    private Label labelSuccesful;

    @FXML
    public void initialize() {
        submitButton.setOnAction(event -> {
            try {
                loginButtonAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void cancelButtonAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    private boolean validateFields() {

        return !usernameTextField.getText().isEmpty() &&
                !passwordTextField.getText().isEmpty();

    }


    @FXML
    public void loginButtonAction(ActionEvent event) throws IOException {
        if (validateLogin()) {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();


            System.out.println("Login successful for user: " + username);
            labelNotSuccesful.setVisible(false);
            switchToUi(event);

        } else {
            System.out.println("Failed.");
            labelNotSuccesful.setVisible(true);
        }
    }

    public boolean validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM Users WHERE username='" + usernameTextField.getText() + "' AND password = '" + passwordTextField.getText() + "'";
        boolean ok = false;
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    ok = true;
                    return ok;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Stage stage;
    private Scene scene;
    private FXMLLoader root;
    @FXML
    private Button switchToRegister;

    @FXML private Rectangle dragNode;



    @FXML
    public void switchToRegisterScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void switchToUi(ActionEvent event) throws IOException {
        if (event.getSource() instanceof Node) {
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();

            if (validateLogin()) {
                Parent root = FXMLLoader.load(getClass().getResource("userinterface.fxml"));
                Stage stage = (Stage) scene.getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }


}





