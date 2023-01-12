CREATE TYPE role_enum AS ENUM ('USER','ADMIN');
CREATE CAST (varchar AS role_enum) WITH INOUT AS IMPLICIT;

CREATE TABLE users
(
    id        serial primary key ,
    firstname text,
    lastname  text,
    email     text,
    username  text,
    password  text,
    role      role_enum
)