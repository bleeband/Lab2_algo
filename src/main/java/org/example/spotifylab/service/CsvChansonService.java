package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvChansonService {
    public List<Chanson> chargerChansons() throws IOException {
        List<Chanson> chansons = new ArrayList<>();
        InputStream fichier = getClass().getResourceAsStream(
                "/org/example/spotifylab/data/chansons.csv"
        );

        if (fichier == null) {
            throw new IOException("Le fichier chansons.csv est introuvable.");
        }

        try (BufferedReader lecteur = new BufferedReader(
                new InputStreamReader(fichier, StandardCharsets.UTF_8))) {
            lecteur.readLine();
            String ligne;

            while ((ligne = lecteur.readLine()) != null) {
                String[] colonnes = ligne.split(";");

                Chanson chanson = new Chanson(
                        Integer.parseInt(colonnes[0]),
                        colonnes[1],
                        colonnes[2],
                        colonnes[3],
                        Integer.parseInt(colonnes[4]),
                        Genre.valueOf(colonnes[5]),
                        Integer.parseInt(colonnes[6]),
                        Integer.parseInt(colonnes[7])
                );

                chansons.add(chanson);
            }
        }

        return chansons;
    }
}