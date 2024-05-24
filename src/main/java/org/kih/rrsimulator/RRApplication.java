package org.kih.rrsimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RRApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RRApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1062, 650);
        scene.getStylesheets().add(Objects.requireNonNull(RRApplication.class.getResource("styles.css")).toString());
        stage.setTitle("RR simulator");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}