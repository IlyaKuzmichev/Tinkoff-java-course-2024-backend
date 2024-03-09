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

--changeset wilmerno:id6
ALTER TABLE users
    ALTER COLUMN user_id DROP IDENTITY;

--changeset wilmerno:id7
UPDATE users
SET user_id = chat_id;

--changeset wilmerno:id8
ALTER TABLE users
DROP COLUMN chat_id;

--changeset wilmerno:id9
CREATE TYPE user_status_enum AS ENUM (
    'base',
    'track_link_waiting',
    'untrack_link_waiting'
    );



--changeset wilmerno:id10
ALTER TABLE users
ADD COLUMN user_status user_status_enum NOT NULL DEFAULT 'base';

--changeset wilmerno:id11
ALTER TABLE all_links RENAME TO links;

--changeset wilmerno:id12
CREATE TYPE link_type_enum AS ENUM (
  'github',
  'stackoverflow'
);

--changeset wilmerno:id13
ALTER TABLE links
ALTER COLUMN link_type SET DATA TYPE link_type_enum USING link_type::link_type_enum;

--changeset wilmerno:id14
ALTER TABLE user_tracked_links
DROP CONSTRAINT user_tracked_links_link_id_fkey,
DROP CONSTRAINT user_tracked_links_user_id_fkey,
ADD CONSTRAINT user_tracked_links_link_id_fkey
   FOREIGN KEY (link_id)
   REFERENCES links(link_id)
   ON DELETE CASCADE,
ADD CONSTRAINT user_tracked_links_user_id_fkey
    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE;

--changeset wilmerno:id15
ALTER TABLE stackoverflow_links
DROP CONSTRAINT stackoverflow_links_link_id_fkey,
ADD CONSTRAINT stackoverflow_links_link_id_fkey
    FOREIGN KEY (link_id)
    REFERENCES links(link_id)
    ON DELETE CASCADE;

--changeset wilmerno:id16
ALTER TABLE github_links
    DROP CONSTRAINT github_links_link_id_fkey,
    ADD CONSTRAINT github_links_link_id_fkey
        FOREIGN KEY (link_id)
            REFERENCES links(link_id)
            ON DELETE CASCADE;


