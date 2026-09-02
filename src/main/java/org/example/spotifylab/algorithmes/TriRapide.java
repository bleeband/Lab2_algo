package org.example.spotifylab.algorithmes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class TriRapide<T> implements AlgorithmeTri<T> {
    @Override public String nom() { return "Tri rapide"; }
    @Override public String complexiteTheorique() { return "O(n log n) moyen, O(n²) pire"; }
    @Override public List<T> trier(List<T> elements, Comparator<T> comparateur) {
        List<T> resultat = new ArrayList<>(elements);
        triRapide(resultat, 0, resultat.size() - 1, comparateur);
        return resultat;
    }
    private void triRapide(List<T> liste, int debut, int fin, Comparator<T> comparateur) {
        if (debut >= fin) return;
        int pivot = partitionner(liste, debut, fin, comparateur);
        triRapide(liste, debut, pivot - 1, comparateur);
        triRapide(liste, pivot + 1, fin, comparateur);
    }
    private int partitionner(List<T> liste, int debut, int fin, Comparator<T> comparateur) {
        T pivot = liste.get(fin);
        int i = debut - 1;
        for (int j = debut; j < fin; j++) {
            if (comparateur.compare(liste.get(j), pivot) <= 0) Collections.swap(liste, ++i, j);
        }
        Collections.swap(liste, i + 1, fin);
        return i + 1;
    }
}
