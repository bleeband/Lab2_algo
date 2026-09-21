package org.example.spotifylab.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;
import javafx.scene.control.Alert;
import org.example.spotifylab.service.GestionChansonService;

import java.io.IOException;

public class FormulaireChansonController {

    @FXML private TextField champTitre;
    @FXML private TextField champArtiste;
    @FXML private TextField champAlbum;
    @FXML private TextField champAnnee;
    @FXML private ComboBox<Genre> comboGenre;
    @FXML private TextField champDuree;
    @FXML private TextField champEcoutes;
    @FXML private Button boutonAnnuler;
    @FXML private Button boutonEnregistrer;

    private GestionChansonService gestionChansonService;
    private Chanson chansonOriginale;
    private boolean enregistrementEffectue = false;

    @FXML
    private void initialize() {
        comboGenre.getItems().setAll(Genre.values());

        boutonAnnuler.setOnAction(event -> fermer());

        boutonEnregistrer.setOnAction(event -> enregistrer());
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

    public void configurer(
            GestionChansonService gestionChansonService,
            Chanson chanson) {

        this.gestionChansonService = gestionChansonService;
        this.chansonOriginale = chanson;
        remplirFormulaire(chanson);
    }

    private int lireEntier(TextField champ, String nomChamp) {
        try {
            return Integer.parseInt(champ.getText().trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    nomChamp + " doit contenir un nombre entier valide."
            );
        }
    }

    private void enregistrer() {
        try {
            int annee = lireEntier(champAnnee, "Année");
            int duree = lireEntier(champDuree, "Durée");
            int ecoutes = lireEntier(champEcoutes, "Nombre d'écoutes");

            long id = chansonOriginale == null ? 0 : chansonOriginale.getId();

            Chanson chanson = new Chanson(
                    id,
                    champTitre.getText().trim(),
                    champArtiste.getText().trim(),
                    champAlbum.getText().trim(),
                    annee,
                    comboGenre.getValue(),
                    duree,
                    ecoutes
            );

            if (chansonOriginale == null) {
                gestionChansonService.ajouter(chanson);
            } else {
                boolean modifiee = gestionChansonService.modifier(chanson);

                if (!modifiee) {
                    afficherErreur(
                            "Modification impossible",
                            "Cette chanson n'existe plus dans la base de donnée."
                    );
                    return;
                }
            }

            enregistrementEffectue = true;
            fermer();
        } catch (IllegalArgumentException exception) {
            afficherErreur("Saisie invalide", exception.getMessage());
        } catch (IOException exception) {
            afficherErreur("Enregistrement impossible", "Impossible d'enregistrer la chanson. "
            );
        }
    }

    private void afficherErreur(String titre, String message) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.initOwner(boutonEnregistrer.getScene().getWindow());
        alerte.setHeaderText(titre);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    public boolean enregistrementReussi() {
        return enregistrementEffectue;
    }

}