--liquibase formatted sql


--changeset wilmerno:create_user_status_enum
CREATE TYPE user_status_enum AS ENUM (
    'base',
    'track_link',
    'untrack_link'
    );

--changeset wilmerno:create_link_type_enum
CREATE TYPE link_type_enum AS ENUM (
    'github',
    'stackoverflow'
    );

--changeset wilmerno:create_table_users
CREATE TABLE IF NOT EXISTS users
(
    id BIGINT UNIQUE NOT NULL,
    user_status user_status_enum NOT NULL DEFAULT 'base'
);

--changeset wilmerno:create_table_links
CREATE TABLE IF NOT EXISTS links
(
    id         BIGSERIAL PRIMARY KEY,
    link_type link_type_enum NOT NULL,
    url VARCHAR UNIQUE NOT NULL,
    last_check TIMESTAMP WITH TIME ZONE
);

--changeset wilmerno:create_table_github
CREATE TABLE IF NOT EXISTS github_links
(
    link_id BIGINT PRIMARY KEY REFERENCES links(id) ON DELETE CASCADE,
    last_update TIMESTAMP WITH TIME ZONE NOT NULL,
    last_push   TIMESTAMP WITH TIME ZONE NOT NULL,
    pull_requests_count INTEGER NOT NULL DEFAULT 0
);

--changeset wilmerno:create_table_stackoverflow
CREATE TABLE IF NOT EXISTS stackoverflow_links
(
    link_id BIGINT PRIMARY KEY REFERENCES links(id) ON DELETE CASCADE,
    last_update TIMESTAMP WITH TIME ZONE NOT NULL,
    answers_count INTEGER NOT NULL DEFAULT 0
);

--changeset wilmerno:create_table_user_tracked_links
CREATE TABLE IF NOT EXISTS user_tracked_links
(
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    link_id BIGINT REFERENCES links(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, link_id)
);
