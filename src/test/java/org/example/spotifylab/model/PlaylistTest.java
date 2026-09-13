package org.example.spotifylab.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlaylistTest {
    @Test
    public void dureeTotaleAdditionneLesChansons() {
        Playlist playlist = new Playlist("Favoris");

        playlist.ajouter(chanson(1, "A", 120));
        playlist.ajouter(chanson(2, "B", 95));

        assertEquals(215, playlist.getDureeTotaleSec());
    }

    @Test
    public void deplacerEtRetirerGardentLeContenuAJour() {
        Playlist playlist = new Playlist("Route");
        Chanson premiere = chanson(1, "A", 120);
        Chanson deuxieme = chanson(2, "B", 95);
        playlist.ajouter(premiere);
        playlist.ajouter(deuxieme);

        assertTrue(playlist.deplacer(1, -1));
        assertEquals(deuxieme, playlist.getChansons().get(0));

        assertTrue(playlist.retirer(deuxieme));
        assertEquals(120, playlist.getDureeTotaleSec());
    }

    private Chanson chanson(long id, String titre, int dureeSec) {
        return new Chanson(id, titre, "Artiste", "Album", 2024, Genre.POP, dureeSec, 0);
    }
}
