package org.example.spotifylab.service;

import org.example.spotifylab.algorithmes.AlgorithmeTri;
import org.example.spotifylab.model.Chanson;

import java.util.ArrayList;
import java.util.List;


// Eva: Fix temporaire pour tester programme

import java.util.Comparator;

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
}

// Code de Marc-André:

//public class BenchmarkService {
//    public List<ResultatBenchmark> mesurer(List<Chanson> chansons,
//                                           List<AlgorithmeTri<Chanson>> algorithmes,
//                                           CritereTri critere) {
//        List<ResultatBenchmark> resultats = new ArrayList<>();
//        for (AlgorithmeTri<Chanson> algorithme : algorithmes) {
//            long debut = System.nanoTime();
//            algorithme.trier(chansons, critere.getComparateur());
//            long duree = System.nanoTime() - debut;
//            resultats.add(new ResultatBenchmark(algorithme.nom(), algorithme.complexiteTheorique(), duree));
//        }
//        return resultats;
//    }
//}
