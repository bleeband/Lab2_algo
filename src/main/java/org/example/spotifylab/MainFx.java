package org.example.spotifylab;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import java.net.URL;

public class MainFx extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));

        Parent root = loader.load();

        Scene scene = new Scene(root); //crée scene contenant l'interface
        URL icone = getClass().getResource("images/logo.png");
        if (icone != null) {
            primaryStage.getIcons().add(new Image(icone.toExternalForm())); //ajoute une icone à la fenetre
        }
        primaryStage.setTitle("ECM Player"); //titre de la fenetre
        primaryStage.setResizable(false); //empêche le redimensionnement de la fenetre
        primaryStage.centerOnScreen(); //centre la fenetre sur l'ecran
        primaryStage.setHeight(855); //hauteur de la fenetre
        primaryStage.setWidth(1050); //largeur de la fenetre

        primaryStage.setScene(scene); //l'attache à la fenetre

        primaryStage.show(); //affiche la fenetre


    }
}
