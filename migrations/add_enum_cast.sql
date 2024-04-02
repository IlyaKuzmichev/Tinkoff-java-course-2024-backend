--liquibase formatted sql


--changeset wilmerno:cast_to_user_status_enum
CREATE CAST(VARCHAR AS user_status_enum) WITH INOUT AS IMPLICIT;

--changeset wilmerno:cast_to_link_type_enum
CREATE CAST (VARCHAR AS link_type_enum) WITH INOUT AS IMPLICIT;
