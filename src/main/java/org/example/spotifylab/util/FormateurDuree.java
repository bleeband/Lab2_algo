package org.example.spotifylab.util;

public final class FormateurDuree {
    private FormateurDuree() {}

    public static String formater(int secondes) {
        int secondesValides = Math.max(0, secondes);
        int heures = secondesValides / 3600;
        int minutes = (secondesValides % 3600) / 60;
        int secondesRestantes = secondesValides % 60;

        if (heures > 0) {
            return "%d:%02d:%02d".formatted(heures, minutes, secondesRestantes);
        }

        return "%d:%02d".formatted(minutes, secondesRestantes);
    }

    public static String formaterLong(int secondes) {
        int secondesValides = Math.max(0, secondes);
        int heures = secondesValides / 3600;
        int minutes = (secondesValides % 3600) / 60;

        if (heures > 0) {
            return "%d h %02d min".formatted(heures, minutes);
        }

        return "%d min".formatted(minutes);
    }
}
