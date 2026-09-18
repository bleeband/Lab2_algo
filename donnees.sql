\encoding UTF8

TRUNCATE TABLE playlist_chanson, playlist, chanson, artiste RESTART IDENTITY CASCADE;

DROP TABLE IF EXISTS import_chanson;

CREATE TABLE import_chanson (
                                id BIGINT,
                                titre VARCHAR(200),
                                artiste VARCHAR(150),
                                album VARCHAR(200),
                                annee INTEGER,
                                genre VARCHAR(50),
                                duree_sec INTEGER,
                                ecoutes INTEGER
);


\copy import_chanson (id, titre, artiste, album, annee, genre, duree_sec, ecoutes) FROM 'src/main/resources/org/example/spotifylab/data/chansons.csv' WITH (FORMAT csv, HEADER true, DELIMITER ';')

INSERT INTO artiste (nom)
SELECT DISTINCT artiste
FROM import_chanson
ORDER BY artiste;

INSERT INTO chanson (
    id,
    titre,
    album,
    annee,
    genre,
    duree_sec,
    ecoutes,
    id_artiste
)
SELECT
    i.id,
    i.titre,
    i.album,
    i.annee,
    i.genre,
    i.duree_sec,
    i.ecoutes,
    a.id_artiste
FROM import_chanson i
         JOIN artiste a
              ON a.nom = i.artiste;

SELECT setval(pg_get_serial_sequence('chanson', 'id'), COALESCE(MAX(id), 1))
FROM chanson;

DROP TABLE import_chanson;
