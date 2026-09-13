package org.example.spotifylab.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {
    private String nom;
    private List<Chanson> chansons;
    private LocalDate dateCreation;

    public Playlist(String nom) {
        this.nom = nom;
        this.chansons = new ArrayList<>();
        this.dateCreation = LocalDate.now();
    }

    public boolean ajouter(Chanson chanson) {
        if (chanson == null || chansons.contains(chanson)) {
            return false;
        }
        chansons.add(chanson);
        return true;
    }

    public boolean retirer(Chanson chanson) {
        return chansons.remove(chanson);
    }

    public boolean deplacer(int index, int direction) {
        int nouvelIndex = index + direction;
        if (index < 0 || index >= chansons.size() || nouvelIndex < 0 || nouvelIndex >= chansons.size()) {
            return false;
        }
        Collections.swap(chansons, index, nouvelIndex);
        return true;
    }

    public void vider() {
        chansons.clear();
    }

    public int getDureeTotaleSec() {
        return chansons.stream()
                .mapToInt(Chanson::getDureeSec)
                .sum();
    }

    public String getNom() {
        return nom;
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    @Override
    public String toString() {
        return nom + " (" + chansons.size() + ", " + getDureeTotaleSec() + " s)";
    }
}
