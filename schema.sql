CREATE TABLE artiste (
                         id_artiste SERIAL PRIMARY KEY,
                         nom VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE chanson (
                         id BIGINT PRIMARY KEY,
                         titre VARCHAR(200) NOT NULL,
                         album VARCHAR(200),
                         annee INTEGER CHECK (annee BETWEEN 1900 AND 2100),
                         genre VARCHAR(50) NOT NULL,
                         duree_sec INTEGER CHECK (duree_sec > 0),
                         ecoutes INTEGER DEFAULT 0 CHECK (ecoutes >= 0),
                         id_artiste INTEGER NOT NULL,

                         CONSTRAINT fk_chanson_artiste
                             FOREIGN KEY (id_artiste)
                                 REFERENCES artiste(id_artiste)
                                 ON DELETE RESTRICT
);