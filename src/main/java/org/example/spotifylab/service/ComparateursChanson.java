package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;

import java.util.Comparator;

public class ComparateursChanson {

    public static final Comparator<Chanson> PAR_TITRE =
            (chanson1, chanson2) ->
                    chanson1.getTitre().compareToIgnoreCase(chanson2.getTitre());

    public static final Comparator<Chanson> PAR_ARTISTE =
            (chanson1, chanson2) ->
                    chanson1.getArtiste().compareToIgnoreCase(chanson2.getArtiste());

    public static final Comparator<Chanson> PAR_DUREE =
            (chanson1, chanson2) ->
                    Integer.compare(chanson1.getDureeSec(), chanson2.getDureeSec());

    public static final Comparator<Chanson> PAR_ANNEE =
            (chanson1, chanson2) ->
                    Integer.compare(chanson2.getAnnee(), chanson1.getAnnee());

    public static final Comparator<Chanson> PAR_ECOUTES =
            (chanson1, chanson2) ->
                    Integer.compare(chanson2.getEcoutes(), chanson1.getEcoutes());

    public static final Comparator<Chanson> PAR_GENRE =
            (chanson1, chanson2) ->
                    chanson1.getGenre().compareTo(chanson2.getGenre());
}