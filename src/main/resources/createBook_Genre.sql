CREATE TABLE book_genre (
                             book_id INT,
                             genre_id INT,
                             PRIMARY KEY (book_id, genre_id),
                             FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
                             FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
);
INSERT INTO book_genre (book_id, genre_id) VALUES
                                               (1,10),
                                               (2,8),
                                               (3,10),
                                               (4,7),
                                               (5,9),
                                               (6,9),
                                               (7,3),
                                               (8,2),
                                               (9,10),
                                               (10,10),
                                               (11,8),
                                               (12,8),
                                               (13,9),
                                               (14,8),
                                               (15,9),
                                               (16,9),
                                               (17,7),
                                               (18,3),
                                               (19,8),
                                               (20,9),
                                               (21,3),
                                               (22,5),
                                               (23,1),
                                               (24,3),
                                               (25,5),
                                               (26,7),
                                               (27,8),
                                               (28,2),
                                               (29,5),
                                               (30,5),
                                               (31,1),
                                               (32,2),
                                               (33,5),
                                               (34,4),
                                               (35,8),
                                               (36,5),
                                               (37,10),
                                               (38,2),
                                               (39,10),
                                               (40,4),
                                               (41,9),
                                               (42,2),
                                               (43,1),
                                               (44,5),
                                               (45,7),
                                               (46,8),
                                               (47,5),
                                               (48,5),
                                               (49,7),
                                               (50,5),
                                               (51,7),
                                               (52,4),
                                               (53,4),
                                               (54,4),
                                               (55,2),
                                               (56,4),
                                               (57,4),
                                               (58,6),
                                               (59,3),
                                               (60,7),
                                               (61,8),
                                               (62,2),
                                               (63,7),
                                               (64,7),
                                               (65,4),
                                               (66,10),
                                               (67,5),
                                               (68,5),
                                               (69,6),
                                               (70,8),
                                               (71,2),
                                               (72,1),
                                               (73,9),
                                               (74,8),
                                               (75,6),
                                               (76,9),
                                               (77,2),
                                               (78,9),
                                               (79,8),
                                               (80,5),
                                               (81,9),
                                               (82,7),
                                               (83,2),
                                               (84,7),
                                               (85,8),
                                               (86,1),
                                               (87,2),
                                               (88,10),
                                               (89,10),
                                               (90,1),
                                               (91,3),
                                               (92,4),
                                               (93,1),
                                               (94,2),
                                               (95,10),
                                               (96,7),
                                               (97,6),
                                               (98,3),
                                               (99,5),
                                               (100,7),
                                               (1,4),
                                               (2,5),
                                               (3,9),
                                               (4,4),
                                               (5,6),
                                               (6,2),
                                               (7,8),
                                               (9,3),
                                               (10,9),
                                               (11,6),
                                               (12,10),
                                               (13,2),
                                               (14,2),
                                               (16,3),
                                               (17,10),
                                               (18,7),
                                               (21,5),
                                               (22,3),
                                               (23,7),
                                               (24,1),
                                               (27,4),
                                               (28,10),
                                               (29,6),
                                               (30,9),
                                               (31,9),
                                               (32,7),
                                               (33,4),
                                               (34,8),
                                               (35,1),
                                               (37,8),
                                               (38,9),
                                               (39,2),
                                               (42,3),
                                               (43,10),
                                               (45,8),
                                               (46,9),
                                               (47,1),
                                               (48,10),
                                               (49,6),
                                               (50,8),
                                               (51,9),
                                               (52,6),
                                               (53,1),
                                               (56,8),
                                               (57,6),
                                               (58,7),
                                               (59,7),
                                               (60,4),
                                               (61,2),
                                               (63,5),
                                               (64,1),
                                               (65,1),
                                               (66,7),
                                               (67,10),
                                               (68,7),
                                               (69,1),
                                               (70,7),
                                               (71,6),
                                               (72,3),
                                               (73,7),
                                               (74,4),
                                               (76,4),
                                               (77,3),
                                               (78,1),
                                               (79,10),
                                               (80,6),
                                               (81,10),
                                               (82,9),
                                               (83,3),
                                               (84,8),
                                               (85,10),
                                               (86,9),
                                               (87,7),
                                               (88,8),
                                               (89,5),
                                               (90,7),
                                               (91,4),
                                               (92,10),
                                               (93,7),
                                               (94,1),
                                               (96,10),
                                               (97,9),
                                               (98,4),
                                               (99,1),
                                               (100,2),
                                               (1,3),
                                               (2,10),
                                               (3,4),
                                               (4,3),
                                               (5,10),
                                               (6,6),
                                               (7,5),
                                               (10,8),
                                               (11,5),
                                               (12,4),
                                               (13,5),
                                               (15,7),
                                               (16,5),
                                               (17,5),
                                               (19,3),
                                               (20,8),
                                               (21,2),
                                               (23,3),
                                               (24,10),
                                               (25,2),
                                               (26,5),
                                               (27,10),
                                               (28,4),
                                               (29,8),
                                               (31,3),
                                               (32,1),
                                               (33,8),
                                               (35,2),
                                               (36,1),
                                               (37,1),
                                               (38,3),
                                               (39,6),
                                               (40,10),
                                               (43,3),
                                               (44,2),
                                               (45,10),
                                               (46,4),
                                               (47,2),
                                               (49,4),
                                               (50,10),
                                               (51,1),
                                               (52,7),
                                               (53,6),
                                               (54,6),
                                               (55,3),
                                               (57,8),
                                               (59,10),
                                               (60,5),
                                               (61,7),
                                               (62,9),
                                               (65,7),
                                               (66,4),
                                               (67,7),
                                               (68,6),
                                               (69,3),
                                               (70,1),
                                               (71,1),
                                               (72,2),
                                               (73,3),
                                               (74,2),
                                               (75,10),
                                               (76,1),
                                               (84,3),
                                               (86,4),
                                               (87,1),
                                               (89,4),
                                               (90,8),
                                               (91,1),
                                               (92,6),
                                               (93,10),
                                               (94,6),
                                               (96,2),
                                               (97,2),
                                               (98,6),
                                               (99,6);


