DROP TABLE IF EXISTS LIKES;
DROP TABLE IF EXISTS FRIENDS;
DROP TABLE IF EXISTS USERS cascade;
DROP TABLE IF EXISTS FILMS_GENRES;
DROP TABLE IF EXISTS GENRES;
DROP TABLE IF EXISTS FILMS;
DROP TABLE IF EXISTS MPA;


create table IF NOT EXISTS MPA
(
    MPA_ID   INTEGER,
    MPA_NAME VARCHAR UNIQUE not null,
    constraint "MPA_pk"
        primary key (MPA_ID)
);

create table IF NOT EXISTS USERS
(
    USER_ID   INTEGER auto_increment,
    EMAIL     VARCHAR not null UNIQUE,
    LOGIN     VARCHAR not null UNIQUE,
    USER_NAME VARCHAR not null,
    BIRTHDAY  DATE    not null,
    constraint "USERS_pk"
        primary key (USER_ID)
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID       INTEGER NOT NULL,
    FRIEND_ID     INTEGER NOT NULL,
    FRIEND_STATUS boolean,
    CONSTRAINT "FRIENDS_pk"
        unique (FRIEND_ID, USER_ID),
    constraint "FRIENDS_USERS_USER_ID_USER_ID_fk"
        foreign key (USER_ID) references USERS,
    constraint "FRIENDS_USERS_FRIEND_ID_USER_ID_fk"
        foreign key (FRIEND_ID) references USERS (USER_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      INTEGER auto_increment,
    FILM_NAME    VARCHAR      not null,
    DESCRIPTION  VARCHAR(200) not null,
    RELEASE_DATE DATE         not null,
    DURATION     INTEGER      not null CHECK (DURATION > 0),
    MPA_ID       INTEGER      not null,
    constraint "FILMS_pk"
        primary key (FILM_ID),
    constraint "FILMS_MPA_MPA_ID_MPA_ID_fk"
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID   INTEGER,
    GENRE_NAME VARCHAR UNIQUE NOT NULL,
    constraint "GENRE_pk"
        primary key (GENRE_ID)
);
create table IF NOT EXISTS FILMS_GENRES
(
    FILMS_ID INTEGER NOT NULL,
    GENRE_ID INTEGER NOT NULL,
    CONSTRAINT "FILMS_GENRES_pk"
        unique (FILMS_ID, GENRE_ID),
    constraint "FILMS_GENRES_FILMS_FILM_ID_fk"
        foreign key (FILMS_ID) references FILMS,
    constraint "FILMS_GENRES_fk"
        foreign key (GENRE_ID) references GENRES
);


create table IF NOT EXISTS LIKES
(
    FILMS_ID INTEGER NOT NULL,
    USER_ID  INTEGER NOT NULL,
    CONSTRAINT "LIKES_pk"
        unique (USER_ID, FILMS_ID),
    constraint "LIKES_FILMS_FILM_ID_fk"
        foreign key (FILMS_ID) references FILMS,
    constraint "LIKES_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS


);
