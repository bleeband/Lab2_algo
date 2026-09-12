package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;

import java.util.List;

public class PaginationService {
    public List<Chanson> obtenirPage(List<Chanson> chansons, int pageCourante, int taillePage) {
        int debut =  pageCourante * taillePage;

        int fin = Math.min(debut + taillePage, chansons.size());

        return chansons.subList(debut, fin);

        }

    }