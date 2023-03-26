package controller;

import helper.LoginHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {


    Stage stage;
    Parent scene;


    @FXML
    private TextField UserNameTextField;
    @FXML
    private TextField PasswordTextField;
    @FXML
    private Label ZoneIdLabel;
    @FXML
    private Label ErrorMessageLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale currentLocale = Locale.getDefault();
        ZoneIdLabel.setText("Current locale: " + currentLocale);
        ErrorMessageLabel.setText("");

    }

    public void onLoginButtonAction(ActionEvent actionEvent) throws SQLException, IOException {
        if (LoginHelper.check_login(UserNameTextField.getText(), PasswordTextField.getText())) {
            // Switch to HomeScreenController.fxml
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            System.out.println("Login Successful!");
        }
        else {
            System.out.println("Login Failed");
        }
    }
}
