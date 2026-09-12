package org.example.spotifylab.controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.spotifylab.model.Chanson;
import javafx.scene.control.TableColumn;

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


}
