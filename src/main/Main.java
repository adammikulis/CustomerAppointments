package main;

import helper.JDBCHelper;
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
        primaryStage.setTitle("First Screen");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {

        JDBCHelper.openConnection();
        /*int rowsAffected = AppointmentQuery.insert_user("adam", "adam");
        if (rowsAffected > 0) {
            System.out.println(rowsAffected);
        }
        else {
            System.out.println("Insert failed");
        }*/

        launch(args);

        JDBCHelper.closeConnection();
    }
}
