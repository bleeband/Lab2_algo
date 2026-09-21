package org.example.spotifylab.service;

import org.example.spotifylab.dao.ChansonDao;
import org.example.spotifylab.model.Chanson;

import java.io.IOException;

public class GestionChansonService {

    private final ChansonDao chansonDao;

    public GestionChansonService(ChansonDao chansonDao) {
        this.chansonDao = chansonDao;
    }

    public Chanson ajouter(Chanson chanson) throws IOException {
        valider(chanson);
        return chansonDao.ajouter(chanson);
    }

    public boolean modifier(Chanson chanson) throws IOException {
        valider(chanson);
        return chansonDao.modifier(chanson);
    }

    public boolean supprimer(long id) throws IOException {
        return chansonDao.supprimer(id);
    }

    private void valider(Chanson chanson) {
        if (chanson == null) {
            throw new IllegalArgumentException("La chanson est obligatoire.");
        }

        if (chanson.getTitre() == null || chanson.getTitre().isBlank()) {
            throw new IllegalArgumentException("Le titre est obligatoire.");
        }

        if (chanson.getTitre().length() > 200) {
            throw new IllegalArgumentException(
                    "Le titre ne doit pas dépasser 200 caractères.");
        }

        if (chanson.getArtiste() == null || chanson.getArtiste().isBlank()) {
            throw new IllegalArgumentException("L’artiste est obligatoire.");
        }

        if (chanson.getArtiste().length() > 150) {
            throw new IllegalArgumentException(
                    "Le nom de l’artiste ne doit pas dépasser 150 caractères.");
        }

        if (chanson.getAlbum() != null && chanson.getAlbum().length() > 200) {
            throw new IllegalArgumentException(
                    "Le nom de l'album ne doit pas dépasser 200 caractères.");
        }

        if (chanson.getAnnee() < 1900 || chanson.getAnnee() > 2100) {
            throw new IllegalArgumentException(
                    "L’année doit être comprise entre 1900 et 2100.");
        }

        if (chanson.getGenre() == null) {
            throw new IllegalArgumentException("Le genre est obligatoire.");
        }

        if (chanson.getDureeSec() <= 0) {
            throw new IllegalArgumentException(
                    "La durée doit être supérieure à zéro.");
        }

        if (chanson.getEcoutes() < 0) {
            throw new IllegalArgumentException(
                    "Le nombre d’écoutes ne peut pas être négatif.");
        }
    }
}