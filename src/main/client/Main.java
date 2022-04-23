package client;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    FXMLLoader loader;

    @Override
    public void start(Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Torrent");
        stage.setWidth(600);
        stage.setHeight(400);
        stage.show();
    }

    /**
     * Method used to close all connections when the applications is closing.
     */
    @Override
    public void stop() {
        Functions.closeConnections();
    }
}

