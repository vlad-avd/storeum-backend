INSERT INTO roles(id, name)
VALUES
    (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

INSERT INTO users(id, username, email, password)
VALUES
--        passwords are encoded: 'user' and 'admin' accordingly
    (1, 'user', 'user@gmail.com', '$2a$12$M0sPZsqwKvGdPzmkITDnPuFTDbCzGGsjvHXaA3PUEdYYJSymIXESG'),
    (2, 'admin', 'admin@gmail.com', '$2a$12$YfEgDr9gKxxMoEjZY5WD9eXVkT3h9b.IFMc6Fa8MSwvZbAeGtxo1W');

INSERT INTO user_roles(user_id, role_id)
VALUES
    (1, 1), (2, 1), (2, 2);