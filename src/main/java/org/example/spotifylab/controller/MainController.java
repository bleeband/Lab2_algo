package org.example.spotifylab.controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.service.CsvChansonService;

import java.io.IOException;
import java.util.List;

import org.example.spotifylab.service.PaginationService;

public class MainController {

    @FXML
    private void initialize() {
        colonneTitre.setCellValueFactory(cellule ->
                new SimpleStringProperty(cellule.getValue().getTitre()));
        colonneArtiste.setCellValueFactory(cellule ->
                new SimpleStringProperty(cellule.getValue().getArtiste()));
        colonneAnnee.setCellValueFactory(cellule ->
                new SimpleIntegerProperty(cellule.getValue().getAnnee()).asObject());
        colonneDuree.setCellValueFactory(cellule ->
                new SimpleIntegerProperty(cellule.getValue().getDureeSec()).asObject());
        colonneEcoutes.setCellValueFactory(cellule ->
                new SimpleIntegerProperty(cellule.getValue().getEcoutes()).asObject());

        try {
            chansons = csvChansonService.chargerChansons();
            afficherPage(); // affiche les chansons
        } catch (IOException e) {
            Alert alerte = new Alert(Alert.AlertType.ERROR);
            alerte.setHeaderText("Impossible de charger les chansons");
            alerte.setContentText(e.getMessage());
            alerte.showAndWait();

        }

    }

    @FXML
    private TextField champRecherche;

    @FXML
    private TableView<Chanson> tableChansons;

    @FXML
    private TableColumn<Chanson, String> colonneTitre;

    @FXML
    private TableColumn<Chanson, String> colonneArtiste;

    @FXML
    private TableColumn<Chanson, Integer> colonneAnnee;

    @FXML
    private TableColumn<Chanson, Integer> colonneDuree;

    @FXML
    private TableColumn<Chanson, Integer> colonneEcoutes;

    // affichage chansons

    private final CsvChansonService csvChansonService = new CsvChansonService();

    private List<Chanson> chansons = List.of();

    private int pageCourante = 0;

    private int taillePage = 25;

    private final PaginationService paginationService = new PaginationService();

    private void afficherPage(){
        List<Chanson> page = paginationService.obtenirPage(
                chansons, pageCourante, taillePage);
        tableChansons.getItems().setAll(page);

        int nombrePages = paginationService.calculerNombrePages(
                chansons.size(), taillePage);

        if (nombrePages == 0) {

            labelPage.setText("Page 0 / 0");
        }
        else {
            labelPage.setText("Page " + (pageCourante + 1) + " / " + nombrePages );
        }

        boutonPagePrecedente.setDisable(pageCourante == 0);

        boutonPageSuivante.setDisable(nombrePages == 0 || pageCourante >= nombrePages -1);

    }

    // boutons précedent/suivant + Page 1/1

    @FXML
    private Button boutonPagePrecedente;

    @FXML
    private Button boutonPageSuivante;

    @FXML
    private Label labelPage;

    @FXML
    private void pagePrecedente() {
        if (pageCourante > 0) {
            pageCourante--;
            afficherPage();
        }
    }

    @FXML
    private void pageSuivante() {
        int nombrePages = paginationService.calculerNombrePages(chansons.size(), taillePage);
        if (pageCourante < nombrePages - 1) {
            pageCourante++;
            afficherPage();
        }
    }




}
