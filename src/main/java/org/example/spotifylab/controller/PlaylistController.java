package org.example.spotifylab.controller;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Playlist;
import org.example.spotifylab.service.PlaylistService;
import org.example.spotifylab.util.FormateurDuree;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

final class PlaylistController {
    private final ListView<Playlist> playlists;
    private final ListView<Chanson> chansonsPlaylist;
    private final ComboBox<Playlist> ajoutPlaylist;
    private final Label dureePlaylist;
    private final PlaylistService service;
    private final Supplier<Chanson> chansonSelectionnee;
    private final BiConsumer<String, String> afficherErreur;

    PlaylistController(ListView<Playlist> playlists,
                       ListView<Chanson> chansonsPlaylist,
                       ComboBox<Playlist> ajoutPlaylist,
                       Label dureePlaylist,
                       Button nouvellePlaylist,
                       Button supprimer,
                       Button ajouter,
                       Button retirer,
                       Button monter,
                       Button descendre,
                       PlaylistService service,
                       Supplier<Chanson> chansonSelectionnee,
                       BiConsumer<String, String> afficherErreur) {
        this.playlists = playlists;
        this.chansonsPlaylist = chansonsPlaylist;
        this.ajoutPlaylist = ajoutPlaylist;
        this.dureePlaylist = dureePlaylist;
        this.service = service;
        this.chansonSelectionnee = chansonSelectionnee;
        this.afficherErreur = afficherErreur;

        playlists.getSelectionModel().selectedItemProperty()
                .addListener((observable, ancienne, playlist) -> afficherContenu(playlist));
        nouvellePlaylist.setOnAction(event -> creerPlaylist());
        supprimer.setOnAction(event -> supprimerPlaylist());
        ajouter.setOnAction(event -> ajouterSelectionAPlaylist());
        retirer.setOnAction(event -> retirerSelectionDePlaylist());
        monter.setOnAction(event -> deplacerSelection(-1));
        descendre.setOnAction(event -> deplacerSelection(1));
        actualiser(null);
    }

    void actualiser(Playlist playlistSelectionnee) {
        List<Playlist> toutes = service.toutes();
        playlists.getItems().setAll(toutes);
        ajoutPlaylist.getItems().setAll(toutes);

        if (playlistSelectionnee != null) {
            playlists.getSelectionModel().select(playlistSelectionnee);
            ajoutPlaylist.setValue(playlistSelectionnee);
        } else {
            ajoutPlaylist.setValue(null);
            afficherContenu(playlists.getSelectionModel().getSelectedItem());
        }
    }

    private void creerPlaylist() {
        TextInputDialog dialogue = new TextInputDialog();
        dialogue.setTitle("Nouvelle playlist");
        dialogue.setHeaderText("Creer une playlist");
        dialogue.setContentText("Nom :");

        dialogue.showAndWait().ifPresent(nom -> {
            try {
                actualiser(service.creer(nom));
            } catch (IllegalArgumentException exception) {
                afficherErreur.accept("Playlist invalide", exception.getMessage());
            }
        });
    }

    private void supprimerPlaylist() {
        Playlist playlist = playlistActive();
        if (playlist != null) {
            service.supprimer(playlist);
            actualiser(null);
        }
    }

    private void ajouterSelectionAPlaylist() {
        Chanson chanson = chansonSelectionnee.get();
        Playlist playlist = playlistActive();

        if (chanson == null || playlist == null) {
            afficherErreur.accept("Selection requise", "Choisis une chanson et une playlist.");
            return;
        }

        if (!service.ajouter(playlist, chanson)) {
            afficherErreur.accept("Ajout impossible", "Cette chanson est deja dans la playlist.");
            return;
        }

        actualiser(playlist);
    }

    private void retirerSelectionDePlaylist() {
        Playlist playlist = playlistActive();
        Chanson chanson = chansonsPlaylist.getSelectionModel().getSelectedItem();
        if (playlist != null && chanson != null && service.retirer(playlist, chanson)) {
            actualiser(playlist);
        }
    }

    private void deplacerSelection(int direction) {
        Playlist playlist = playlistActive();
        int index = chansonsPlaylist.getSelectionModel().getSelectedIndex();
        boolean deplace = direction < 0 ? service.monter(playlist, index) : service.descendre(playlist, index);

        if (deplace) {
            actualiser(playlist);
            chansonsPlaylist.getSelectionModel().select(index + direction);
        }
    }

    private Playlist playlistActive() {
        Playlist playlist = playlists.getSelectionModel().getSelectedItem();
        return playlist != null ? playlist : ajoutPlaylist.getValue();
    }

    private void afficherContenu(Playlist playlist) {
        if (playlist == null) {
            chansonsPlaylist.getItems().clear();
            dureePlaylist.setText("0:00");
            return;
        }

        chansonsPlaylist.getItems().setAll(playlist.getChansons());
        dureePlaylist.setText(FormateurDuree.formater(playlist.getDureeTotaleSec()));
    }
}
