package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;

import java.text.Normalizer;
import java.util.List;

public class ChansonService {
    public List<Chanson> rechercher(List<Chanson> chansons, String texte) {
        if (texte == null || texte.isBlank()) {
            return chansons;
        }
        String recherche = normaliser(texte);
        return chansons.stream()
                .filter(chanson ->
                        normaliser(chanson.getTitre()).contains(recherche)
                                || normaliser(chanson.getArtiste()).contains(recherche)
                )
                .toList();
    }

    public List<Chanson> filtrer(
            List<Chanson> chansons,
            Genre genre,
            //integer au lieu de int pour accepter Null
            Integer decennie,
            String artiste,
            Integer dureeMax,
            Integer ecoutesMin
    ) {
        List<Chanson> resultat = chansons;
        // par genre
        if (genre != null) {
            resultat = resultat.stream()
                    .filter(chanson -> chanson.getGenre() == genre)
                    .toList();
        }
        // si decenie 1990, accepte de 90 a 99
        if (decennie != null) {
            resultat = resultat.stream()
                    .filter(chanson ->
                            chanson.getAnnee() >= decennie
                                    && chanson.getAnnee() < decennie + 10
                    )
                    .toList();
        }
        // par artiste
        if (artiste != null && !artiste.isBlank()) {
            resultat = resultat.stream()
                    .filter(chanson ->
                            chanson.getArtiste().equalsIgnoreCase(artiste)
                    )
                    .toList();
        }
        //dure max
        if (dureeMax != null) {
            resultat = resultat.stream()
                    .filter(chanson ->
                            chanson.getDureeSec() <= dureeMax
                    )
                    .toList();
        }
        //nbr ecoute min
        if (ecoutesMin != null) {
            resultat = resultat.stream()
                    .filter(chanson ->
                            chanson.getEcoutes() >= ecoutesMin
                    )
                    .toList();
        }
        return resultat;
    }

    // normalizer separe les accent des lettres et //p{M} remplace les accent separer pour les enlever
    private String normaliser(String texte) {
        return Normalizer.normalize(texte, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase();
    }
}