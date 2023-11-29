/** Controls interaction with various screens
 *
 */
package controller;

import dao.LoginDAO;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/** Class for controlling the login screen
 *
 */
public class LoginScreenController implements Initializable {



    Stage stage;
    Parent scene;

    String languageCode;

    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private Label loginTitleLabel;
    @FXML
    private Label loginUsernameLabel;
    @FXML
    private Label loginPasswordLabel;
    @FXML
    private Label localeIdLabel;
    @FXML
    private Button loginLoginButton;

    /** Initialization method for login screen
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageCode = Locale.getDefault().getLanguage();
        if (languageCode.equals("en")) {
            localeIdLabel.setText("ZoneID: " + ZoneId.systemDefault().toString());
        } else if (languageCode.equals("fr")) {
            localeIdLabel.setText("Locale actuelle: " + ZoneId.systemDefault().toString());
            userNameTextField.setPromptText("Nom d'utilisateur");
            passwordPasswordField.setPromptText("Mot de passe");
            loginUsernameLabel.setText("Nom d'utilisateur");
            loginPasswordLabel.setText("Mot de passe");
            loginLoginButton.setText("Connexion");
        }
    }

    /** Logs in user if credentials are correct. Shows alert in English or French based on error
     *
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onLoginButtonAction(ActionEvent actionEvent) throws SQLException, IOException {
        String userName = userNameTextField.getText();
        String password = passwordPasswordField.getText();
        if (checkEmptyUsernamePassword(userName, password)) {
            try {
                LoginDAO.checkLogin(userName, password);
                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(AlertType.ERROR);

                if (languageCode.equals("en")) {
                    alert.setTitle("Error");
                    if (e.getMessage().equals("Incorrect password")) {
                        alert.setHeaderText("Incorrect password");
                        alert.setContentText("The password you entered is incorrect. Please try again.");
                    } else if (e.getMessage().equals("Username not found")) {
                        alert.setHeaderText("User not found");
                        alert.setContentText("The user you entered was not found. Please try again.");
                    }

                } else if (languageCode.equals("fr")) {
                    alert.setTitle("Erreur");
                    if (e.getMessage().equals("Incorrect password")) {
                        alert.setHeaderText("Mot de passe incorrect");
                        alert.setContentText("Le mot de passe que vous avez entré est incorrect. Veuillez réessayer.");
                    } else if (e.getMessage().equals("Username not found")) {
                        alert.setHeaderText("Utilisateur introuvable");
                        alert.setContentText("L'utilisateur que vous avez entré n'a pas été trouvé. Veuillez réessayer.");
                    }
                }

                alert.showAndWait();
                userNameTextField.clear();
                passwordPasswordField.clear();
            }
        }
    }

    /** Checks if username or password are empty
     *
     * @param userName
     * @param password
     * @return
     */
    private boolean checkEmptyUsernamePassword(String userName, String password) {
        if (userName.trim().isEmpty() || password.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            userNameTextField.clear();
            passwordPasswordField.clear();

            if (languageCode.equals("en")) {
                alert.setTitle("Error");
                alert.setHeaderText("Empty Fields");
                alert.setContentText("Please enter a username and password.");
            } else if (languageCode.equals("fr")) {
                alert.setTitle("Error");
                alert.setHeaderText("Champs vides");
                alert.setContentText("Veuillez entrer un nom d'utilisateur et un mot de passe.");

            }
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
