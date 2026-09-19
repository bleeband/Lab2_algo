package org.example.spotifylab.dao;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgreSqlChansonDao implements ChansonDao {

    @Override
    public List<Chanson> trouverTous() throws IOException {
        String sql = """
                SELECT c.id, c.titre, a.nom AS artiste, c.album, c.annee,
                       c.genre, c.duree_sec, c.ecoutes
                FROM chanson c
                JOIN artiste a ON a.id_artiste = c.id_artiste
                ORDER BY c.id
                """;

        try (Connection connexion = ouvrirConnexion();
             PreparedStatement requete = connexion.prepareStatement(sql);
             ResultSet resultats = requete.executeQuery()) {
            List<Chanson> chansons = new ArrayList<>();
            while (resultats.next()) {
                chansons.add(lireChanson(resultats));
            }
            return chansons;
        } catch (SQLException exception) {
            throw erreurSql("Impossible de lire les chansons dans PostgreSQL.", exception);
        }
    }

    @Override
    public Optional<Chanson> trouverParId(long id) throws IOException {
        String sql = """
                SELECT c.id, c.titre, a.nom AS artiste, c.album, c.annee,
                       c.genre, c.duree_sec, c.ecoutes
                FROM chanson c
                JOIN artiste a ON a.id_artiste = c.id_artiste
                WHERE c.id = ?
                """;

        try (Connection connexion = ouvrirConnexion();
             PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setLong(1, id);
            try (ResultSet resultats = requete.executeQuery()) {
                if (resultats.next()) {
                    return Optional.of(lireChanson(resultats));
                }
                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw erreurSql("Impossible de trouver la chanson " + id + " dans PostgreSQL.", exception);
        }
    }

    @Override
    public Chanson ajouter(Chanson chanson) throws IOException {
        String sql = """
                INSERT INTO chanson (titre, album, annee, genre, duree_sec, ecoutes, id_artiste)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection connexion = ouvrirConnexion()) {
            connexion.setAutoCommit(false);
            try {
                long idArtiste = trouverOuAjouterArtiste(connexion, chanson.getArtiste());
                try (PreparedStatement requete = connexion.prepareStatement(sql)) {
                    requete.setString(1, chanson.getTitre());
                    requete.setString(2, chanson.getAlbum());
                    requete.setInt(3, chanson.getAnnee());
                    requete.setString(4, chanson.getGenre().name());
                    requete.setInt(5, chanson.getDureeSec());
                    requete.setInt(6, chanson.getEcoutes());
                    requete.setLong(7, idArtiste);

                    try (ResultSet resultats = requete.executeQuery()) {
                        resultats.next();
                        connexion.commit();
                        return new Chanson(
                                resultats.getLong("id"),
                                chanson.getTitre(),
                                chanson.getArtiste(),
                                chanson.getAlbum(),
                                chanson.getAnnee(),
                                chanson.getGenre(),
                                chanson.getDureeSec(),
                                chanson.getEcoutes()
                        );
                    }
                }
            } catch (SQLException exception) {
                connexion.rollback();
                throw exception;
            } finally {
                connexion.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            throw erreurSql("Impossible d'ajouter la chanson dans PostgreSQL.", exception);
        }
    }

    @Override
    public boolean modifier(Chanson chanson) throws IOException {
        String sql = """
                UPDATE chanson
                SET titre = ?, album = ?, annee = ?, genre = ?,
                    duree_sec = ?, ecoutes = ?, id_artiste = ?
                WHERE id = ?
                """;

        try (Connection connexion = ouvrirConnexion()) {
            connexion.setAutoCommit(false);
            try {
                long idArtiste = trouverOuAjouterArtiste(connexion, chanson.getArtiste());
                try (PreparedStatement requete = connexion.prepareStatement(sql)) {
                    requete.setString(1, chanson.getTitre());
                    requete.setString(2, chanson.getAlbum());
                    requete.setInt(3, chanson.getAnnee());
                    requete.setString(4, chanson.getGenre().name());
                    requete.setInt(5, chanson.getDureeSec());
                    requete.setInt(6, chanson.getEcoutes());
                    requete.setLong(7, idArtiste);
                    requete.setLong(8, chanson.getId());

                    boolean modifiee = requete.executeUpdate() > 0;
                    connexion.commit();
                    return modifiee;
                }
            } catch (SQLException exception) {
                connexion.rollback();
                throw exception;
            } finally {
                connexion.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            throw erreurSql("Impossible de modifier la chanson " + chanson.getId() + " dans PostgreSQL.", exception);
        }
    }

    @Override
    public boolean supprimer(long id) throws IOException {
        String sql = "DELETE FROM chanson WHERE id = ?";

        try (Connection connexion = ouvrirConnexion();
             PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setLong(1, id);
            return requete.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw erreurSql("Impossible de supprimer la chanson " + id + " dans PostgreSQL.", exception);
        }
    }

    private long trouverOuAjouterArtiste(Connection connexion, String nom) throws SQLException {
        Optional<Long> idExistant = trouverIdArtiste(connexion, nom);
        if (idExistant.isPresent()) {
            return idExistant.get();
        }

        String sql = "INSERT INTO artiste (nom) VALUES (?)";
        try (PreparedStatement requete = connexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            requete.setString(1, nom);
            requete.executeUpdate();
            try (ResultSet cles = requete.getGeneratedKeys()) {
                cles.next();
                return cles.getLong(1);
            }
        }
    }

    private Optional<Long> trouverIdArtiste(Connection connexion, String nom) throws SQLException {
        String sql = "SELECT id_artiste FROM artiste WHERE nom = ?";
        try (PreparedStatement requete = connexion.prepareStatement(sql)) {
            requete.setString(1, nom);
            try (ResultSet resultats = requete.executeQuery()) {
                if (resultats.next()) {
                    return Optional.of(resultats.getLong("id_artiste"));
                }
                return Optional.empty();
            }
        }
    }

    private Chanson lireChanson(ResultSet resultats) throws SQLException {
        return new Chanson(
                resultats.getLong("id"),
                resultats.getString("titre"),
                resultats.getString("artiste"),
                resultats.getString("album"),
                resultats.getInt("annee"),
                Genre.valueOf(resultats.getString("genre")),
                resultats.getInt("duree_sec"),
                resultats.getInt("ecoutes")
        );
    }

    private Connection ouvrirConnexion() throws SQLException {
        InitialiseurBase.initialiserSiNecessaire();
        return ConnexionBase.obtenirConnexion();
    }

    private IOException erreurSql(String message, SQLException exception) {
        return new IOException(message + System.lineSeparator() + exception.getMessage(), exception);
    }
}
