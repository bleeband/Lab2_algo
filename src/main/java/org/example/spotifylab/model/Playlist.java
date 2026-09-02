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

    public void ajouterChanson(Chanson chanson) {
        if (!chansons.contains(chanson)) {
            chansons.add(chanson);
        }
    }
    public void retirerChanson(Chanson chanson) {
        chansons.remove(chanson);
    }
    public void monterChanson(int index) {
        if (index > 0 && index < chansons.size()) {
            Collections.swap(chansons, index, index - 1);
        }
    }
    public void descendreChanson(int index) {
        if (index >= 0 && index < chansons.size() - 1) {
            Collections.swap(chansons, index, index + 1);
        }
    }
    public void vider() {
        chansons.clear();
    }

    public int getDureeTotaleSec() {
        int total = 0;
        for (Chanson chanson : chansons) {
            total += chanson.getDureeSec();
        }
        return total;
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
}