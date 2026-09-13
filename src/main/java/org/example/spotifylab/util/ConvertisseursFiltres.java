package org.example.spotifylab.util;

import javafx.util.StringConverter;
import org.example.spotifylab.model.Genre;

public class ConvertisseursFiltres {

    public static StringConverter<Genre> pourGenre() {
        return new StringConverter<Genre>() {
            @Override
            public String toString(Genre genre) {
                if (genre == null) {
                    return "Genre";
                } else {
                    return genre.name();
                }
            }

            @Override
            public Genre fromString(String texte) {
                return null;
            }
        };
    }

    public static StringConverter<String> pourArtiste() {
        return new StringConverter<String>() {
            @Override
            public String toString(String artiste) {
                if (artiste == null) {
                    return "Artiste";
                } else {
                    return artiste;
                }
            }

            @Override
            public String fromString(String texte) {
                return null;
            }
        };
    }

    public static StringConverter<Integer> pourDecennie() {
        return new StringConverter<Integer>() {
            @Override
            public String toString(Integer decennie) {
                if (decennie == null) {
                    return "Décennie";
                } else {
                    return decennie + "s";
                }
            }

            @Override
            public Integer fromString(String texte) {
                return null;
            }
        };
    }
}