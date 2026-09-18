package org.example.spotifylab.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnexionBase {

    private static final Path FICHIER_CONFIGURATION = Path.of("database.properties");
    private static final String CLE_URL = "db.url";
    private static final String CLE_UTILISATEUR = "db.user";
    private static final String CLE_MOT_DE_PASSE = "db.password";

    private ConnexionBase() {
    }

    public static Connection obtenirConnexion() throws SQLException {
        Properties configuration = lireConfiguration();
        String url = lireValeurObligatoire(configuration, CLE_URL);
        String utilisateur = lireValeurObligatoire(configuration, CLE_UTILISATEUR);
        String motDePasse = lireValeurObligatoire(configuration, CLE_MOT_DE_PASSE);

        try {
            return DriverManager.getConnection(url, utilisateur, motDePasse);
        } catch (SQLException exception) {
            if ("3D000".equals(exception.getSQLState())) {
                creerBaseSiAbsente(url, utilisateur, motDePasse);
                return DriverManager.getConnection(url, utilisateur, motDePasse);
            }
            throw exception;
        }
    }

    private static void creerBaseSiAbsente(String url, String utilisateur, String motDePasse) throws SQLException {
        String nomBase = extraireNomBase(url);
        String urlMaintenance = construireUrlMaintenance(url);

        try (Connection connexion = DriverManager.getConnection(urlMaintenance, utilisateur, motDePasse);
             java.sql.Statement requete = connexion.createStatement()) {
            requete.executeUpdate("CREATE DATABASE " + nomSqlSecurise(nomBase));
        }
    }

    private static String extraireNomBase(String url) throws SQLException {
        int debut = url.lastIndexOf('/');
        if (debut < 0 || debut == url.length() - 1) {
            throw new SQLException("URL JDBC invalide. Exemple attendu: jdbc:postgresql://localhost:5432/spotify_lab");
        }

        int fin = url.indexOf('?', debut);
        String nomBase = fin < 0 ? url.substring(debut + 1) : url.substring(debut + 1, fin);
        if (!nomBase.matches("[A-Za-z0-9_]+")) {
            throw new SQLException("Nom de base invalide dans database.properties: " + nomBase);
        }
        return nomBase;
    }

    private static String construireUrlMaintenance(String url) throws SQLException {
        int debut = url.lastIndexOf('/');
        if (debut < 0) {
            throw new SQLException("URL JDBC invalide. Exemple attendu: jdbc:postgresql://localhost:5432/spotify_lab");
        }

        int fin = url.indexOf('?', debut);
        if (fin < 0) {
            return url.substring(0, debut + 1) + "postgres";
        }
        return url.substring(0, debut + 1) + "postgres" + url.substring(fin);
    }

    private static String nomSqlSecurise(String nomBase) {
        return "\"" + nomBase + "\"";
    }

    private static Properties lireConfiguration() throws SQLException {
        if (!Files.exists(FICHIER_CONFIGURATION)) {
            throw new SQLException("Fichier database.properties introuvable. Copiez database.properties.example.");
        }

        Properties configuration = new Properties();
        try (InputStream entree = Files.newInputStream(FICHIER_CONFIGURATION)) {
            configuration.load(entree);
            return configuration;
        } catch (IOException exception) {
            throw new SQLException("Impossible de lire database.properties.", exception);
        }
    }

    private static String lireValeurObligatoire(Properties configuration, String cle) throws SQLException {
        String valeur = configuration.getProperty(cle);
        if (valeur == null || valeur.isBlank()) {
            throw new SQLException("Cle manquante dans database.properties: " + cle);
        }
        return valeur;
    }
}
