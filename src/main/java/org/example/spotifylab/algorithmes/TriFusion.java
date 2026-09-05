package org.example.spotifylab.algorithmes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TriFusion<T> implements AlgorithmeTri<T> {
    @Override public String nom() { return "Tri fusion"; }
    @Override public String complexiteTheorique() { return "O(n log n)"; }
    @Override public List<T> trier(List<T> elements, Comparator<T> comparateur) {
        if (elements.size() <= 1) return new ArrayList<>(elements);
        int milieu = elements.size() / 2;
        return fusionner(trier(elements.subList(0, milieu), comparateur),
                trier(elements.subList(milieu, elements.size()), comparateur), comparateur);
    }
    private List<T> fusionner(List<T> gauche, List<T> droite, Comparator<T> comparateur) {
        List<T> resultat = new ArrayList<>(gauche.size() + droite.size());
        int i = 0, j = 0;
        while (i < gauche.size() && j < droite.size()) {
            if (comparateur.compare(gauche.get(i), droite.get(j)) <= 0) resultat.add(gauche.get(i++));
            else resultat.add(droite.get(j++));
        }
        while (i < gauche.size()) resultat.add(gauche.get(i++));
        while (j < droite.size()) resultat.add(droite.get(j++));
        return resultat;
    }
}
