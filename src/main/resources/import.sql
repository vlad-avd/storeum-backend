INSERT INTO roles(id, name)
VALUES
    (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

INSERT INTO users(id, first_name, email, password, is_enabled)
VALUES
-- passwords are encoded: 'user' and 'admin' accordingly
    (1, 'user', 'user@gmail.com', '$2a$12$M0sPZsqwKvGdPzmkITDnPuFTDbCzGGsjvHXaA3PUEdYYJSymIXESG', 'true'),
    (2, 'admin', 'admin@gmail.com', '$2a$12$YfEgDr9gKxxMoEjZY5WD9eXVkT3h9b.IFMc6Fa8MSwvZbAeGtxo1W', 'true');

INSERT INTO user_roles(user_id, role_id)
VALUES
    (1, 1), (2, 1), (2, 2);

INSERT INTO folder(id, title, user_id, parent_folder_id)
VALUES
    (1, 'Development', 1, null),
    (2, 'English', 1, null),
    (3, 'Leisure', 1, null),
    (4, 'Books', 1, 1),
    (5, 'Courses', 1, 1),
    (6, 'Articles', 1, 2),
    (7, 'Java', 1, 3),
    (8, 'Python', 1, 3);

INSERT INTO note(id, title, description, link, folder_id, user_id)
VALUES
    (1, 'BOTW map', '', 'https://zeldamaps.com/?game=BotW', 3, 1),
    (2, 'Мы обречены', NULL, 'https://www.youtube.com/channel/UCUSbYJK87rpBUJ5KGQd7oHA', 3, 1),
    (3, 'projects', 'Storeum', NULL, 3, 1),
    (4, 'Ravesli', 'linux', 'https://ravesli.com/uroki-po-linux/', 3, 1),
    (5, 'Game making', 'Godot, libgdx', NULL, 3, 1),
    (6, 'habr', NULL, 'https://habr.com/ru/', 3, 1),
    (7, 'Lorem', 'Ipsum', NULL, 3, 1),
    (8, 'Lorem', 'Ipsum', NULL, 3, 1),
    (9, 'Lorem', 'Ipsum', NULL, 3, 1),
    (10, 'Lorem', 'Ipsum', NULL, 3, 1),
    (11, 'Lorem', 'Ipsum', NULL, 3, 1),
    (12, 'Lorem', 'Ipsum', NULL, 3, 1),
    (13, 'Lorem', 'Ipsum', NULL, 3, 1),
    (14, 'Lorem', 'Ipsum', NULL, 3, 1),
    (15, 'Lorem', 'Ipsum', NULL, 3, 1),
    (16, 'Lorem', 'Ipsum', NULL, 3, 1),
    (17, 'Lorem', 'Ipsum', NULL, 3, 1),
    (18, 'Lorem', 'Ipsum', NULL, 3, 1),
    (19, 'Lorem', 'Ipsum', NULL, 3, 1),
    (20, 'Lorem', 'Ipsum', NULL, 3, 1),
    (21, 'Lorem', 'Ipsum', NULL, 3, 1),
    (22, 'Lorem', 'Ipsum', NULL, 3, 1),
    (23, 'Lorem', 'Ipsum', NULL, 3, 1),
    (24, 'Lorem', 'Ipsum', NULL, 3, 1),
    (25, 'Lorem', 'Ipsum', NULL, 3, 1);

-- start_index = table_size + sequence_incremented_size
ALTER SEQUENCE user_sequence RESTART WITH 12;
ALTER SEQUENCE folder_sequence RESTART WITH 18;
ALTER SEQUENCE note_sequence RESTART WITH 35;
ALTER SEQUENCE ec_token_sequence RESTART WITH 10;