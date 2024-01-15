package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUserDialogController {

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
    private User userData;

    public void setParentController(ControllerAdmin parentController) {
        this.parentController = parentController;
    }

    public void setUserData(User userData) {
        this.userData = userData;
        usernameField.setText(userData.getUsername());
        roleField.setText(userData.getRole());
        passwordField.setText(userData.getPassword());
        cnpField.setText(userData.getCnp());
        numeField.setText(userData.getNume());
        prenumeField.setText(userData.getPrenume());
        adresaField.setText(userData.getAdresa());
        nrTelefonField.setText(userData.getNrTelefon());
        emailField.setText(userData.getEmail());
        ibanField.setText(userData.getIban());
        nrContractField.setText(String.valueOf(userData.getNrContract()));
    }

    @FXML
    public void editUser() {
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
        int newNrcontract= Integer.parseInt(nrContractField.getText());

        parentController.editExistingUser(userData, newUsername, newPassword, newRole, newCnp, newNume,
                newPrenume, newAdresa, newNrTelefon, newEmail, newIban, newNrcontract);

        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void cancelEdit() {
        // Închideți fereastra de dialog fără a face nicio acțiune
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
