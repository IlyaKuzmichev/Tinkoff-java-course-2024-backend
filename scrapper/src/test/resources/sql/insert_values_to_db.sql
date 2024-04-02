INSERT INTO users (id) VALUES (101), (102), (103);

INSERT INTO links (link_type, url, last_check) VALUES
    ('github', 'https://github.com/IlyaKuzmichev/Scrapper_scammer', '2024-01-01 11:11:11.111111 +00:00'),
    ('github', 'https://github.com/IlyaKuzmichev/s21_Containers', '2024-01-01 11:11:11.111111 +00:00'),
    ('stackoverflow', 'https://stackoverflow.com/questions/5963269/how-to-make-a-great-r-reproducible-example', '2024-01-01 11:11:11.111111 +00:00'),
    ('stackoverflow', 'https://stackoverflow.com/questions/218384/what-is-a-nullpointerexception-and-how-do-i-fix-it' , '2025-01-01 11:11:11.111111 +00:00');

INSERT INTO github_links (link_id, last_update, last_push, pull_requests_count) VALUES
    (1, '2024-03-03 11:11:11.111111 +00:00', '2024-02-02 11:11:11.111111 +00:00', 1),
    (2, '2024-02-02 11:11:11.111111 +00:00', '2024-04-04 11:11:11.111111 +00:00', 9);

INSERT INTO stackoverflow_links (link_id, last_update, answers_count) VALUES
    (3, '2024-03-03 11:11:11.111111 +00:00', 2),
    (4, '2024-01-01 11:11:11.111111 +00:00', 8);

INSERT INTO user_tracked_links (user_id, link_id) VALUES
    (101, 1),
    (101, 2),
    (101, 3),
    (101, 4),
    (102, 2),
    (102, 4),
    (103, 3);


