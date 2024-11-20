

INSERT into users (email, username, password)
VALUES ('User1@gmail.com', 'User1','$2a$10$xicIe0E5qkLBbUJO70tGS.gCFFR1qNt22O1qxGNLI.7M4HqkRbEJC'),
       ('User2@gmail.com', 'User2', '$2a$10$BBNH/PNCsroV5ppAY2DJgeayU.jrleS9ERHcyBNcr0pWzHos4ScDW'),
       ('User3@gmail.com', 'User3', '$2a$10$BBNH/PNCsroV5ppAY2DJgeayU.jrleS9ERHcyBNcr0pWzHos4ScDW');

INSERT into roles (name)
Values ('ADMIN'),
       ('USER');

INSERT into user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 2)