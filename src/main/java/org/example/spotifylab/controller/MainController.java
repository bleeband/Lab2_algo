package org.example.spotifylab.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.spotifylab.algorithmes.AlgorithmeTri;
import org.example.spotifylab.algorithmes.TriFusion;
import org.example.spotifylab.algorithmes.TriInsertion;
import org.example.spotifylab.algorithmes.TriRapide;
import org.example.spotifylab.model.Bibliotheque;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;
import org.example.spotifylab.model.Playlist;
import org.example.spotifylab.service.ChansonService;
import org.example.spotifylab.service.ConfigurationSourceDonnees;
import org.example.spotifylab.service.PaginationService;
import org.example.spotifylab.service.PlaylistService;
import org.example.spotifylab.service.SourceDonnees;
import org.example.spotifylab.util.ConvertisseursFiltres;
import org.example.spotifylab.util.FormateurDuree;
import org.example.spotifylab.service.GestionChansonService;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class MainController {
    @FXML private TextField champRecherche;
    @FXML private TableView<Chanson> tableChansons;
    @FXML private TableColumn<Chanson, String> colonneTitre;
    @FXML private TableColumn<Chanson, String> colonneArtiste;
    @FXML private TableColumn<Chanson, Integer> colonneAnnee;
    @FXML private TableColumn<Chanson, String> colonneDuree;
    @FXML private TableColumn<Chanson, Integer> colonneEcoutes;
    @FXML private Button boutonPagePrecedente;
    @FXML private Button boutonPageSuivante;
    @FXML private Label labelPage;
    @FXML private Label labelDureeMax;
    @FXML private Label detailTitre;
    @FXML private Label detailArtiste;
    @FXML private Label detailAlbum;
    @FXML private Label detailGenre;
    @FXML private Label detailAnnee;
    @FXML private Label detailDuree;
    @FXML private Label detailEcoutes;
    @FXML private Label lecteurTitre;
    @FXML private Label lecteurArtiste;
    @FXML private Label lecteurTempsEcoule;
    @FXML private Label lecteurDureeTotale;
    @FXML private Slider sliderProgression;
    @FXML private Button boutonPrecedent;
    @FXML private Button boutonPlay;
    @FXML private Button boutonPause;
    @FXML private Button boutonSuivant;
    @FXML private Button boutonShuffle;
    @FXML private ComboBox<Genre> comboGenre;
    @FXML private ComboBox<Integer> comboDecennie;
    @FXML private ComboBox<String> comboArtiste;
    @FXML private Slider sliderDuree;
    @FXML private ListView<Playlist> listePlaylists;
    @FXML private ListView<Chanson> listeChansonsPlaylist;
    @FXML private ComboBox<Playlist> comboAjoutPlaylist;
    @FXML private Label labelDureePlaylist;
    @FXML private Button boutonNouvellePlaylist;
    @FXML private Button boutonSupprimerPlaylist;
    @FXML private Button boutonAjouter;
    @FXML private Button boutonRetirerPlaylist;
    @FXML private Button boutonMonterPlaylist;
    @FXML private Button boutonDescendrePlaylist;
    @FXML private Button boutonBenchmark;
    @FXML private Button boutonModifierChanson;
    @FXML private Button boutonSupprimerChanson;
    @FXML private Button boutonAjouterChanson;

    private final SourceDonnees sourceDonnees = ConfigurationSourceDonnees.creer();
    private final GestionChansonService gestionChansonService = ConfigurationSourceDonnees.creerGestionChansonService(sourceDonnees);
    private final PaginationService paginationService = new PaginationService();
    private final ChansonService chansonService = new ChansonService();
    private final AlgorithmeTri<Chanson> algorithmeTri = new TriFusion<>();
    private final List<AlgorithmeTri<Chanson>> algorithmesBenchmark =
            List.of(new TriInsertion<>(), new TriFusion<>(), new TriRapide<>());
    private List<Chanson> chansons = List.of();
    private List<Chanson> chansonsFiltrees = List.of();
    private int pageCourante = 0;
    private int taillePage = 25;

    @FXML
    private void ajouterChanson() {
        ouvrirFormulaireChanson(null);
    }

    @FXML
    private void modifierChanson() {
        Chanson selection =
                tableChansons.getSelectionModel().getSelectedItem();

        if (selection == null) {
            return;
        }

        ouvrirFormulaireChanson(selection);
    }

    @FXML
    private void supprimerChanson() {

    }

    @FXML
    private void initialize() {
        configurerTable();
        configurerFiltres();
        configurerEvenements();
        configurerLecteur();
        chargerChansons();
    }

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

    private void configurerTable() {
        colonneTitre.setCellValueFactory(cellule -> new SimpleStringProperty(cellule.getValue().getTitre()));
        colonneArtiste.setCellValueFactory(cellule -> new SimpleStringProperty(cellule.getValue().getArtiste()));
        colonneAnnee.setCellValueFactory(cellule -> new SimpleIntegerProperty(cellule.getValue().getAnnee()).asObject());
        colonneDuree.setCellValueFactory(cellule ->
                new SimpleStringProperty(FormateurDuree.formater(cellule.getValue().getDureeSec())));
        colonneEcoutes.setCellValueFactory(cellule ->
                new SimpleIntegerProperty(cellule.getValue().getEcoutes()).asObject());
        tableChansons.setSortPolicy(table -> {
            Comparator<Chanson> comparateur = table.getComparator();
            if (comparateur != null) appliquerTri(comparateur);
            else appliquerFiltres();
            return true;
        });
    }

    private void configurerFiltres() {
        comboGenre.getItems().setAll(Genre.values());
        comboGenre.getItems().add(0, null);
        comboArtiste.getItems().add(0, null);
        comboDecennie.getItems().add(0, null);
        comboGenre.setConverter(ConvertisseursFiltres.pourGenre());
        comboArtiste.setConverter(ConvertisseursFiltres.pourArtiste());
        comboDecennie.setConverter(ConvertisseursFiltres.pourDecennie());
        comboGenre.setPromptText("Genre");
        comboArtiste.setPromptText("Artiste");
        comboDecennie.setPromptText("Decennie");
        sliderDuree.setBlockIncrement(20);
        sliderDuree.setMin(0);
        sliderDuree.setMax(600);
        sliderDuree.setValue(600);
        actualiserLabelDureeMax();
    }

    private void configurerEvenements() {
        boutonBenchmark.setOnAction(event -> ouvrirBenchmark());
        tableChansons.getSelectionModel().selectedItemProperty()
                .addListener((observable, ancienne, nouvelle) -> afficherDetails(nouvelle));
        champRecherche.textProperty().addListener((observable, ancien, nouveau) -> appliquerFiltres());
        comboGenre.valueProperty().addListener((observable, ancien, nouveau) -> appliquerFiltres());
        comboArtiste.valueProperty().addListener((observable, ancien, nouveau) -> appliquerFiltres());
        comboDecennie.valueProperty().addListener((observable, ancien, nouveau) -> appliquerFiltres());
        sliderDuree.valueProperty().addListener((observable, ancien, nouveau) -> {
            actualiserLabelDureeMax();
            appliquerFiltres();
        });
        afficherDetails(null);


        if (gestionChansonService == null) {
            boutonAjouterChanson.setDisable(true);
            boutonModifierChanson.setDisable(true);
            boutonSupprimerChanson.setDisable(true);
        } else {
            boutonModifierChanson.disableProperty().bind(
                    tableChansons.getSelectionModel().selectedItemProperty().isNull()
            );
            boutonSupprimerChanson.disableProperty().bind(
                    tableChansons.getSelectionModel().selectedItemProperty().isNull()
            );
        }
    }

    private void configurerLecteur() {
        new LecteurController(
                lecteurTitre, lecteurArtiste, lecteurTempsEcoule, lecteurDureeTotale, sliderProgression,
                boutonPrecedent, boutonPlay, boutonPause, boutonSuivant, boutonShuffle,
                () -> chansonsFiltrees,
                () -> tableChansons.getSelectionModel().getSelectedItem(),
                this::afficherDetails,
                tableChansons::refresh,
                () -> afficherErreur("Selection requise", "Choisis une chanson a lire."));
    }

    private void chargerChansons() {
        try {
            chansons = sourceDonnees.chargerChansons();
            chansonsFiltrees = chansons;
            remplirFiltresDepuisChansons();

            Bibliotheque bibliotheque = new Bibliotheque(chansons);
            PlaylistService playlistService;

            if (gestionChansonService == null) {
                playlistService = new PlaylistService(bibliotheque);
            } else {
                playlistService = new PlaylistService(
                        bibliotheque,
                        ConfigurationSourceDonnees.creerPlaylistDao()
                );
            }
            new PlaylistController(
                    listePlaylists, listeChansonsPlaylist, comboAjoutPlaylist, labelDureePlaylist,
                    boutonNouvellePlaylist, boutonSupprimerPlaylist, boutonAjouter,
                    boutonRetirerPlaylist, boutonMonterPlaylist, boutonDescendrePlaylist,
                    playlistService, () -> tableChansons.getSelectionModel().getSelectedItem(), this::afficherErreur);
            afficherPage();
        } catch (IOException exception) {
            afficherErreur("Impossible de charger les chansons", exception.getMessage());
        }
    }

    private void remplirFiltresDepuisChansons() {
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
    }

    private void afficherPage() {
        tableChansons.getItems().setAll(
                paginationService.obtenirPage(chansonsFiltrees, pageCourante, taillePage));
        int nombrePages = paginationService.calculerNombrePages(chansonsFiltrees.size(), taillePage);
        labelPage.setText(nombrePages == 0 ? "Page 0 / 0" : "Page " + (pageCourante + 1) + " / " + nombrePages);
        boutonPagePrecedente.setDisable(pageCourante == 0);
        boutonPageSuivante.setDisable(nombrePages == 0 || pageCourante >= nombrePages - 1);
    }

    private void appliquerFiltres() {
        Integer dureeMax = sliderDuree.getValue() >= sliderDuree.getMax()
                ? null
                : (int) Math.round(sliderDuree.getValue());
        chansonsFiltrees = chansonService.rechercher(chansons, champRecherche.getText());
        chansonsFiltrees = chansonService.filtrer(
                chansonsFiltrees, comboGenre.getValue(), comboDecennie.getValue(),
                comboArtiste.getValue(), dureeMax, null);

        Comparator<Chanson> comparateur = tableChansons.getComparator();
        if (comparateur != null) {
            appliquerTri(comparateur);
        } else {
            pageCourante = 0;
            afficherPage();
        }
    }

    private void actualiserLabelDureeMax() {
        if (sliderDuree.getValue() >= sliderDuree.getMax()) labelDureeMax.setText("Toutes");
        else labelDureeMax.setText(FormateurDuree.formater((int) Math.round(sliderDuree.getValue())));
    }

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
            return;
        }

        lecteurTitre.setText(chanson.getTitre());
        lecteurArtiste.setText(chanson.getArtiste());
        detailTitre.setText(chanson.getTitre());
        detailArtiste.setText(chanson.getArtiste());
        detailAlbum.setText(chanson.getAlbum());
        detailGenre.setText(String.valueOf(chanson.getGenre()));
        detailAnnee.setText(String.valueOf(chanson.getAnnee()));
        detailDuree.setText("duree : " + FormateurDuree.formater(chanson.getDureeSec()));
        detailEcoutes.setText("ecoutes : " + chanson.getEcoutes());
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

    private void appliquerTri(Comparator<Chanson> comparateur) {
        chansonsFiltrees = algorithmeTri.trier(chansonsFiltrees, comparateur);
        pageCourante = 0;
        afficherPage();
    }

    private void ouvrirFormulaireChanson(Chanson chanson) {
        if (gestionChansonService == null) {
            return;
        }

        try {
            FXMLLoader chargeur = new FXMLLoader(
                    getClass().getResource(
                            "/org/example/spotifylab/fxml/formulaire-chanson.fxml"
                    )
            );

            Parent racine = chargeur.load();

            FormulaireChansonController controleur = chargeur.getController();
            controleur.remplirFormulaire(chanson);

            Stage fenetre = new Stage();
            fenetre.initOwner(tableChansons.getScene().getWindow());
            fenetre.initModality(Modality.WINDOW_MODAL);
            fenetre.setTitle(
                    chanson == null ? "Ajouter une chanson" : "Modifier une chanson"
            );
            fenetre.setScene(new Scene(racine));
            fenetre.showAndWait();

        } catch (IOException exception) {
            afficherErreur(
                    "Ouverture impossible",
                    "Impossible d’ouvrir le formulaire de chanson."
            );
        }
    }
}
