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


COPY import_chanson (
    id,
    titre,
    artiste,
    album,
    annee,
    genre,
    duree_sec,
    ecoutes
    )
    FROM 'METTRE LE PATH DU CSV ICI'
    WITH (
    FORMAT CSV,
    HEADER TRUE,
    DELIMITER ';',
    ENCODING 'UTF8'
    );

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

DROP TABLE import_chanson;