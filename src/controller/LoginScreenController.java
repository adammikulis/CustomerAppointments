package controller;

import helper.LoginQuery;
import helper.SessionManager;
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
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private Label localeIdLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String languageCode = Locale.getDefault().getLanguage();

        if (languageCode.equals("en")) {
            localeIdLabel.setText("Current Locale: " + Locale.getDefault().toString());
        } else if (languageCode.equals("fr")) {
            localeIdLabel.setText("Locale actuelle: " + Locale.getDefault().toString());
            userNameTextField.setPromptText("Nom d'utilisateur");
            passwordPasswordField.setPromptText("Mot de passe");
        }
    }

    public void onLoginButtonAction(ActionEvent actionEvent) throws SQLException, IOException {
        String userName = userNameTextField.getText();
        String password = passwordPasswordField.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            userNameTextField.clear();
            passwordPasswordField.clear();

            if (Locale.getDefault().getLanguage().equals("fr")) {
                alert.setHeaderText("Champs vides");
                alert.setContentText("Veuillez entrer un nom d'utilisateur et un mot de passe.");
            } else {
                alert.setHeaderText("Empty Fields");
                alert.setContentText("Please enter a username and password.");
            }
            alert.showAndWait();
            return;
        }

        try {
            LoginQuery.checkLogin(userName, password);

            SessionManager.getInstance().setCurrentUserName(userName);
            System.out.println("Current user: " + SessionManager.getInstance().getCurrentUserName());

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");

            if (Locale.getDefault().getLanguage().equals("fr")) {
                if (e.getMessage().equals("Incorrect password")) {
                    alert.setHeaderText("Mot de passe incorrect");
                    alert.setContentText("Le mot de passe que vous avez entré est incorrect. Veuillez réessayer.");
                } else if (e.getMessage().equals("User not found")) {
                    alert.setHeaderText("Utilisateur introuvable");
                    alert.setContentText("L'utilisateur que vous avez entré n'a pas été trouvé. Veuillez réessayer.");
                }
            } else {
                if (e.getMessage().equals("Incorrect password")) {
                    alert.setHeaderText("Incorrect password");
                    alert.setContentText("The password you entered is incorrect. Please try again.");
                } else if (e.getMessage().equals("User not found")) {
                    alert.setHeaderText("User not found");
                    alert.setContentText("The user you entered was not found. Please try again.");
                }
            }

            alert.showAndWait();
            userNameTextField.clear();
            passwordPasswordField.clear();
        }
    }

    public void loginActivity() throws IOException {
        try {
            File loginActivity = new File("login_activity.txt");
            loginActivity.createNewFile();

        } catch (IOException e) {
            System.out.println("IO Exception " + e);
        }
        // Write code that provides the ability to track user activity by recording all user log-in attempts, dates, and time stamps and whether each attempt was successful in a file named login_activity.txt. Append each new record to the existing file, and save to the root folder of the application.
    }
}
