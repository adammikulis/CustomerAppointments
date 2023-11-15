package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {

    @FXML
    private ComboBox homeScreenContactComboBox;
    Stage stage;
    Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void onViewAllAppointmentsButtonPressed(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onAddUpdateClientButtonPressed(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ClientScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void onHomeScreenExitButtonPressed(ActionEvent actionEvent) throws IOException {
        System.exit(0); // Exit the JavaFX application
    }
}
