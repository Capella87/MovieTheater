import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.sql.*;
import java.util.*;

class Database {
	Connection con;

	public Database() {
		try {
			openCon();
		} catch (Exception e) {
			System.out.println("데이터베이스에 연결할 수 없습니다.");
			con = null;
			e.printStackTrace();
		}

	}

	void openCon() throws Exception {
		String Driver = "";

		// 접속변수를 초기화한다. url은 자바 드라이버 이름, 호스트명(localhost), 포트번호, database 이름을 입력
		String url = "jdbc:mysql://127.0.0.1:3306/madang?&serverTimezone=Asia/Seoul";

		// userid는 관리자(madang), pwd는 사용자의 비밀번호(madang)를 입력
		String userid = "madang";
		String pwd = "madang";

		try { // 드라이버를 찾는 과정
			Class.forName("com.mysql.cj.jdbc.Driver"); // Class.forName()으로 드라이버를 로딩한다. 드라이버 이름을 Class.forName에 입력
			System.out.println("드라이버 로드 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try { // 데이터베이스를 연결하는 과정
			System.out.println("데이터베이스 연결 준비...");
			con = DriverManager.getConnection(url, userid, pwd); // 접속 객체 con을 DriverManager.getConnection 함수로 생성한다.
			System.out.println("데이터베이스 연결 성공"); // 접속이 성공하면 "데이터베이스 연결 성공"을 출력하도록 한다.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	boolean resetDatabase() {
		try {
			openCon();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		String[] resetCommands = { "DROP TABLE IF EXISTS tickets;\n",

				"DROP TABLE IF EXISTS reservations;\n",

				"DROP TABLE IF EXISTS seats;\n",

				"DROP TABLE IF EXISTS schedules;\n",

				"DROP TABLE IF EXISTS members;\n",

				"DROP TABLE IF EXISTS theaters;\n",

				"DROP TABLE IF EXISTS movies;\n",

				"CREATE TABLE movies\n" + "(\n" + "    `movie_id`      INT            NOT NULL    AUTO_INCREMENT,\n"
						+ "    `name`          VARCHAR(45)    NULL,\n" + "    `showtime`      VARCHAR(45)    NULL,\n"
						+ "    `rating`        VARCHAR(45)    NULL,\n" + "    `director`      VARCHAR(45)    NULL,\n"
						+ "    `actor`         VARCHAR(45)    NULL,\n" + "    `genre`         VARCHAR(45)    NULL,\n"
						+ "    `info`          VARCHAR(45)    NULL,\n" + "    `release_date`  VARCHAR(45)    NULL,\n"
						+ "     PRIMARY KEY (movie_id)\n" + ");\n",

				"CREATE TABLE theaters\n" + "(\n" + "    `theater_id`  INT            NOT NULL    AUTO_INCREMENT,\n"
						+ "    `movie_id`    INT            NULL,\n" + "    `seat_count`  INT            NULL,\n"
						+ "    `status`      VARCHAR(45)    NULL,\n" + "     PRIMARY KEY (theater_id)\n" + ");\n",
				"ALTER TABLE theaters\n"
						+ "    ADD CONSTRAINT FK_theaters_movie_id_movies_movie_id FOREIGN KEY (movie_id)\n"
						+ "        REFERENCES movies (movie_id) ON DELETE cascade ON UPDATE cascade;\n",

				"CREATE TABLE members\n" + "(\n" + "    `member_id`  INT            NOT NULL    AUTO_INCREMENT,\n"
						+ "    `username`   VARCHAR(45)    UNIQUE,\n" + "    `name`       VARCHAR(45)    NULL,\n"
						+ "    `tel`        VARCHAR(45)    NULL,\n" + "    `email`      VARCHAR(45)    NULL,\n"
						+ "     PRIMARY KEY (member_id, username)\n" + ");\n",

				"CREATE TABLE reservations\n" + "(\n"
						+ "    `reservation_id`  INT            NOT NULL    AUTO_INCREMENT,\n"
						+ "    `username`        VARCHAR(45)    NULL,\n"
						+ "    `pay_method`      VARCHAR(45)    NULL,\n"
						+ "    `pay_status`      VARCHAR(45)    NULL,\n"
						+ "    `pay_price`       VARCHAR(45)    NULL,\n"
						+ "    `pay_date`        VARCHAR(45)    NULL,\n" + "     PRIMARY KEY (reservation_id)\n"
						+ ");\n",

				"ALTER TABLE reservations\n"
						+ "    ADD CONSTRAINT FK_reservations_username_members_username FOREIGN KEY (username)\n"
						+ "        REFERENCES members (username) ON DELETE CASCADE ON UPDATE CASCADE;\n",

				"CREATE TABLE seats\n" + "(\n" + "    `seat_id`     INT            NOT NULL    AUTO_INCREMENT,\n"
						+ "    `theater_id`  INT            NULL,\n" + "    `status`      VARCHAR(45)    NULL,\n"
						+ "     PRIMARY KEY (seat_id)\n" + ");\n",

				"ALTER TABLE seats\n"
						+ "    ADD CONSTRAINT FK_seats_theater_id_theaters_theater_id FOREIGN KEY (theater_id)\n"
						+ "        REFERENCES theaters (theater_id) ON DELETE CASCADE ON UPDATE CASCADE;\n",

				"CREATE TABLE schedules\n" + "(\n" + "    `schedule_id`  INT            NOT NULL    AUTO_INCREMENT,\n"
						+ "    `movie_id`     INT            NULL,\n" + "    `theater_id`   INT            NULL,\n"
						+ "    `start_date`   VARCHAR(45)    NULL,\n" + "    `date`         VARCHAR(45)    NULL,\n"
						+ "    `count`        VARCHAR(45)    NULL,\n" + "    `start_time`   VARCHAR(45)    NULL,\n"
						+ "     PRIMARY KEY (schedule_id)\n" + ");\n",
				"ALTER TABLE schedules\n"
						+ "    ADD CONSTRAINT FK_schedules_movie_id_movies_movie_id FOREIGN KEY (movie_id)\n"
						+ "        REFERENCES movies (movie_id) ON DELETE CASCADE ON UPDATE CASCADE;\n" + "\n",
				"ALTER TABLE schedules\n"
						+ "    ADD CONSTRAINT FK_schedules_theater_id_theaters_theater_id FOREIGN KEY (theater_id)\n"
						+ "        REFERENCES theaters (theater_id) ON DELETE RESTRICT ON UPDATE RESTRICT;\n",

				"CREATE TABLE tickets\n" + "(\n" + "    `ticket_id`       INT            NOT NULL    AUTO_INCREMENT,\n"
						+ "    `movie_id`        INT            NULL,\n"
						+ "    `schedule_id`     INT            NULL,\n"
						+ "    `theater_id`      INT            NULL,\n"
						+ "    `seat_id`         INT            NULL,\n"
						+ "    `reservation_id`  INT            NULL,\n"
						+ "    `username`        VARCHAR(45)    NULL,\n"
						+ "    `status`          VARCHAR(45)    NULL,\n"
						+ "    `standard_price`  VARCHAR(45)    NULL,\n"
						+ "    `sale_price`      VARCHAR(45)    NULL,\n" + "     PRIMARY KEY (ticket_id)\n" + ");\n",

				"ALTER TABLE tickets\n"
						+ "    ADD CONSTRAINT FK_tickets_schedule_id_schedules_schedule_id FOREIGN KEY (schedule_id)\n"
						+ "        REFERENCES schedules (schedule_id) ON DELETE CASCADE ON UPDATE CASCADE;\n",
				"ALTER TABLE tickets\n"
						+ "    ADD CONSTRAINT FK_tickets_theater_id_theaters_theater_id FOREIGN KEY (theater_id)\n"
						+ "        REFERENCES theaters (theater_id) ON DELETE RESTRICT ON UPDATE RESTRICT;\n",
				"ALTER TABLE tickets\n" + "    ADD CONSTRAINT FK_tickets_seat_id_seats_seat_id FOREIGN KEY (seat_id)\n"
						+ "        REFERENCES seats (seat_id) ON DELETE CASCADE ON UPDATE CASCADE;\n",
				"ALTER TABLE tickets\n"
						+ "    ADD CONSTRAINT FK_tickets_reservation_id_reservations_reservation_id FOREIGN KEY (reservation_id)\n"
						+ "        REFERENCES reservations (reservation_id) ON DELETE CASCADE ON UPDATE CASCADE;\n",
				"ALTER TABLE tickets\n"
						+ "    ADD CONSTRAINT FK_tickets_username_members_username FOREIGN KEY (username)\n"
						+ "        REFERENCES members (username) ON DELETE RESTRICT ON UPDATE RESTRICT;\n",
				"ALTER TABLE tickets\n"
						+ "    ADD CONSTRAINT FK_tickets_movie_id_movies_movie_id FOREIGN KEY (movie_id)\n"
						+ "        REFERENCES movies (movie_id) ON DELETE RESTRICT ON UPDATE RESTRICT;\n" };

		String[] InsertQuery = {
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('gildong', '홍길동', '1', 'asdf')\n",
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('top', '채하나', '1', 'asdf')\n",
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('route', '박아홉', '1', 'asdf')\n",
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('parkone', '박하나', '1', 'asdsad')\n",
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('leetwo', '이이', '1', 'asdf')\n",
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('parkthree', '박셋', '1', 'asdf')\n",
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('leefour', '이넷', '1', 'dasdasad')\n",
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('parkfive', '박오', '1', 'asds')\n",
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('leesix', '이육', '1', 'asdsasf')\n",
				"INSERT INTO madang.members (username, name, tel, email) VALUES ('leeseven', '이칠', '1', 'asdf')\n",

				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('기생충', '132', '15', '봉준호', '송강호', '스릴러', '1', '2021.01');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('괴물', '119', '15', '봉준호', '송강호', '스릴러', '1', '2021.02');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('시간을 달리는 소녀', '5', '5', '호소다 마모루', '나카 리이사', '애니메이션', '1', '2021.03');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('토이 스토리 3', '102', '5', '존 라세터', '톰 행크스', '애니메이션', '1', '2021.04');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('토이 스토리 2', '95', '5', '존 라세터', '톰 행크스', '애니메이션', '1', '2021.05');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('살인의 추억', '132', '195', '봉준호', '송강호', '스릴러', '1', '2021.06');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('범죄도시', '121', '19', '강윤성', '마동석', '스릴러', '1', '2021.07');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('타이타닉', '194', '15', '제임스 카메론', '케이트 윈슬렛', '로맨스', '1', '2021.08');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('레옹', '132', '19', '뤽 베송', '장 르노', '범죄', '1', '2021.09');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('인셉션', '147', '12', '크리스토퍼 놀란', '디카프리오', '액션', '1', '2021.10');\n",
				"INSERT INTO madang.movies (name, showtime, rating, director, actor, genre, info, release_date) VALUES ('추격자', '123', '19', '나홍진', '하정우', '스릴러', '1', '2021.11');\n",

				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (1, 5, 'true');\n",
				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (2, 5, 'true');\n",
				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (3, 5, 'true');\n",
				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (4, 5, 'true');\n",
				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (5, 5, 'true');\n",
				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (6, 5, 'true');\n",
				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (7, 5, 'true');\n",
				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (8, 5, 'true');\n",
				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (9, 5, 'true');\n",
				"INSERT INTO madang.theaters (movie_id, seat_count, status) VALUES (10, 5, 'true');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (1, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (1, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (1, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (1, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (1, 'false');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (2, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (2, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (2, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (2, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (2, 'false');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (3, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (3, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (3, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (3, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (3, 'false');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (4, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (4, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (4, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (4, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (4, 'false');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (5, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (5, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (5, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (5, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (5, 'false');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (6, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (6, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (6, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (6, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (6, 'false');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (7, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (7, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (7, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (7, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (7, 'false');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (8, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (8, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (8, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (8, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (8, 'false');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (9, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (9, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (9, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (9, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (9, 'false');\n",

				"INSERT INTO madang.seats (theater_id, status) VALUES (10, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (10, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (10, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (10, 'false');\n",
				"INSERT INTO madang.seats (theater_id, status) VALUES (10, 'false');\n",

				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('gildong', '카카오페이', 'Y', '10000', '2022-06-01');\n",
				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('gildong', '카카오페이', 'Y', '10000', '2022-06-01');\n",
				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('gildong', '카카오페이', 'Y', '10000', '2022-06-01');\n",
				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('route', '카카오페이', 'Y', '10000', '2022-06-01');\n",
				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('route', '카카오페이', 'Y', '10000', '2022-06-01');\n",
				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('parkone', '카카오페이', 'Y', '10000', '2022-06-01');\n",
				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('leetwo', '카카오페이', 'Y', '10000', '2022-06-01');\n",
				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('parkthree', '카카오페이', 'Y', '10000', '2022-06-01');\n",
				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('leefour', '카카오페이', 'Y', '10000', '2022-06-01');\n",
				"INSERT INTO madang.reservations (username, pay_method, pay_status, pay_price, pay_date) VALUES ('parkfive', '카카오페이', 'Y', '10000', '2022-06-01');\n",

				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (1, 1, '2022-06-22', '2022-06-22', '1', '10:00');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (2, 2, '2022-06-22', '2022-06-22', '1', '11:00');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (3, 3, '2022-06-22', '2022-06-22', '1', '12:00');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (4, 4, '2022-06-22', '2022-06-22', '2', '10:30');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (5, 5, '2022-06-22', '2022-06-22', '3', '10:45');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (6, 6, '2022-06-22', '2022-06-23', '4', '10:50');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (7, 7, '2022-06-22', '2022-06-23', '1', '10:10');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (8, 8, '2022-06-22', '2022-06-23', '2', '10:20');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (9, 9, '2022-06-22', '2022-06-23', '2', '13:00');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (10,10, '2022-06-22', '2022-06-23', '2', '14:00');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (1, 1, '2022-06-22', '2022-06-24', '1', '10:00');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (2, 2, '2022-06-22', '2022-06-24', '1', '11:00');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (3, 3, '2022-06-22', '2022-06-25', '1', '12:00');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (4, 4, '2022-06-22', '2022-06-25', '2', '10:30');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (5, 5, '2022-06-22', '2022-06-26', '3', '10:45');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (6, 6, '2022-06-22', '2022-06-26', '4', '10:50');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (7, 7, '2022-06-22', '2022-06-27', '1', '10:10');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (8, 8, '2022-06-22', '2022-06-27', '2', '10:20');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (9, 9, '2022-06-22', '2022-06-28', '2', '13:00');\n",
				"INSERT INTO madang.schedules (movie_id, theater_id, start_date, date, count, start_time) VALUES (10,10, '2022-06-22', '2022-06-29', '2', '14:00');\n",

				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (1, 1, 1, 1, 1, 'gildong', 'Y', '10000', '10000');\n",
				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (2, 2, 2, 6, 2, 'gildong', 'Y', '10000', '10000');\n",
				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (3, 3, 3, 11, 3, 'gildong', 'Y', '10000', '10000');\n",
				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (4, 4, 4, 16, 4, 'route', 'Y', '10000', '10000');\n",
				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (5, 5, 5, 21, 5, 'route', 'Y', '10000', '10000');\n",
				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (6, 6, 6, 26, 6, 'parkone', 'Y', '10000', '10000');\n",
				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (7, 7, 7, 31, 7, 'leetwo', 'Y', '10000', '10000');\n",
				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (8, 8, 8, 36, 8, 'parkthree', 'Y', '10000', '10000');\n",
				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (9, 9, 9, 41, 9, 'leefour', 'Y', '10000', '10000');\n",
				"INSERT INTO madang.tickets (movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) VALUES (10, 10, 10, 46, 10, 'parkfive', 'Y', '10000', '10000');" };

		try {
			Statement resetStatement = this.con.createStatement();
			PreparedStatement pstmt = null;

			// Table 생성
			for (int i = 0; i < resetCommands.length; i++)
				resetStatement.executeUpdate(resetCommands[i]);

			// Sample Data 생성
			for (int i = 0; i < InsertQuery.length; i++) {
				pstmt = this.con.prepareStatement(InsertQuery[i]);
				pstmt.executeUpdate();
			}
			

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}

public class MovieTheater {

	private JFrame frame;
	private static Database db;
	private Administrator admin;
	private Customer customer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					db = new Database();

					var window = new MovieTheater();
					window.frame.setVisible(true);
				} catch (Exception e) {
					System.out.println("Error!");
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MovieTheater() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("영화관");
		frame.getContentPane().setFont(new Font("Malgun Gothic", Font.BOLD, 20));
		frame.setBounds(100, 100, 737, 496);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel = new JLabel("세종대학교 영화관");
		lblNewLabel.setFont(new Font("Gulim", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblNewLabel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JButton btnNewButton = new JButton("회원");
		btnNewButton.setFont(new Font("Gulim", Font.BOLD, 20));
		btnNewButton.setBounds(387, 162, 186, 63);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				customer = new Customer(db);
				customer.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
				customer.setVisible(true);
				customer.toFront();
				customer.requestFocus();
				customer.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			}
		});
		panel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("관리자");
		btnNewButton_1.setFont(new Font("Gulim", Font.BOLD, 20));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				admin = new Administrator(db);
				admin.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
				admin.setVisible(true);
				admin.toFront();
				admin.requestFocus();
			}
		});
		btnNewButton_1.setBounds(128, 162, 186, 63);
		panel.add(btnNewButton_1);

		JLabel welcomeLabel = new JLabel("세종대학교 영화관에 오신 것을 환영합니다.");
		welcomeLabel.setFont(new Font("Gulim", Font.BOLD, 16));
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setBounds(184, 83, 331, 35);
		panel.add(welcomeLabel);

		JButton btnNewButton_2 = new JButton("Credit");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				var credit = new Credit();
				credit.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
				credit.setVisible(true);
				credit.toFront();
				credit.requestFocus();
			}
		});
		btnNewButton_2.setBounds(306, 355, 98, 28);
		panel.add(btnNewButton_2);
	}
}
