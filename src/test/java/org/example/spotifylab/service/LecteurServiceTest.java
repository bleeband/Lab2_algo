package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class LecteurServiceTest {

    @Test
    void lireCompteLaPremiereChanson() {
        LecteurService lecteur = new LecteurService();
        Chanson chanson = new Chanson(1, "Halo", "Beyonce", "Album", 2008, Genre.POP, 240, 10);

        Chanson chansonLue = lecteur.lire(List.of(chanson), chanson);

        assertSame(chanson, chansonLue);
        assertEquals(11, chanson.getEcoutes());
    }

    @Test
    void reprendreLectureNeRecomptePasLaMemeChanson() {
        LecteurService lecteur = new LecteurService();
        Chanson chanson = new Chanson(1, "Halo", "Beyonce", "Album", 2008, Genre.POP, 240, 10);

        lecteur.lire(List.of(chanson), chanson);
        lecteur.pause();
        lecteur.demarrer();

        assertEquals(11, chanson.getEcoutes());
    }
}
