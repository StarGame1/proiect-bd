package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddUserDialogController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField roleField;
    @FXML
    private TextField cnpField;
    @FXML
    private TextField numeField;
    @FXML
    private TextField prenumeField;
    @FXML
    private TextField adresaField;
    @FXML
    private TextField nrTelefonField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField nrContractField;

    private ControllerAdmin parentController;
    private Stage dialogStage;
    public void setParentController(ControllerAdmin parentController) {
        this.parentController = parentController;
    }
    @FXML
    public void addUser() {
        String newUsername = usernameField.getText();
        String newPassword = passwordField.getText();
        String newRole = roleField.getText();
        String newCnp = cnpField.getText();
        String newNume = numeField.getText();
        String newPrenume = prenumeField.getText();
        String newAdresa = adresaField.getText();
        String newNrTelefon = nrTelefonField.getText();
        String newEmail = emailField.getText();
        String newIban = ibanField.getText();
        int newNrContract = Integer.parseInt(nrContractField.getText());

        parentController.addNewUserWithDetails(newUsername, newPassword, newRole, newCnp, newNume, newPrenume,
                newAdresa, newNrTelefon, newEmail, newIban, newNrContract);

        // Închide fereastra dialogului după adăugarea utilizatorului
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
