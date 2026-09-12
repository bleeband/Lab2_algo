package org.example.spotifylab.controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.spotifylab.model.Chanson;
import javafx.scene.control.TableColumn;
import org.example.spotifylab.service.CsvChansonService;

import java.io.IOException;
import java.util.List;
import javafx.scene.control.Alert;

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
            List<Chanson> chansons = csvChansonService.chargerChansons();
            tableChansons.getItems().setAll(chansons); // affiche les chansons
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

    private final CsvChansonService csvChansonService = new CsvChansonService();


}
