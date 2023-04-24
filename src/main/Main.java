package main;

import helper.ConnectionHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ClientList;

import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
        primaryStage.setTitle("Client Database");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        ConnectionHelper.openConnection();
        launch(args);
        ConnectionHelper.closeConnection();
    }
}