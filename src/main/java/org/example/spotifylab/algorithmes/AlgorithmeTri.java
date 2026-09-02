package org.example.spotifylab.algorithmes;

import java.util.Comparator;
import java.util.List;

public interface AlgorithmeTri<T> {
    String nom();
    String complexiteTheorique();
    // chaque tri retourne une nouvelle liste afin de ne pas modifier le catalogue original
    List<T> trier(List<T> elements, Comparator<T> comparateur);
}