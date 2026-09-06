package org.example.spotifylab.service;

/** Mesure d'un algorithme de tri. */
public record ResultatBenchmark(String algorithme, String complexite, long nanosecondes) {
    public double millisecondes() { return nanosecondes / 1_000_000.0; }
}
