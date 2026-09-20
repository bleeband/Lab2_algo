package org.example.spotifylab.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;

public class FormulaireChansonController {

    @FXML private TextField champTitre;
    @FXML private TextField champArtiste;
    @FXML private TextField champAlbum;
    @FXML private TextField champAnnee;
    @FXML private ComboBox<Genre> comboGenre;
    @FXML private TextField champDuree;
    @FXML private TextField champEcoutes;
    @FXML private Button boutonAnnuler;

    @FXML
    private void initialize() {
        comboGenre.getItems().setAll(Genre.values());

        boutonAnnuler.setOnAction(event -> fermer());
    }

    public void remplirFormulaire(Chanson chanson) {
        if (chanson == null) {
            return;
        }

        champTitre.setText(chanson.getTitre());
        champArtiste.setText(chanson.getArtiste());
        champAlbum.setText(chanson.getAlbum());
        champAnnee.setText(String.valueOf(chanson.getAnnee()));
        comboGenre.setValue(chanson.getGenre());
        champDuree.setText(String.valueOf(chanson.getDureeSec()));
        champEcoutes.setText(String.valueOf(chanson.getEcoutes()));
    }

    private void fermer() {
        Stage fenetre = (Stage) boutonAnnuler.getScene().getWindow();
        fenetre.close();
    }
}