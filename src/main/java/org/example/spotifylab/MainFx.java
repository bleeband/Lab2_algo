package org.example.spotifylab;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainFx extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root); //crée scene contenant l'interface

        primaryStage.setScene(scene); //l'attache à la fenetre

        primaryStage.show(); //affiche la fenetre


    }
}
