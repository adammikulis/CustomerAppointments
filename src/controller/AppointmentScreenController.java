package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class AppointmentScreenController implements Initializable {

    @FXML
    private TableView AppointmentTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointment Screen initialized");

    }

}
