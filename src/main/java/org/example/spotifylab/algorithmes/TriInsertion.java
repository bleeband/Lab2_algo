package org.example.spotifylab.algorithmes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TriInsertion<T> implements AlgorithmeTri<T> {
    @Override public String nom() { return "Tri par insertion"; }
    @Override public String complexiteTheorique() { return "O(n²)"; }
    @Override public List<T> trier(List<T> elements, Comparator<T> comparateur) {
        List<T> resultat = new ArrayList<>(elements);
        for (int i = 1; i < resultat.size(); i++) {
            T valeur = resultat.get(i);
            int j = i - 1;
            while (j >= 0 && comparateur.compare(resultat.get(j), valeur) > 0) {
                resultat.set(j + 1, resultat.get(j));
                j--;
            }
            resultat.set(j + 1, valeur);
        }
        return resultat;
    }
}