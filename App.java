package com.centroestetico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/centroestetico/ui/MainWindow.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/centroestetico/ui/root.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                getClass().getResource("/com/centroestetico/ui/css/app.css")
                        .toExternalForm());



        stage.setTitle("Centro Estético");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
