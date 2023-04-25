package main;

import helper.DriverManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/view/ClientScreen.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentScreen.fxml"));
        primaryStage.setTitle("Client Database");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        DriverManager.openConnection();
        launch(args);
        DriverManager.closeConnection();
    }
}