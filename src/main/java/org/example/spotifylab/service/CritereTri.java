package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;


import java.util.Comparator;


/** Critères de tri proposés à l'utilisateur. */
public enum CritereTri {
    ID("Identifiant croissant", Comparator.comparingLong(Chanson::getId)),
    TITRE("Titre A-Z", Comparator.comparing(Chanson::getTitre, String.CASE_INSENSITIVE_ORDER)),
    ARTISTE("Artiste A-Z", Comparator.comparing(Chanson::getArtiste, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(Chanson::getTitre, String.CASE_INSENSITIVE_ORDER)),
    ALBUM("Album A-Z", Comparator.comparing(Chanson::getAlbum, String.CASE_INSENSITIVE_ORDER)),
    DUREE("Durée croissante", Comparator.comparingInt(Chanson::getDureeSec)),
    ANNEE_RECENTE("Année récente", Comparator.comparingInt(Chanson::getAnnee).reversed()),
    ECOUTES("Plus écoutées", Comparator.comparingInt(Chanson::getEcoutes).reversed()),
    GENRE("Genre A-Z", Comparator.comparing(chanson -> chanson.getGenre().name(),
            String.CASE_INSENSITIVE_ORDER));

    private final String libelle;

    private final Comparator<Chanson> comparateur;

    CritereTri(String libelle, Comparator<Chanson> comparateur) {
        this.libelle = libelle; this.comparateur = comparateur;
    }

    public Comparator<Chanson> getComparateur() { return comparateur; }

    @Override public String toString() { return libelle; }


}
