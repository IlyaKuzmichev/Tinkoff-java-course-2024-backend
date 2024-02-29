--liquibase formatted sql

--changeset author:wilmerno version 0.1
CREATE TABLE users
(
    user_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    chat_id BIGINT UNIQUE NOT NULL
);

--changeset author:wilmerno version 0.1
CREATE TABLE all_links
(
    link_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    link_type VARCHAR(20) NOT NULL CHECK (link_type IN ('github', 'stackoverflow')),
    url VARCHAR UNIQUE NOT NULL
);

--changeset author:wilmerno version 0.1
CREATE TABLE github_links
(
    link_id BIGINT PRIMARY KEY REFERENCES all_links(link_id),
    last_update TIMESTAMP
    -- another parameters for tracking TODO
);

--changeset author:wilmerno version 0.1
CREATE TABLE stackoverflow_links
(
    link_id BIGINT PRIMARY KEY REFERENCES all_links(link_id)
    last_update TIMESTAMP
    -- another parameters for tracking TODO
);

--changeset author:wilmerno version 0.1
CREATE TABLE user_tracked_links
(
    user_id BIGINT REFERENCES users(user_id),
    link_id BIGINT REFERENCES all_links(link_id),
    PRIMARY KEY (user_id, link_id)
)





