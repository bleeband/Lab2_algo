package org.example.spotifylab.model;

public class Chanson {

    private int id;
    private String titre;
    private String artiste;
    private String album;
    private int annee;
    private Genre genre;
    private int dureeSec;
    private int ecoutes;

    public Chanson(int id, String titre, String artiste, String album,
                   int annee, Genre genre, int dureeSec, int ecoutes) {

        this.id = id;
        this.titre = titre;
        this.artiste = artiste;
        this.album = album;
        this.annee = annee;
        this.genre = genre;
        this.dureeSec = dureeSec;
        this.ecoutes = ecoutes;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getArtiste() {
        return artiste;
    }

    public String getAlbum() {
        return album;
    }

    public int getAnnee() {
        return annee;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getDureeSec() {
        return dureeSec;
    }

    public int getEcoutes() {
        return ecoutes;
    }

    public void incrementerEcoutes() {
        ecoutes++;
    }

    @Override
    public String toString() {
        return titre + " - " + artiste;
    }
}