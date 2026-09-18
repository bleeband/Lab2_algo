package org.example.spotifylab.service;

import org.example.spotifylab.dao.PlaylistDao;
import org.example.spotifylab.dao.PostgreSqlChansonDao;
import org.example.spotifylab.dao.PostgreSqlPlaylistDao;

public final class ConfigurationSourceDonnees {

    private ConfigurationSourceDonnees() {
    }

    public static SourceDonnees creer() {
        return new PostgreSqlChansonDao();
        // Pour revenir au CSV: return new CsvChansonService();
    }

    public static PlaylistDao creerPlaylistDao() {
        return new PostgreSqlPlaylistDao();
    }
}
