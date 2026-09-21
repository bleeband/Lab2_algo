package org.example.spotifylab.service;

import org.example.spotifylab.dao.PlaylistDao;
import org.example.spotifylab.dao.PostgreSqlChansonDao;
import org.example.spotifylab.dao.PostgreSqlPlaylistDao;
import org.example.spotifylab.dao.ChansonDao;

public final class ConfigurationSourceDonnees {

    private ConfigurationSourceDonnees() {
    }

    public static SourceDonnees creer() {
        return new PostgreSqlChansonDao();
        // Pour revenir au CSV: return new CsvChansonService();
//        return new CsvChansonService();
    }

    public static GestionChansonService creerGestionChansonService(SourceDonnees sourceDonnees) {

        if (sourceDonnees instanceof ChansonDao chansonDao) {
            return new GestionChansonService(chansonDao);
        }
        return null;
    }

    public static PlaylistDao creerPlaylistDao() {
        return new PostgreSqlPlaylistDao();
    }
}
