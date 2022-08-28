-- liquibase formatted sql

-- changeSet serge:1
CREATE INDEX student_name_index ON student (name);

-- changeSet serge:2
CREATE INDEX faculty_name_and_color_index ON faculty (name, color);