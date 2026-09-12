package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;

import java.util.List;

public class PaginationService {

    public List<Chanson> obtenirPage(List<Chanson> chansons, int pageCourante, int taillePage) {

        if (pageCourante < 0 || taillePage <= 0) {
            throw new IllegalArgumentException("Page ou taille de page invalide");
        }

        int debut =  pageCourante * taillePage;

        if ( debut >= chansons.size()) {
            return List.of();
        }

        int fin = Math.min(debut + taillePage, chansons.size());

        return chansons.subList(debut, fin);

        }

    public int calculerNombrePages(int nombreChansons, int taillePage) {
        return (nombreChansons + taillePage -1) / taillePage;
    }

    }
