package org.example.spotifylab.dao;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Playlist;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostgreSqlPlaylistDao implements PlaylistDao {

    @Override
    public List<Playlist> trouverToutes(List<Chanson> chansonsDisponibles) throws IOException {
        String sql = """
                SELECT p.id_playlist, p.nom, p.date_creation,
                       pc.id_chanson
                FROM playlist p
                LEFT JOIN playlist_chanson pc ON pc.id_playlist = p.id_playlist
                ORDER BY p.id_playlist, pc.position
                """;

        Map<Long, Chanson> chansonsParId = chansonsDisponibles.stream()
                .collect(Collectors.toMap(Chanson::getId, chanson -> chanson));
        Map<Long, Playlist> playlists = new LinkedHashMap<>();

        try (Connection connexion = ouvrirConnexion();
             PreparedStatement requete = connexion.prepareStatement(sql);
             ResultSet resultats = requete.executeQuery()) {
            while (resultats.next()) {
                long idPlaylist = resultats.getLong("id_playlist");
                Playlist playlist = playlists.get(idPlaylist);
                if (playlist == null) {
                    playlist = new Playlist(
                            idPlaylist,
                            resultats.getString("nom"),
                            resultats.getDate("date_creation").toLocalDate()
                    );
                    playlists.put(idPlaylist, playlist);
                }

                long idChanson = resultats.getLong("id_chanson");
                if (!resultats.wasNull()) {
                    Optional.ofNullable(chansonsParId.get(idChanson)).ifPresent(playlist::ajouter);
                }
            }
            return new ArrayList<>(playlists.values());
        } catch (SQLException exception) {
            throw erreurSql("Impossible de lire les playlists dans PostgreSQL.", exception);
        }
    }

    @Override
    public Playlist ajouter(Playlist playlist) throws IOException {
        String sql = "INSERT INTO playlist (nom, date_creation) VALUES (?, ?) RETURNING id_playlist";

        try (Connection connexion = ouvrirConnexion();
             PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, playlist.getNom());
            requete.setDate(2, Date.valueOf(playlist.getDateCreation()));
            try (ResultSet resultats = requete.executeQuery()) {
                resultats.next();
                playlist.setId(resultats.getLong("id_playlist"));
                return playlist;
            }
        } catch (SQLException exception) {
            throw erreurSql("Impossible d'ajouter la playlist dans PostgreSQL.", exception);
        }
    }

    @Override
    public boolean supprimer(long id) throws IOException {
        String sql = "DELETE FROM playlist WHERE id_playlist = ?";

        try (Connection connexion = ouvrirConnexion();
             PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setLong(1, id);
            return requete.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw erreurSql("Impossible de supprimer la playlist dans PostgreSQL.", exception);
        }
    }

    @Override
    public void enregistrerChansons(Playlist playlist) throws IOException {
        String supprimer = "DELETE FROM playlist_chanson WHERE id_playlist = ?";
        String inserer = """
                INSERT INTO playlist_chanson (id_playlist, id_chanson, position)
                VALUES (?, ?, ?)
                """;

        try (Connection connexion = ouvrirConnexion()) {
            connexion.setAutoCommit(false);
            try (PreparedStatement suppression = connexion.prepareStatement(supprimer);
                 PreparedStatement insertion = connexion.prepareStatement(inserer)) {
                suppression.setLong(1, playlist.getId());
                suppression.executeUpdate();

                List<Chanson> chansons = playlist.getChansons();
                for (int i = 0; i < chansons.size(); i++) {
                    insertion.setLong(1, playlist.getId());
                    insertion.setLong(2, chansons.get(i).getId());
                    insertion.setInt(3, i);
                    insertion.addBatch();
                }

                insertion.executeBatch();
                connexion.commit();
            } catch (SQLException exception) {
                connexion.rollback();
                throw exception;
            } finally {
                connexion.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            throw erreurSql("Impossible d'enregistrer les chansons de la playlist dans PostgreSQL.", exception);
        }
    }

    private Connection ouvrirConnexion() throws SQLException {
        InitialiseurBase.initialiserSiNecessaire();
        return ConnexionBase.obtenirConnexion();
    }

    private IOException erreurSql(String message, SQLException exception) {
        return new IOException(message + System.lineSeparator() + exception.getMessage(), exception);
    }
}
