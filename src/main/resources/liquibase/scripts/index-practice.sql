-- liquibase formatted sql

-- changeSet Serge:1
CREATE TABLE student(
                         id          bigserial             NOT NULL     PRIMARY KEY,
                         name        text                  NOT NULL,
                         age         int CHECK (age > 0)   NOT NULL,
                         faculty_id  int                   NOT NULL
);

-- changeSet Serge:2
CREATE TABLE faculty(
                        id     bigserial   NOT NULL     PRIMARY KEY ,
                        name   text        NOT NULL     UNIQUE,
                        color  text        NOT NULL     UNIQUE
);

-- changeSet Serge:3
CREATE TABLE avatar(
                        id          bigserial   NOT NULL    PRIMARY KEY,
                        file_path   text        NOT NULL    UNIQUE,
                        file_size   bigint      NOT NULL,
                        media_type  text        NOT NULL,
                        data        bytea       NOT NULL,
                        student_id  bigint      NOT NULL    UNIQUE
);

-- changeSet Serge:4
INSERT INTO student (id, name, age, faculty_id) VALUES (1, 'Gnom', 17, 1);
INSERT INTO student (id, name, age, faculty_id) VALUES (2, 'Gendalf', 21, 1);
INSERT INTO student (id, name, age, faculty_id) VALUES (3, 'Garry', 15, 2);
INSERT INTO student (id, name, age, faculty_id) VALUES (4, 'Elrond', 17, 2);
INSERT INTO student (id, name, age, faculty_id) VALUES (5, 'Kate', 18, 1);
INSERT INTO student (id, name, age, faculty_id) VALUES (6, 'Alisa', 22, 1);
INSERT INTO student (id, name, age, faculty_id) VALUES (7, 'Serge', 17, 2);
INSERT INTO student (id, name, age, faculty_id) VALUES (8, 'Helen', 19, 1);
INSERT INTO student (id, name, age, faculty_id) VALUES (9, 'Olga', 19, 1);

-- changeSet Serge:5
INSERT INTO faculty (id, name, color) VALUES (1, 'holly', 'white');
INSERT INTO faculty (id, name, color) VALUES (2, 'mag', 'red');

-- changeSet Serge:6
CREATE INDEX student_name_index ON student (name);

-- changeSet Serge:7
CREATE INDEX faculty_name_and_color_index ON faculty (name, color);