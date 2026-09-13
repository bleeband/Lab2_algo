package org.example.spotifylab.controller;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;
import org.example.spotifylab.service.CsvChansonService;

import java.io.IOException;
import java.util.List;

import org.example.spotifylab.service.PaginationService;
import org.example.spotifylab.service.ChansonService;

import org.example.spotifylab.util.ConvertisseursFiltres;

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

        comboGenre.getItems().setAll(Genre.values());
        comboGenre.getItems().add(0, null);

        comboArtiste.getItems().add(0, null);
        comboDecennie.getItems().add(0, null);

        comboGenre.setConverter(ConvertisseursFiltres.pourGenre());
        comboArtiste.setConverter(ConvertisseursFiltres.pourArtiste());
        comboDecennie.setConverter(ConvertisseursFiltres.pourDecennie());

        comboGenre.setPromptText("Genre");
        comboArtiste.setPromptText("Artiste");
        comboDecennie.setPromptText("Décennie");
        sliderDuree.setBlockIncrement(20);
        sliderDuree.setMin(0);
        sliderDuree.setMax(600);
        sliderDuree.setValue(600);

        tableChansons.getSelectionModel().selectedItemProperty().addListener(
                (observable, ancienneChanson, nouvelleChanson) ->
                        afficherDetails(nouvelleChanson));
        afficherDetails(null);

        champRecherche.textProperty().addListener(
                (observable, ancienTexte, nouveauTexte) -> appliquerFiltres());

        comboGenre.valueProperty().addListener(
                (observable, ancienGenre, nouveauGenre) -> appliquerFiltres());

        comboArtiste.valueProperty().addListener(
                (observable, ancienArtiste, nouvelArtiste) -> appliquerFiltres());

        comboDecennie.valueProperty().addListener(
                (observable, ancienneDecenie, nouvelleDecennie) -> appliquerFiltres());

        sliderDuree.valueProperty().addListener(
                (observable, ancienneDuree, nouvelleDuree) -> appliquerFiltres());


        try {
            chansons = csvChansonService.chargerChansons();
            chansonsFiltrees = chansons;
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
    private List<Chanson> chansonsFiltrees = List.of();

    private int pageCourante = 0;

    private int taillePage = 25;

    private final PaginationService paginationService = new PaginationService();
    private final ChansonService chansonService = new ChansonService();

    private void afficherPage(){
        List<Chanson> page = paginationService.obtenirPage(
                chansonsFiltrees, pageCourante, taillePage);
        tableChansons.getItems().setAll(page);

        int nombrePages = paginationService.calculerNombrePages(
                chansonsFiltrees.size(), taillePage);

        if (nombrePages == 0) {

            labelPage.setText("Page 0 / 0");
        }
        else {
            labelPage.setText("Page " + (pageCourante + 1) + " / " + nombrePages );
        }

        boutonPagePrecedente.setDisable(pageCourante == 0);

        boutonPageSuivante.setDisable(nombrePages == 0 || pageCourante >= nombrePages -1);

    }

    private void appliquerFiltres(){
        Integer dureeMax;

        if (sliderDuree.getValue() >= sliderDuree.getMax()) {
            dureeMax = null;
        }
        else {
            dureeMax = (int) Math.round(sliderDuree.getValue());
        }
        chansonsFiltrees = chansonService.rechercher(chansons, champRecherche.getText());
        chansonsFiltrees = chansonService.filtrer(chansonsFiltrees, comboGenre.getValue(), comboDecennie.getValue(), comboArtiste.getValue(), dureeMax, null);
        pageCourante = 0;
        afficherPage();
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
        int nombrePages = paginationService.calculerNombrePages(chansonsFiltrees.size(), taillePage);
        if (pageCourante < nombrePages - 1) {
            pageCourante++;
            afficherPage();
        }
    }

    // panneau de details

    @FXML
    private Label detailTitre;

    @FXML
    private Label detailArtiste;

    @FXML
    private Label detailAlbum;

    @FXML
    private Label detailGenre;

    @FXML
    private Label detailAnnee;

    @FXML
    private Label detailDuree;

    @FXML
    private Label detailEcoutes;

    private void afficherDetails(Chanson chanson) {
        if (chanson == null) {
            detailTitre.setText("");
            detailArtiste.setText("");
            detailAlbum.setText("");
            detailGenre.setText("");
            detailAnnee.setText("");
            detailDuree.setText("");
            detailEcoutes.setText("");
        }
        else {
            detailTitre.setText(chanson.getTitre());
            detailArtiste.setText(chanson.getArtiste());
            detailAlbum.setText(chanson.getAlbum());
            detailGenre.setText(String.valueOf(chanson.getGenre()));
            detailAnnee.setText(String.valueOf(chanson.getAnnee()));
            detailDuree.setText(String.valueOf(chanson.getDureeSec()));
            detailEcoutes.setText(String.valueOf(chanson.getEcoutes()));
        }
    }

    // fonctionnalités tri

    @FXML
    private ComboBox<Genre> comboGenre;

    @FXML
    private ComboBox<Integer> comboDecennie;

    @FXML
    private ComboBox<String> comboArtiste;

    @FXML
    private Slider sliderDuree;





}
