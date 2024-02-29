--liquibase formatted sql

--changeset wilmerno:id1
CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    chat_id BIGINT UNIQUE NOT NULL
);

--changeset wilmerno:id2
CREATE TABLE IF NOT EXISTS all_links
(
    link_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    link_type VARCHAR(20) NOT NULL CHECK (link_type IN ('github', 'stackoverflow')),
    url VARCHAR UNIQUE NOT NULL
);

--changeset wilmerno:id3
CREATE TABLE IF NOT EXISTS github_links
(
    link_id BIGINT PRIMARY KEY REFERENCES all_links(link_id),
    last_update TIMESTAMP
    -- another parameters for tracking TODO
);

--changeset wilmerno:id4
CREATE TABLE IF NOT EXISTS stackoverflow_links
(
    link_id BIGINT PRIMARY KEY REFERENCES all_links(link_id),
    last_update TIMESTAMP
    -- another parameters for tracking TODO
);

--changeset wilmerno:id5
CREATE TABLE IF NOT EXISTS user_tracked_links
(
    user_id BIGINT REFERENCES users(user_id),
    link_id BIGINT REFERENCES all_links(link_id),
    PRIMARY KEY (user_id, link_id)
);
