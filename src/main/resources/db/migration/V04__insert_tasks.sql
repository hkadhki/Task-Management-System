
INSERT into tasks (title, description, priority, status, author_id, executor_id)
VALUES ('task1', 'dis', 'LOW', 'PENDING', 1, 2),
       ('task2', 'dis', 'MEDIUM', 'IN_PROGRESS', 1, 2),
       ('task3', 'dis', 'HIGH', 'IN_PROGRESS', 1, 3),
       ('task4', 'dis', 'LOW', 'PENDING', 1, 3);

INSERT into comments(date, text, author_id)
VALUES ('2024-01-01', 'text', 1),
       ('2024-01-01', 'text', 1),
       ('2024-01-01', 'text', 2),
       ('2024-01-01', 'text', 3),
       ('2024-01-01', 'text', 2);

INSERT into tasks_comments(task_entity_id, comments_id)
VALUES (1,1),
       (1,2),
       (1,3),
       (3,4),
       (2,5);




