package org.example.spotifylab.dao;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.service.SourceDonnees;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ChansonDao extends SourceDonnees {

    List<Chanson> trouverTous() throws IOException;

    Optional<Chanson> trouverParId(long id) throws IOException;

    Chanson ajouter(Chanson chanson) throws IOException;

    boolean modifier(Chanson chanson) throws IOException;

    boolean supprimer(long id) throws IOException;

    @Override
    default List<Chanson> chargerChansons() throws IOException {
        return trouverTous();
    }
}
