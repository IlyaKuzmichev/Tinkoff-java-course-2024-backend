--liquibase formatted sql

--changeset wilmerno:id1
DROP TABLE IF EXISTS users CASCADE;

--changeset wilmerno:id2
DROP TABLE IF EXISTS all_links CASCADE;

--changeset wilmerno:id3
DROP TABLE IF EXISTS github_links CASCADE;

--changeset wilmerno:id4
DROP TABLE IF EXISTS stackoverflow_links CASCADE;

--changeset wilmerno:id5
DROP TABLE IF  EXISTS user_tracked_links CASCADE;






