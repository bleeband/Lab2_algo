package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChansonServiceTest {

    private final ChansonService service = new ChansonService();

    private final List<Chanson> chansons = List.of(
            new Chanson(1, "Halo", "Beyoncé", "Album 1",
                    2008, Genre.POP, 240, 10),

            new Chanson(2, "One", "Metallica", "Album 2",
                    1991, Genre.METAL, 450, 20),

            new Chanson(3, "Rock Song", "Queen", "Album 3",
                    1995, Genre.ROCK, 200, 5)
    );

    @Test
    public void rechercheIgnoreCasseEtAccents() {

        List<Chanson> resultat =
                service.rechercher(chansons, "BEYONCE");

        assertEquals(1, resultat.size());
        assertEquals("Beyoncé", resultat.get(0).getArtiste());
    }

    @Test
    public void filtresPeuventSeCombiner() {

        List<Chanson> resultat = service.filtrer(
                chansons,
                Genre.ROCK,
                1990,
                "Queen",
                250,
                null
        );

        assertEquals(1, resultat.size());
        assertEquals("Rock Song", resultat.get(0).getTitre());
    }
}