INSERT INTO roles(name)
VALUES
    ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users(username, email, password)
VALUES
--        passwords are encoded: 'user' and 'admin' accordingly
    ('user', 'user@gmail.com', '$2a$12$M0sPZsqwKvGdPzmkITDnPuFTDbCzGGsjvHXaA3PUEdYYJSymIXESG'),
    ('admin', 'admin@gmail.com', '$2a$12$YfEgDr9gKxxMoEjZY5WD9eXVkT3h9b.IFMc6Fa8MSwvZbAeGtxo1W');

INSERT INTO user_roles(user_id, role_id)
VALUES
    (1, 1), (2, 1), (2, 2);

INSERT INTO folder(title, user_id, parent_folder_id)
VALUES
    ('Development', 1, null),
    ('English', 1, null),
    ('Books', 1, 1),
    ('Courses', 1, 1),
    ('Articles', 1, 2),
    ('Java', 1, 3),
    ('Python', 1, 3);