DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users CASCADE ;
DROP SEQUENCE IF EXISTS global_seq CASCADE ;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    registered       TIMESTAMP           DEFAULT now() NOT NULL,
    enabled          BOOL                DEFAULT TRUE  NOT NULL,
    calories_per_day INTEGER             DEFAULT 2000  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS meals;

create table meals
(
    id          integer default nextval('global_seq'::regclass) not null
        constraint table_name_pk
            primary key,
    user_id     integer                                         not null
        constraint meals_users_id_fk
            references users
            on delete cascade,
    date_time   timestamp                                       not null,
    description varchar                                         not null,
    calories    integer default 0                               not null
);

alter table meals
    owner to "user";

create unique index meals_user_id_date_time_uindex
    on meals (user_id asc, date_time desc);