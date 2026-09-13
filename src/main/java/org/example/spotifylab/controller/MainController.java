package org.example.spotifylab.controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.spotifylab.model.Bibliotheque;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;
import org.example.spotifylab.model.Playlist;
import org.example.spotifylab.service.CsvChansonService;

import java.io.IOException;
import java.util.List;

import org.example.spotifylab.service.PaginationService;
import org.example.spotifylab.service.ChansonService;
import org.example.spotifylab.service.LecteurService;
import org.example.spotifylab.service.PlaylistService;

import org.example.spotifylab.util.ConvertisseursFiltres;
import org.example.spotifylab.util.FormateurDuree;

import org.example.spotifylab.algorithmes.AlgorithmeTri;
import org.example.spotifylab.algorithmes.TriFusion;
import org.example.spotifylab.algorithmes.TriInsertion;
import org.example.spotifylab.algorithmes.TriRapide;

import java.util.Comparator;

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
                new SimpleStringProperty(FormateurDuree.formater(cellule.getValue().getDureeSec())));
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
        actualiserLabelDureeMax();
        boutonNouvellePlaylist.setOnAction(event -> creerPlaylist());
        boutonSupprimerPlaylist.setOnAction(event -> supprimerPlaylist());
        boutonAjouter.setOnAction(event -> ajouterSelectionAPlaylist());
        boutonBenchmark.setOnAction(event -> ouvrirBenchmark());
        configurerLecteur();

        tableChansons.getSelectionModel().selectedItemProperty().addListener(
                (observable, ancienneChanson, nouvelleChanson) ->
                        afficherDetails(nouvelleChanson));
        afficherDetails(null);

        tableChansons.setSortPolicy(table -> {
            Comparator<Chanson> comparateur = table.getComparator();

            if (comparateur != null) {
                appliquerTri(comparateur);
            } else {
                appliquerFiltres();
            }

            return true;
        });

        champRecherche.textProperty().addListener(
                (observable, ancienTexte, nouveauTexte) -> appliquerFiltres());

        comboGenre.valueProperty().addListener(
                (observable, ancienGenre, nouveauGenre) -> appliquerFiltres());

        comboArtiste.valueProperty().addListener(
                (observable, ancienArtiste, nouvelArtiste) -> appliquerFiltres());

        comboDecennie.valueProperty().addListener(
                (observable, ancienneDecenie, nouvelleDecennie) -> appliquerFiltres());

        sliderDuree.valueProperty().addListener((observable, ancienneDuree, nouvelleDuree) -> {
            actualiserLabelDureeMax();
            appliquerFiltres();
        });


        try {
            chansons = csvChansonService.chargerChansons();
            playlistService = new PlaylistService(new Bibliotheque(chansons));
            chansonsFiltrees = chansons;
            chansons.stream()
                    .map(Chanson::getArtiste)
                    .distinct()
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .forEach(comboArtiste.getItems()::add);

            chansons.stream()
                    .map(chanson -> (chanson.getAnnee() / 10) * 10)
                    .distinct()
                    .sorted()
                    .forEach(comboDecennie.getItems()::add);
            actualiserPlaylists(null);
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
    private TableColumn<Chanson, String> colonneDuree;

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
    private PlaylistService playlistService;
    private final LecteurService lecteurService = new LecteurService();
    private Timeline minuterieLecture;

    private final AlgorithmeTri<Chanson> algorithmeTri = new TriFusion<>();
    private final List<AlgorithmeTri<Chanson>> algorithmesBenchmark = List.of(
            new TriInsertion<>(), new TriFusion<>(), new TriRapide<>());

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
        Comparator<Chanson> comparateur = tableChansons.getComparator();

        if (comparateur != null) {
            appliquerTri(comparateur);
        } else {
            pageCourante = 0;
            afficherPage();
        }
    }

    // boutons précedent/suivant + Page 1/1

    @FXML
    private Button boutonPagePrecedente;

    @FXML
    private Button boutonPageSuivante;

    @FXML
    private Label labelPage;

    @FXML
    private Label labelDureeMax;

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

    private void actualiserLabelDureeMax() {
        if (sliderDuree.getValue() >= sliderDuree.getMax()) {
            labelDureeMax.setText("Toutes");
            return;
        }

        labelDureeMax.setText(FormateurDuree.formater((int) Math.round(sliderDuree.getValue())));
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

    @FXML
    private Label lecteurTitre;

    @FXML
    private Label lecteurArtiste;

    @FXML
    private Label lecteurTempsEcoule;

    @FXML
    private Label lecteurDureeTotale;

    @FXML
    private Slider sliderProgression;

    @FXML
    private Button boutonPrecedent;

    @FXML
    private Button boutonPlay;

    @FXML
    private Button boutonPause;

    @FXML
    private Button boutonSuivant;

    @FXML
    private Button boutonShuffle;

    private void afficherDetails(Chanson chanson) {
        if (chanson == null) {
            lecteurTitre.setText("");
            lecteurArtiste.setText("");
            detailTitre.setText("");
            detailArtiste.setText("");
            detailAlbum.setText("");
            detailGenre.setText("");
            detailAnnee.setText("");
            detailDuree.setText("");
            detailEcoutes.setText("");
        }
        else {
            lecteurTitre.setText(chanson.getTitre());
            lecteurArtiste.setText(chanson.getArtiste());
            detailTitre.setText(chanson.getTitre());
            detailArtiste.setText(chanson.getArtiste());
            detailAlbum.setText(chanson.getAlbum());
            detailGenre.setText(String.valueOf(chanson.getGenre()));
            detailAnnee.setText(String.valueOf(chanson.getAnnee()));
            detailDuree.setText("durée : " + FormateurDuree.formater(chanson.getDureeSec()));
            detailEcoutes.setText("écoutes : " + chanson.getEcoutes());
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

    // fonctionnalités tri par algorithme

    @FXML
    private ListView<Playlist> listePlaylists;

    @FXML
    private ComboBox<Playlist> comboAjoutPlaylist;

    @FXML
    private Button boutonNouvellePlaylist;

    @FXML
    private Button boutonSupprimerPlaylist;

    @FXML
    private Button boutonAjouter;

    @FXML
    private Button boutonBenchmark;

    private void creerPlaylist() {
        TextInputDialog dialogue = new TextInputDialog();
        dialogue.setTitle("Nouvelle playlist");
        dialogue.setHeaderText("Creer une playlist");
        dialogue.setContentText("Nom :");

        dialogue.showAndWait().ifPresent(nom -> {
            try {
                Playlist playlist = playlistService.creer(nom);
                actualiserPlaylists(playlist);
            } catch (IllegalArgumentException exception) {
                afficherErreur("Playlist invalide", exception.getMessage());
            }
        });
    }

    private void supprimerPlaylist() {
        Playlist playlist = listePlaylists.getSelectionModel().getSelectedItem();

        if (playlist == null) {
            playlist = comboAjoutPlaylist.getValue();
        }

        if (playlist != null) {
            playlistService.supprimer(playlist);
            actualiserPlaylists(null);
        }
    }

    private void ajouterSelectionAPlaylist() {
        Chanson chanson = tableChansons.getSelectionModel().getSelectedItem();
        Playlist playlist = comboAjoutPlaylist.getValue();

        if (playlist == null) {
            playlist = listePlaylists.getSelectionModel().getSelectedItem();
        }

        if (chanson == null || playlist == null) {
            afficherErreur("Selection requise", "Choisis une chanson et une playlist.");
            return;
        }

        if (!playlistService.ajouter(playlist, chanson)) {
            afficherErreur("Ajout impossible", "Cette chanson est deja dans la playlist.");
            return;
        }

        actualiserPlaylists(playlist);
    }

    private void actualiserPlaylists(Playlist playlistSelectionnee) {
        List<Playlist> playlists = playlistService == null ? List.of() : playlistService.toutes();
        listePlaylists.getItems().setAll(playlists);
        comboAjoutPlaylist.getItems().setAll(playlists);

        if (playlistSelectionnee != null) {
            listePlaylists.getSelectionModel().select(playlistSelectionnee);
            comboAjoutPlaylist.setValue(playlistSelectionnee);
        } else {
            comboAjoutPlaylist.setValue(null);
        }
    }

    private void afficherErreur(String titre, String message) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setHeaderText(titre);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    private void ouvrirBenchmark() {
        try {
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/org/example/spotifylab/fxml/benchmark.fxml"));
            Parent racine = chargeur.load();
            chargeur.<BenchmarkController>getController().initialiser(chansons, algorithmesBenchmark);

            Stage fenetre = new Stage();
            fenetre.initModality(Modality.APPLICATION_MODAL);
            fenetre.setTitle("Benchmark des tris");
            fenetre.setScene(new Scene(racine, 560, 360));
            fenetre.showAndWait();
        } catch (IOException exception) {
            afficherErreur("Benchmark impossible", exception.getMessage());
        }
    }

    private void configurerLecteur() {
        sliderProgression.setMin(0);
        sliderProgression.setValue(0);
        lecteurTempsEcoule.setText("0:00");
        lecteurDureeTotale.setText("0:00");

        boutonPlay.setOnAction(event -> lireOuReprendreSelection());
        boutonPause.setOnAction(event -> pauseLecture());
        boutonPrecedent.setOnAction(event -> chansonPrecedente());
        boutonSuivant.setOnAction(event -> chansonSuivante());
        boutonShuffle.setOnAction(event -> melangerLecture());

        minuterieLecture = new Timeline(new KeyFrame(Duration.seconds(1), event -> avancerLecture()));
        minuterieLecture.setCycleCount(Timeline.INDEFINITE);
    }

    private void lireOuReprendreSelection() {
        Chanson selection = tableChansons.getSelectionModel().getSelectedItem();
        Chanson courante = lecteurService.chansonCourante();

        if (selection != null && selection != courante) {
            Chanson chanson = lecteurService.lire(chansonsFiltrees, selection);
            afficherLecture(chanson, true);
            afficherDetails(chanson);
            tableChansons.refresh();
        } else if (courante != null) {
            afficherLecture(courante, false);
            lecteurService.demarrer();
        } else {
            afficherErreur("Selection requise", "Choisis une chanson a lire.");
            return;
        }

        minuterieLecture.play();
    }

    private void pauseLecture() {
        lecteurService.pause();
        minuterieLecture.pause();
    }

    private void chansonPrecedente() {
        Chanson chanson = lecteurService.precedente();
        if (chanson != null) {
            afficherLecture(chanson, true);
            if (lecteurService.estEnLecture()) {
                minuterieLecture.play();
            }
        }
    }

    private void chansonSuivante() {
        Chanson chanson = lecteurService.suivante();
        if (chanson != null) {
            afficherLecture(chanson, true);
            tableChansons.refresh();
            if (lecteurService.estEnLecture()) {
                minuterieLecture.play();
            }
        }
    }

    private void melangerLecture() {
        Chanson chanson = lecteurService.melanger();
        if (chanson != null) {
            afficherLecture(chanson, true);
        }
    }

    private void avancerLecture() {
        Chanson chanson = lecteurService.chansonCourante();
        if (chanson == null || !lecteurService.estEnLecture()) {
            return;
        }

        double prochaineSeconde = sliderProgression.getValue() + 1;
        if (prochaineSeconde >= chanson.getDureeSec()) {
            chansonSuivante();
            return;
        }

        sliderProgression.setValue(prochaineSeconde);
        lecteurTempsEcoule.setText(FormateurDuree.formater((int) prochaineSeconde));
    }

    private void afficherLecture(Chanson chanson, boolean recommencerProgression) {
        lecteurTitre.setText(chanson.getTitre());
        lecteurArtiste.setText(chanson.getArtiste());
        sliderProgression.setMax(chanson.getDureeSec());
        lecteurDureeTotale.setText(FormateurDuree.formater(chanson.getDureeSec()));

        if (recommencerProgression) {
            sliderProgression.setValue(0);
            lecteurTempsEcoule.setText("0:00");
        }
    }

    private void appliquerTri(Comparator<Chanson> comparateur) {
        chansonsFiltrees = algorithmeTri.trier(chansonsFiltrees, comparateur);
        pageCourante = 0;
        afficherPage();
    }
}
