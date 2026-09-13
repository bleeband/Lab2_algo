package org.example.spotifylab.model;

import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {

    private List<Chanson> chansons;
    private List<Playlist> playlists;

    public Bibliotheque(List<Chanson> chansons) {
        this.chansons = new ArrayList<>(chansons);
        this.playlists = new ArrayList<>();
    }

    public boolean supprimerPlaylist(Playlist playlist) {
        return playlists.remove(playlist);
    }

    public boolean ajouterPlaylist(Playlist playlist) {
        if (playlist == null || playlists.stream().anyMatch(p -> p.getNom().equalsIgnoreCase(playlist.getNom()))) {
            return false;
        }
        return playlists.add(playlist);
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }
}
