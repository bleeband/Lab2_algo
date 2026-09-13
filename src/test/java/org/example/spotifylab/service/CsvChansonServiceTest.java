package org.example.spotifylab.service;

import org.example.spotifylab.model.Chanson;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvChansonServiceTest {

    @Test
    public void chargerChansonsCharge494Chansons() throws IOException {

        SourceDonnees sourceDonnees = new CsvChansonService();

        List<Chanson> chansons = sourceDonnees.chargerChansons();

        assertEquals(494, chansons.size());
    }
}