package org.example.spotifylab.algorithmes;

import org.example.spotifylab.model.Chanson;
import org.example.spotifylab.model.Genre;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgorithmesTriTest {

    private final Chanson halo = new Chanson(1, "Halo", "Beyonce", "I Am", 2008, Genre.POP, 240, 50);
    private final Chanson one = new Chanson(2, "One", "Metallica", "Black Album", 1991, Genre.METAL, 450, 20);
    private final Chanson starlight = new Chanson(3, "Starlight", "Muse", "Black Holes", 2006, Genre.ROCK, 243, 100);
    private final List<Chanson> chansons = List.of(starlight, one, halo);

    @Test
    void triInsertionTrieParTitreSansModifierLaSource() {
        verifierTri(new TriInsertion<>());
    }

    @Test
    void triFusionTrieParTitreSansModifierLaSource() {
        verifierTri(new TriFusion<>());
    }

    @Test
    void triRapideTrieParTitreSansModifierLaSource() {
        verifierTri(new TriRapide<>());
    }

    private void verifierTri(AlgorithmeTri<Chanson> algorithme) {
        List<Chanson> resultat = algorithme.trier(
                chansons,
                Comparator.comparing(Chanson::getTitre, String.CASE_INSENSITIVE_ORDER)
        );

        assertEquals(List.of(halo, one, starlight), resultat);
        assertEquals(List.of(starlight, one, halo), chansons);
    }
}
