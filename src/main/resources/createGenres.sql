CREATE TABLE genre
(
    id        INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name      VARCHAR(255)
);

INSERT INTO genre (name) values
                              ('Detective'),
                              ('Crime'),
                              ('Science'),
                              ('Post-apocalyptic'),
                              ('Fantasy'),
                              ('Western'),
                              ('Horror'),
                              ('Classic'),
                              ('Humor'),
                              ('Folklore');


INSERT INTO book_genre (book_id, genre_id) VALUES
