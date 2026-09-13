package org.example.spotifylab.service;

import org.example.spotifylab.algorithmes.AlgorithmeTri;
import org.example.spotifylab.model.Chanson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BenchmarkService {
    public List<ResultatBenchmark> mesurer(List<Chanson> chansons,
                                           List<AlgorithmeTri<Chanson>> algorithmes,
                                           Comparator<Chanson> comparateur) {
        List<ResultatBenchmark> resultats = new ArrayList<>();
        for (AlgorithmeTri<Chanson> algorithme : algorithmes) {
            long debut = System.nanoTime();
            algorithme.trier(chansons, comparateur);
            long duree = System.nanoTime() - debut;
            resultats.add(new ResultatBenchmark(algorithme.nom(), algorithme.complexiteTheorique(), duree));
        }
        return resultats;
    }

    public List<ResultatBenchmark> mesurer(List<Chanson> chansons,
                                           List<AlgorithmeTri<Chanson>> algorithmes,
                                           CritereTri critere) {
        return mesurer(chansons, algorithmes, critere.getComparateur());
    }
}
