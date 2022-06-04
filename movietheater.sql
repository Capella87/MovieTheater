use madang;

DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS seats;
DROP TABLE IF EXISTS schedules;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS theaters;
DROP TABLE IF EXISTS movies;



-- movies Table Create SQL
CREATE TABLE movies
(
    `movie_id`      INT            NOT NULL    AUTO_INCREMENT,
    `name`          VARCHAR(45)    NULL,
    `showtime`      VARCHAR(45)    NULL,
    `rating`        VARCHAR(45)    NULL,
    `director`      VARCHAR(45)    NULL,
    `actor`         VARCHAR(45)    NULL,
    `genre`         VARCHAR(45)    NULL,
    `info`          VARCHAR(45)    NULL,
    `release_date`  VARCHAR(45)    NULL,
     PRIMARY KEY (movie_id)
);


-- theaters Table Create SQL
CREATE TABLE theaters
(
    `theater_id`  INT            NOT NULL    AUTO_INCREMENT,
    `movie_id`    INT            NULL,
    `seat_count`  INT            NULL,
    `status`      VARCHAR(45)    NULL,
     PRIMARY KEY (theater_id)
);

ALTER TABLE theaters
    ADD CONSTRAINT FK_theaters_movie_id_movies_movie_id FOREIGN KEY (movie_id)
        REFERENCES movies (movie_id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- members Table Create SQL
CREATE TABLE members
(
    `member_id`  INT            NOT NULL    AUTO_INCREMENT,
    `username`   VARCHAR(45)    UNIQUE,
    `name`       VARCHAR(45)    NULL,
    `tel`        VARCHAR(45)    NULL,
    `email`      VARCHAR(45)    NULL,
     PRIMARY KEY (member_id, username)
);


-- reservations Table Create SQL
CREATE TABLE reservations
(
    `reservation_id`  INT            NOT NULL    AUTO_INCREMENT,
    `username`        VARCHAR(45)    NULL,
    `pay_method`      VARCHAR(45)    NULL,
    `pay_status`      VARCHAR(45)    NULL,
    `pay_price`       VARCHAR(45)    NULL,
    `pay_date`        VARCHAR(45)    NULL,
     PRIMARY KEY (reservation_id)
);

ALTER TABLE reservations
    ADD CONSTRAINT FK_reservations_username_members_username FOREIGN KEY (username)
        REFERENCES members (username) ON DELETE CASCADE ON UPDATE CASCADE;


-- seats Table Create SQL
CREATE TABLE seats
(
    `seat_id`     INT            NOT NULL    AUTO_INCREMENT,
    `theater_id`  INT            NULL,
    `status`      VARCHAR(45)    NULL,
     PRIMARY KEY (seat_id)
);

ALTER TABLE seats
    ADD CONSTRAINT FK_seats_theater_id_theaters_theater_id FOREIGN KEY (theater_id)
        REFERENCES theaters (theater_id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- schedules Table Create SQL
CREATE TABLE schedules
(
    `schedule_id`  INT            NOT NULL    AUTO_INCREMENT,
    `movie_id`     INT            NULL,
    `theater_id`   INT            NULL,
    `start_date`   VARCHAR(45)    NULL,
    `date`         VARCHAR(45)    NULL,
    `count`        VARCHAR(45)    NULL,
    `start_time`   VARCHAR(45)    NULL,
     PRIMARY KEY (schedule_id)
);

ALTER TABLE schedules
    ADD CONSTRAINT FK_schedules_movie_id_movies_movie_id FOREIGN KEY (movie_id)
        REFERENCES movies (movie_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE schedules
    ADD CONSTRAINT FK_schedules_theater_id_theaters_theater_id FOREIGN KEY (theater_id)
        REFERENCES theaters (theater_id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- tickets Table Create SQL
CREATE TABLE tickets
(
    `ticket_id`       INT            NOT NULL    AUTO_INCREMENT,
    `movie_id`        INT            NULL,
    `schedule_id`     INT            NULL,
    `theater_id`      INT            NULL,
    `seat_id`         INT            NULL,
    `reservation_id`  INT            NULL,
    `username`        VARCHAR(45)    NULL,
    `status`          VARCHAR(45)    NULL,
    `standard_price`  VARCHAR(45)    NULL,
    `sale_price`      VARCHAR(45)    NULL,
     PRIMARY KEY (ticket_id)
);

ALTER TABLE tickets
    ADD CONSTRAINT FK_tickets_schedule_id_schedules_schedule_id FOREIGN KEY (schedule_id)
        REFERENCES schedules (schedule_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tickets
    ADD CONSTRAINT FK_tickets_theater_id_theaters_theater_id FOREIGN KEY (theater_id)
        REFERENCES theaters (theater_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE tickets
    ADD CONSTRAINT FK_tickets_seat_id_seats_seat_id FOREIGN KEY (seat_id)
        REFERENCES seats (seat_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tickets
    ADD CONSTRAINT FK_tickets_reservation_id_reservations_reservation_id FOREIGN KEY (reservation_id)
        REFERENCES reservations (reservation_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tickets
    ADD CONSTRAINT FK_tickets_username_members_username FOREIGN KEY (username)
        REFERENCES members (username) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE tickets
    ADD CONSTRAINT FK_tickets_movie_id_movies_movie_id FOREIGN KEY (movie_id)
        REFERENCES movies (movie_id) ON DELETE RESTRICT ON UPDATE RESTRICT;
