package org.example.spotifylab.service;

import org.example.spotifylab.model.Bibliotheque;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Playlist;
import java.util.List;

/** Encapsule toutes les règles de gestion des playlists. */
public final class PlaylistService {
    private final Bibliotheque bibliotheque;
    public PlaylistService(Bibliotheque bibliotheque) { this.bibliotheque = bibliotheque; }
    public List<Playlist> toutes() { return bibliotheque.getPlaylists(); }
    public Playlist creer(String nom) {
        Playlist playlist = new Playlist(nom);
        if (!bibliotheque.ajouterPlaylist(playlist)) throw new IllegalArgumentException("Ce nom existe déjà");
        return playlist;
    }
    public boolean supprimer(Playlist playlist) { return bibliotheque.supprimerPlaylist(playlist); }
    public boolean ajouter(Playlist playlist, Chanson chanson) { return playlist != null && playlist.ajouter(chanson); }
    public boolean retirer(Playlist playlist, Chanson chanson) { return playlist != null && playlist.retirer(chanson); }
    public boolean monter(Playlist playlist, int index) { return playlist != null && playlist.deplacer(index, -1); }
    public boolean descendre(Playlist playlist, int index) { return playlist != null && playlist.deplacer(index, 1); }
    public void vider(Playlist playlist) { if (playlist != null) playlist.vider(); }
}
