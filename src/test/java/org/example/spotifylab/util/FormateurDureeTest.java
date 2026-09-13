package org.example.spotifylab.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormateurDureeTest {

    @Test
    void formateMinutesEtSecondes() {
        assertEquals("0:00", FormateurDuree.formater(0));
        assertEquals("3:05", FormateurDuree.formater(185));
        assertEquals("59:59", FormateurDuree.formater(3599));
    }

    @Test
    void formateAvecHeuresQuandNecessaire() {
        assertEquals("1:00:00", FormateurDuree.formater(3600));
        assertEquals("1:02:03", FormateurDuree.formater(3723));
    }

    @Test
    void formateDureeLonguePourPlaylists() {
        assertEquals("0 min", FormateurDuree.formaterLong(-10));
        assertEquals("3 min", FormateurDuree.formaterLong(185));
        assertEquals("1 h 02 min", FormateurDuree.formaterLong(3723));
    }
}
