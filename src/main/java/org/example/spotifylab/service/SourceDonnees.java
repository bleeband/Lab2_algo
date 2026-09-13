package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;

import java.io.IOException;
import java.util.List;

public interface SourceDonnees {

    List<Chanson> chargerChansons() throws IOException;
}