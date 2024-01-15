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

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private User loggedInUser;


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
    private String retrieveRoleFromDatabase(String username) {
        String role = null;

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String retrieveRoleQuery = "SELECT role FROM Users WHERE username = '" + username + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(retrieveRoleQuery);

            if (queryResult.next()) {
                role = queryResult.getString("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return role;
    }


    @FXML
    public void loginButtonAction(ActionEvent event) throws IOException {
        if (validateLogin()) {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            GroupChatMessage user = new GroupChatMessage();
            user.setSenderName(username);
            String role =retrieveRoleFromDatabase(username);

            loggedInUser=new User(username, role);
            System.out.println("Login successful for user: " + username);
            labelNotSuccesful.setVisible(false);
            switchToUi(event, loggedInUser);

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
    public void switchToUi(ActionEvent event, User loggedInUser) throws IOException {
        if (event.getSource() instanceof Node) {
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();


            if (validateLogin()) {
                if (loggedInUser.getRole().equals("profesor")) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfInterface.fxml"));
                    Parent root = loader.load();

                    // Access the controller of ProfInterface
                    ProfInterface profInterfaceController = loader.getController();

                    // Set the logged-in user in ProfInterface
                    profInterfaceController.setLoggedInUser(loggedInUser);

                    Stage stage = (Stage) scene.getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                else if(loggedInUser.getRole().equals("student"))
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("userinterface.fxml"));
                    Parent root = loader.load();

                    ControllerUI controller = loader.getController();

                    controller.setLoggedInUser(loggedInUser);

                    Stage stage = (Stage) scene.getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else if (loggedInUser.getRole().equals("administrator")) {
                    Parent root = FXMLLoader.load(getClass().getResource("AdminInterface.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                }
            }
        }
    }


}





