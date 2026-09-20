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
        return chansonDao.ajouter(chanson);
    }

    public boolean modifier(Chanson chanson) throws IOException {
        return chansonDao.modifier(chanson);
    }

    public boolean supprimer(long id) throws IOException {
        return chansonDao.supprimer(id);
    }
}