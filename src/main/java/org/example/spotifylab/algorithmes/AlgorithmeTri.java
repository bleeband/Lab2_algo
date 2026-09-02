package org.example.spotifylab.algorithmes;

import java.util.Comparator;
import java.util.List;

public interface AlgorithmeTri<T> {
    String nom();
    String complexiteTheorique();
    List<T> trier(List<T> elements, Comparator<T> comparateur);
}