package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreen implements Initializable {

    public TextField UserNameTextField;
    public TextField PasswordTextField;
    public Label ZoneIdLabel;
    public Label ErrorMessageLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("client db gui initialized");
        Locale currentLocale = Locale.getDefault();
        ZoneIdLabel.setText("Current locale: " + currentLocale);
        ErrorMessageLabel.setText("");

    }

    public void onLoginButtonAction(ActionEvent actionEvent) {
        System.out.println("Login Clicked");
    }
}
