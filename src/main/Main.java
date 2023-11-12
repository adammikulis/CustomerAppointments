package main;

import helper.ConnectionManager;
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
        primaryStage.setTitle("Client Database");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        ConnectionManager.openConnection();
        launch(args);
        ConnectionManager.closeConnection();
    }
}