package org.example.spotifylab.dao;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Playlist;

import java.io.IOException;
import java.util.List;

public interface PlaylistDao {

    List<Playlist> trouverToutes(List<Chanson> chansonsDisponibles) throws IOException;

    Playlist ajouter(Playlist playlist) throws IOException;

    boolean supprimer(long id) throws IOException;

    void enregistrerChansons(Playlist playlist) throws IOException;
}
