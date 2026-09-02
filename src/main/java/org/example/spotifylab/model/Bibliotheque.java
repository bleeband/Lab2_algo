package org.example.spotifylab.model;

import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {

    private List<Chanson> chansons;
    private List<Playlist> playlists;

    public Bibliotheque() {
        chansons = new ArrayList<>();
        playlists = new ArrayList<>();
    }

    public void ajouterChanson(Chanson chanson) {
        chansons.add(chanson);
    }

    public void ajouterChansons(List<Chanson> nouvellesChansons) {
        chansons.addAll(nouvellesChansons);
    }

    public void creerPlaylist(String nom) {
        Playlist playlist = new Playlist(nom);
        playlists.add(playlist);
    }

    public void supprimerPlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }
}