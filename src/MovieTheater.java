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

        String[] resetCommands = { "DROP TABLE IF EXISTS tickets;", "DROP TABLE IF EXISTS reservations;",
                "DROP TABLE IF EXISTS seats;", "DROP TABLE IF EXISTS seats;", "DROP TABLE IF EXISTS schedules;",
                "DROP TABLE IF EXISTS members;", "DROP TABLE IF EXISTS theaters;", "DROP TABLE IF EXISTS movies;",
                "CREATE TABLE movies\r\n" + "(\r\n"
                        + "    `movie_id`      INT            NOT NULL    AUTO_INCREMENT,\r\n"
                        + "    `name`          VARCHAR(45)    NULL,\r\n"
                        + "    `showtime`      VARCHAR(45)    NULL,\r\n"
                        + "    `rating`        VARCHAR(45)    NULL,\r\n"
                        + "    `director`      VARCHAR(45)    NULL,\r\n"
                        + "    `actor`         VARCHAR(45)    NULL,\r\n"
                        + "    `genre`         VARCHAR(45)    NULL,\r\n"
                        + "    `info`          VARCHAR(45)    NULL,\r\n"
                        + "    `release_date`  VARCHAR(45)    NULL,\r\n" + "     PRIMARY KEY (movie_id)\r\n" + ");",
                "CREATE TABLE theaters\r\n" + "(\r\n"
                        + "    `theater_id`  INT            NOT NULL    AUTO_INCREMENT,\r\n"
                        + "    `seat_count`  INT            NULL,\r\n" + "    `status`      VARCHAR(45)    NULL,\r\n"
                        + "     PRIMARY KEY (theater_id)\r\n" + ");",
                "CREATE TABLE members\r\n" + "(\r\n" + "    `member_id`  INT            NOT NULL    AUTO_INCREMENT,\r\n"
                        + "    `username`   VARCHAR(45)    NOT NULL    UNIQUE,\r\n"
                        + "    `name`       VARCHAR(45)    NULL,\r\n" + "    `tel`        VARCHAR(45)    NULL,\r\n"
                        + "    `email`      VARCHAR(45)    NULL,\r\n" + "     PRIMARY KEY (member_id, username)\r\n"
                        + ");",
                "CREATE TABLE reservations\r\n" + "(\r\n"
                        + "    `reservation_id`  INT            NOT NULL    AUTO_INCREMENT,\r\n"
                        + "    `pay_method`      VARCHAR(45)    NULL,\r\n"
                        + "    `pay_status`      VARCHAR(45)    NULL,\r\n"
                        + "    `pay_price`       VARCHAR(45)    NULL,\r\n"
                        + "    `username`        VARCHAR(45)    NOT NULL,\r\n"
                        + "    `pay_date`        VARCHAR(45)    NULL,\r\n" + "     PRIMARY KEY (reservation_id)\r\n"
                        + ");",
                "ALTER TABLE reservations\r\n"
                        + "    ADD CONSTRAINT FK_reservations_username_members_username FOREIGN KEY (username)\r\n"
                        + "        REFERENCES members (username) ON DELETE RESTRICT ON UPDATE RESTRICT;",

                "CREATE TABLE schedules\r\n" + "(\r\n"
                        + "    `schedule_id`  INT            NOT NULL    AUTO_INCREMENT,\r\n"
                        + "    `movie_id`     INT            NULL,\r\n" + "    `theater_id`   INT            NULL,\r\n"
                        + "    `start_date`   VARCHAR(45)    NULL,\r\n" + "    `date`         VARCHAR(45)    NULL,\r\n"
                        + "    `count`        VARCHAR(45)    NULL,\r\n" + "    `start_time`   VARCHAR(45)    NULL,\r\n"
                        + "     PRIMARY KEY (schedule_id)\r\n" + ");",
                "ALTER TABLE schedules\r\n"
                        + "    ADD CONSTRAINT FK_schedules_movie_id_movies_movie_id FOREIGN KEY (movie_id)\r\n"
                        + "        REFERENCES movies (movie_id) ON DELETE RESTRICT ON UPDATE RESTRICT;",
                "ALTER TABLE schedules\r\n"
                        + "    ADD CONSTRAINT FK_schedules_theater_id_theaters_theater_id FOREIGN KEY (theater_id)\r\n"
                        + "        REFERENCES theaters (theater_id) ON DELETE RESTRICT ON UPDATE RESTRICT;",

                "CREATE TABLE seats\r\n" + "(\r\n" + "    `seat_id`     INT            NOT NULL    AUTO_INCREMENT,\r\n"
                        + "    `theater_id`  INT            NULL,\r\n" + "    `status`      VARCHAR(45)    NULL,\r\n"
                        + "     PRIMARY KEY (seat_id)\r\n" + ");",
                "ALTER TABLE seats\r\n"
                        + "    ADD CONSTRAINT FK_seats_theater_id_theaters_theater_id FOREIGN KEY (theater_id)\r\n"
                        + "        REFERENCES theaters (theater_id) ON DELETE RESTRICT ON UPDATE RESTRICT;",
                "CREATE TABLE tickets\r\n" + "(\r\n"
                        + "    `ticket_id`       INT            NOT NULL    AUTO_INCREMENT,\r\n"
                        + "    `date_num`        VARCHAR(45)    NULL,\r\n"
                        + "    `theater_id`      INT            NULL,\r\n"
                        + "    `seat_id`         INT            NULL,\r\n"
                        + "    `reservation_id`  INT            NULL,\r\n"
                        + "    `status`          VARCHAR(45)    NULL,\r\n"
                        + "    `standard_price`  VARCHAR(45)    NULL,\r\n"
                        + "    `sale_price`      VARCHAR(45)    NULL,\r\n" + "     PRIMARY KEY (ticket_id)\r\n" + ");",
                "ALTER TABLE tickets\r\n"
                        + "    ADD CONSTRAINT FK_tickets_theater_id_theaters_theater_id FOREIGN KEY (theater_id)\r\n"
                        + "        REFERENCES theaters (theater_id) ON DELETE RESTRICT ON UPDATE RESTRICT;",

                "ALTER TABLE tickets\r\n"
                        + "    ADD CONSTRAINT FK_tickets_seat_id_seats_seat_id FOREIGN KEY (seat_id)\r\n"
                        + "        REFERENCES seats (seat_id) ON DELETE RESTRICT ON UPDATE RESTRICT;\r\n",
                "ALTER TABLE tickets\r\n"
                        + "    ADD CONSTRAINT FK_tickets_reservation_id_reservations_reservation_id FOREIGN KEY (reservation_id)\r\n"
                        + "        REFERENCES reservations (reservation_id) ON DELETE RESTRICT ON UPDATE RESTRICT;" };
        try {
            Statement resetStatement = this.con.createStatement();
            for (int i = 0; i < resetCommands.length; i++)
                resetStatement.executeUpdate(resetCommands[i]);
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
    }
}
