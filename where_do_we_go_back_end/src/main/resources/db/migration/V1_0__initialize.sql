CREATE SEQUENCE user_id_seq;
CREATE SEQUENCE role_id_seq;

CREATE TABLE public.users
(
    id       VARCHAR(32) PRIMARY KEY NOT NULL,
    email    VARCHAR(50)             NOT NULL,
    name     VARCHAR(50)             NOT NULL,
    password VARCHAR(100)            NOT NULL,
    username VARCHAR(50)             NOT NULL
);

CREATE TABLE public.roles
(
    id   VARCHAR(32) PRIMARY KEY NOT NULL,
    name VARCHAR(60)             NOT NULL
);

CREATE TABLE public.user_roles
(
    user_id VARCHAR(32) NOT NULL,
    role_id VARCHAR(32) NOT NULL
);

ALTER TABLE public.user_roles
    ADD CONSTRAINT FK_user_roles_to_users FOREIGN KEY (user_id) REFERENCES public.users (id);

ALTER TABLE public.user_roles
    ADD CONSTRAINT FK_user_roles_to_roles FOREIGN KEY (role_id) REFERENCES public.roles (id);
