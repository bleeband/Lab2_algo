package org.example.spotifylab.service;

import org.example.spotifylab.dao.PlaylistDao;
import org.example.spotifylab.model.Bibliotheque;
import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Playlist;

import java.io.IOException;
import java.util.List;

public final class PlaylistService {
    private final Bibliotheque bibliotheque;
    private final PlaylistDao playlistDao;

    public PlaylistService(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
        this.playlistDao = null;
    }

    public PlaylistService(Bibliotheque bibliotheque, PlaylistDao playlistDao) throws IOException {
        this.bibliotheque = bibliotheque;
        this.playlistDao = playlistDao;
        for (Playlist playlist : playlistDao.trouverToutes(bibliotheque.getChansons())) {
            bibliotheque.ajouterPlaylist(playlist);
        }
    }

    public List<Playlist> toutes() {
        return bibliotheque.getPlaylists();
    }

    public Playlist creer(String nom) {
        Playlist playlist = new Playlist(nom);
        if (!bibliotheque.ajouterPlaylist(playlist)) {
            throw new IllegalArgumentException("Ce nom existe deja");
        }
        if (playlistDao != null) {
            try {
                playlistDao.ajouter(playlist);
            } catch (IOException exception) {
                bibliotheque.supprimerPlaylist(playlist);
                throw new IllegalStateException(exception.getMessage(), exception);
            }
        }
        return playlist;
    }

    public boolean supprimer(Playlist playlist) {
        if (playlist == null) {
            return false;
        }

        boolean supprimee = bibliotheque.supprimerPlaylist(playlist);
        if (supprimee && playlistDao != null) {
            executer(() -> playlistDao.supprimer(playlist.getId()));
        }
        return supprimee;
    }

    public boolean ajouter(Playlist playlist, Chanson chanson) {
        boolean ajoutee = playlist != null && playlist.ajouter(chanson);
        if (ajoutee) {
            enregistrer(playlist);
        }
        return ajoutee;
    }

    public boolean retirer(Playlist playlist, Chanson chanson) {
        boolean retiree = playlist != null && playlist.retirer(chanson);
        if (retiree) {
            enregistrer(playlist);
        }
        return retiree;
    }

    public boolean monter(Playlist playlist, int index) {
        boolean deplacee = playlist != null && playlist.deplacer(index, -1);
        if (deplacee) {
            enregistrer(playlist);
        }
        return deplacee;
    }

    public boolean descendre(Playlist playlist, int index) {
        boolean deplacee = playlist != null && playlist.deplacer(index, 1);
        if (deplacee) {
            enregistrer(playlist);
        }
        return deplacee;
    }

    public void vider(Playlist playlist) {
        if (playlist != null) {
            playlist.vider();
            enregistrer(playlist);
        }
    }

    private void enregistrer(Playlist playlist) {
        if (playlistDao != null) {
            executer(() -> {
                playlistDao.enregistrerChansons(playlist);
                return true;
            });
        }
    }

    private void executer(OperationDao operation) {
        try {
            operation.executer();
        } catch (IOException exception) {
            throw new IllegalStateException(exception.getMessage(), exception);
        }
    }

    private interface OperationDao {
        boolean executer() throws IOException;
    }
}
