import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.CardLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JTextField;
import java.awt.Dimension;

class FailedToLoginException extends Exception {
    FailedToLoginException(String errorMessage) {
        super(errorMessage);
    }
}

class FailedToPayException extends Exception {
    FailedToPayException(String errorMessage) {
        super(errorMessage);
    }
}

class FailedToBookException extends Exception {
    FailedToBookException(String errorMessage) {
        super(errorMessage);
    }
}

class EmptyFieldException extends Exception {
    EmptyFieldException(String errorMessage) {
        super(errorMessage);
    }
}

class DuplicateAccountException extends Exception {
    DuplicateAccountException(String errorMessage) {
        super(errorMessage);
    }
}

class FailedToCreateAccount extends Exception {
    FailedToCreateAccount(String errorMessage) {
        super(errorMessage);
    }
}

public class SelectSchedule extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private Object[] target;
    private int targetColumnCount;
    private ArrayList<Integer> scheduleIds;
    private Database db;
    private JTable resultTable;
    private int movieId;
    private JScrollPane scrollPane;
    private JLabel subtitle;
    private JPanel titlePanel;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private CardLayout mainLayout;
    private JPanel infoPanel;
    private String username;

    private int ticketId;

    private Object[] selectedSchedule;

    public SelectSchedule(Object[] targetToReserve, Database db, int movieId) {
        this.db = db;
        target = targetToReserve;
        targetColumnCount = target.length;

        setBounds(100, 100, 451, 409);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        mainLayout = new CardLayout(0, 0);
        contentPanel.setLayout(mainLayout);
        {
            JPanel selectSchedule = new JPanel();
            contentPanel.add(selectSchedule, "selectSchedule");
            selectSchedule.setLayout(new BorderLayout(0, 0));
            setTitle("예매");
            scrollPane = new JScrollPane();
            selectSchedule.add(scrollPane, BorderLayout.CENTER);

            JLabel lblNewLabel = null;
            try {
                var statement = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                StringBuilder getTitle = new StringBuilder("SELECT name FROM movies WHERE movie_id = ");
                getTitle.append(movieId);
                System.out.println(getTitle.toString());
                ResultSet result2 = statement.executeQuery(getTitle.toString());
                result2.next();
                lblNewLabel = new JLabel("예매할 영화 시간 - " + result2.getString(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            infoPanel = new JPanel();
            infoPanel.setLayout(null);

            lblNewLabel.setFont(new Font("Gulim", Font.BOLD, 16));
            lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            selectSchedule.add(lblNewLabel, BorderLayout.NORTH);
            // target에서 영화 id로 시간을 조회해서 Table로 출력한다.
            try {
                var statement = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                var searchQuery = new StringBuilder(
                        "SELECT theater_id as '관', date as '날짜', count as '회차', start_time as '시간',");
                searchQuery.append(
                        " ((SELECT seat_count FROM theaters WHERE theaters.theater_id = s.theater_id) - (SELECT count(*) FROM tickets t WHERE t.schedule_id = s.schedule_id)) as '잔여 좌석', ");
                searchQuery.append(" schedule_id FROM schedules as s ");
                searchQuery.append("WHERE movie_id = ");
                searchQuery.append(movieId);

                System.out.println(searchQuery.toString());
                ResultSet result = statement.executeQuery(searchQuery.toString());
                int resultCount = 0;
                while (result.next()) {
                    resultCount++;
                }

                ResultSetMetaData columns = result.getMetaData();
                int columnCount = columns.getColumnCount();
                var resultModel = new DefaultTableModel(resultCount, 0);
                for (int i = 1; i <= columnCount - 1; i++) {
                    resultModel.addColumn(columns.getColumnName(i));
                }

                result.first();
                scheduleIds = new ArrayList<Integer>();
                for (int i = 0; i < resultCount; i++) {
                    for (int j = 1; j <= columnCount - 1; j++) {
                        resultModel.setValueAt(result.getString(j), i, j - 1);
                    }
                    scheduleIds.add(result.getInt(columnCount)); // schedule_id를 별도의 리스트에 추가
                    result.next();
                }

                var wrapperModel = new TableResult(resultModel, "선택");
                resultTable = new JTable(wrapperModel);
                scrollPane.setViewportView(resultTable);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "SQL 쿼리 오류", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                dispose();
            }

            resultTable.setVisible(true);

        }
        {
            JPanel login = new JPanel();
            contentPanel.add(login, "login");
            login.setLayout(new BorderLayout(0, 0));
            {
                JLabel lblNewLabel_1 = new JLabel("로그인");
                lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel_1.setFont(new Font("Gulim", Font.BOLD, 15));
                login.add(lblNewLabel_1, BorderLayout.NORTH);
            }
            {
                JPanel loginPanel = new JPanel();
                login.add(loginPanel, BorderLayout.CENTER);
                loginPanel.setLayout(null);
                {
                    JLabel lblNewLabel_2 = new JLabel("Username: ");
                    lblNewLabel_2.setBounds(36, 9, 82, 14);
                    loginPanel.add(lblNewLabel_2);
                }
                {
                    textField = new JTextField();
                    textField.setBounds(123, 6, 211, 23);
                    textField.setMinimumSize(new Dimension(100, 20));
                    textField.setPreferredSize(new Dimension(100, 20));
                    loginPanel.add(textField);
                    textField.setColumns(10);
                }

                JButton btnNewButton = new JButton("로그인");
                btnNewButton.setBounds(170, 40, 95, 23);
                btnNewButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            var loginQuery = new StringBuilder("SELECT member_id FROM members WHERE username = \'");
                            username = textField.getText();
                            textField.setText("입력 완료");
                            if (username.equals("") || username == null) {
                                JOptionPane.showMessageDialog(null, "Username을 입력해 주세요.", "ERROR",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            loginQuery.append(username + "\'");
                            var statement = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            System.out.println(loginQuery.toString());
                            var tryLogin = statement.executeQuery(loginQuery.toString());

                            if (!tryLogin.next()) {
                                throw new FailedToLoginException("없는 계정입니다. 아래 패널에서 회원가입을 하세요.");
                            } else {
                                // 결제화면으로 이동
                                mainLayout.next(contentPanel);
                            }
                        } catch (FailedToLoginException err) {
                            JOptionPane.showMessageDialog(null, err.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                            err.printStackTrace();
                            return;
                        } catch (SQLException err) {
                            JOptionPane.showMessageDialog(null, "SQL문 실행 오류", "ERROR", JOptionPane.ERROR_MESSAGE);
                            err.printStackTrace();
                            return;
                        }
                    }
                });
                loginPanel.add(btnNewButton);

                JLabel lblNewLabel_2 = new JLabel("Username: ");
                lblNewLabel_2.setBounds(36, 113, 82, 14);
                loginPanel.add(lblNewLabel_2);

                textField_1 = new JTextField();
                textField_1.setPreferredSize(new Dimension(100, 20));
                textField_1.setMinimumSize(new Dimension(100, 20));
                textField_1.setColumns(10);
                textField_1.setBounds(123, 110, 211, 23);
                loginPanel.add(textField_1);

                JLabel lblNewLabel_1 = new JLabel("회원가입");
                lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel_1.setFont(new Font("Gulim", Font.BOLD, 15));
                lblNewLabel_1.setBounds(0, 84, 425, 18);
                loginPanel.add(lblNewLabel_1);

                JLabel lblNewLabel_2_1 = new JLabel("이름: ");
                lblNewLabel_2_1.setBounds(36, 141, 82, 14);
                loginPanel.add(lblNewLabel_2_1);

                textField_2 = new JTextField();
                textField_2.setPreferredSize(new Dimension(100, 20));
                textField_2.setMinimumSize(new Dimension(100, 20));
                textField_2.setColumns(10);
                textField_2.setBounds(123, 138, 211, 23);
                loginPanel.add(textField_2);

                JLabel lblNewLabel_2_1_1 = new JLabel("전화번호:");
                lblNewLabel_2_1_1.setBounds(36, 169, 82, 14);
                loginPanel.add(lblNewLabel_2_1_1);

                textField_3 = new JTextField();
                textField_3.setPreferredSize(new Dimension(100, 20));
                textField_3.setMinimumSize(new Dimension(100, 20));
                textField_3.setColumns(10);
                textField_3.setBounds(123, 166, 211, 23);
                loginPanel.add(textField_3);

                JLabel lblNewLabel_2_1_1_1 = new JLabel("이메일:");
                lblNewLabel_2_1_1_1.setBounds(36, 197, 82, 14);
                loginPanel.add(lblNewLabel_2_1_1_1);

                textField_4 = new JTextField();
                textField_4.setPreferredSize(new Dimension(100, 20));
                textField_4.setMinimumSize(new Dimension(100, 20));
                textField_4.setColumns(10);
                textField_4.setBounds(123, 194, 211, 23);
                loginPanel.add(textField_4);

                JButton btnNewButton_1 = new JButton("회원가입");
                btnNewButton_1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String newUsername = textField_1.getText();
                            if (newUsername.equals(""))
                                throw new EmptyFieldException("Username이 비어 있습니다.");
                            textField_1.setText("");

                            String name = textField_2.getText();
                            if (name.equals(""))
                                throw new EmptyFieldException("이름이 비어 있습니다.");
                            textField_2.setText("");

                            String tel = textField_3.getText();
                            if (tel.equals(""))
                                throw new EmptyFieldException("전화번호가 비어 있습니다.");
                            textField_3.setText("");

                            String email = textField_4.getText();
                            if (email.equals(""))
                                throw new EmptyFieldException("이메일이 비어 있습니다.");
                            textField_4.setText("");

                            var checkDuplicate = new StringBuilder("SELECT member_id FROM members WHERE username = \'");
                            checkDuplicate.append(newUsername);
                            checkDuplicate.append("\'");

                            var statement = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            System.out.println(checkDuplicate.toString());
                            var isDuplicate = statement.executeQuery(checkDuplicate.toString());
                            if (isDuplicate.next()) {
                                throw new DuplicateAccountException("이미 있는 계정입니다. 로그인해 주세요.");
                            }

                            var createAccount = new StringBuilder("INSERT INTO members(");
                            createAccount.append("username, name, tel, email) VALUES(");
                            createAccount.append("\'" + newUsername + "\', ");
                            createAccount.append("\'" + name + "\', ");
                            createAccount.append("\'" + tel + "\', ");
                            createAccount.append("\'" + email + "\'");
                            createAccount.append(")");

                            System.out.println(createAccount.toString());
                            var isInserted = statement.executeUpdate(createAccount.toString());
                            if (isInserted < 1)
                                throw new FailedToCreateAccount("회원가입 실패");
                            JOptionPane.showMessageDialog(null, "회원가입 성공", "알림", JOptionPane.INFORMATION_MESSAGE);
                        } catch (FailedToCreateAccount err) {
                            JOptionPane.showMessageDialog(null, err.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                            err.printStackTrace();
                        } catch (DuplicateAccountException err) {
                            JOptionPane.showMessageDialog(null, err.getMessage(), "안내",
                                    JOptionPane.INFORMATION_MESSAGE);
                            err.printStackTrace();
                        } catch (EmptyFieldException err) {
                            JOptionPane.showMessageDialog(null, err.getMessage(), "안내",
                                    JOptionPane.INFORMATION_MESSAGE);
                            err.printStackTrace();
                        } catch (SQLException err) {

                        }
                    }
                });
                btnNewButton_1.setBounds(170, 228, 95, 23);
                loginPanel.add(btnNewButton_1);
            }
        }
        {
            JPanel paymentPanel = new JPanel();
            contentPanel.add(paymentPanel, "payment");
            paymentPanel.setLayout(new BorderLayout(0, 0));

            JPanel panel = new JPanel();
            paymentPanel.add(panel, BorderLayout.CENTER);
            panel.setLayout(new BorderLayout(0, 0));

            JLabel lblNewLabel_3 = new JLabel("결제 방법은 카카오페이만 지원합니다.");
            lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_3.setVerticalAlignment(SwingConstants.TOP);
            lblNewLabel_3.setFont(new Font("Gulim", Font.BOLD, 15));
            panel.add(lblNewLabel_3, BorderLayout.NORTH);

            JPanel panel_1 = new JPanel();
            panel.add(panel_1, BorderLayout.CENTER);
            panel_1.setLayout(null);

            JLabel lblNewLabel_4 = new JLabel("결제 금액: 10000KRW");
            lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_4.setFont(new Font("Gulim", Font.BOLD, 11));
            lblNewLabel_4.setBounds(138, 54, 125, 23);
            panel_1.add(lblNewLabel_4);

            JLabel lblNewLabel_4_1 = new JLabel("밑에 있는 확인 버튼을 누르면");
            lblNewLabel_4_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_4_1.setBounds(99, 88, 208, 23);
            panel_1.add(lblNewLabel_4_1);

            JLabel lblNewLabel_4_1_1 = new JLabel(" 자동 결제 및 자동으로 좌석이 배정됩니다.");
            lblNewLabel_4_1_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_4_1_1.setBounds(63, 122, 274, 23);
            panel_1.add(lblNewLabel_4_1_1);

            JButton btnNewButton_2 = new JButton("결제");
            btnNewButton_2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        var paymentQuery = new StringBuilder("INSERT INTO reservations ");
                        paymentQuery.append("(username, pay_method, pay_status, pay_price, pay_date) VALUES (");
                        paymentQuery.append("\'");
                        paymentQuery.append(username);
                        paymentQuery.append("\', \'카카오페이\', \'Y\', \'10000\', \'2021-01-01\')");

                        System.out.println(paymentQuery.toString());
                        var statement = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);
                        System.out.println(paymentQuery.toString());
                        var isSuccessed = statement.executeUpdate(paymentQuery.toString(),
                                statement.RETURN_GENERATED_KEYS);
                        var lastInserted = statement.getGeneratedKeys();
                        if (isSuccessed < 1 || !lastInserted.next())
                            throw new FailedToPayException("결제 오류");
                        var reservationId = lastInserted.getInt(1);

                        // 랜덤으로 좌석 예약
                        StringBuilder q = new StringBuilder(
                                "SELECT t.seat_count FROM theaters t WHERE t.theater_id = (SELECT theater_id FROM schedules where schedule_id = ");
                        q.append(selectedSchedule[5]);
                        q.append(')');
                        System.out.println(q.toString());
                        ResultSet getTotalSeats = statement.executeQuery(q.toString());
                        int totalSeats = 0;
                        getTotalSeats.first();
                        totalSeats = getTotalSeats.getInt("t.seat_count");

                        StringBuilder q2 = new StringBuilder(
                                "SELECT count(*) as 'count' FROM tickets t, schedules s WHERE t.schedule_id = s.schedule_id and t.schedule_id = ");
                        q2.append(selectedSchedule[5]);

                        System.out.println(q2.toString());
                        ResultSet getUnavailableSeats = statement.executeQuery(q2.toString());
                        getUnavailableSeats.first();
                        int availableSeats = totalSeats - getUnavailableSeats.getInt("count");
                        if (availableSeats == 0) {
                            JOptionPane.showMessageDialog(null, "매진된 일정입니다.", "안내", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        Random rand = new Random();
                        int randomSeat = -1;
                        while (true) {
                            var q3 = new StringBuilder("SELECT s.theater_id FROM schedules s where s.schedule_id = ");
                            q3.append(selectedSchedule[5]);
                            System.out.println(q3.toString());
                            ResultSet whereIs = statement.executeQuery(q3.toString());
                            whereIs.first();
                            int theater = whereIs.getInt("s.theater_id");

                            var q4 = new StringBuilder("SELECT s.seat_id FROM seats s WHERE s.theater_id = ");
                            q4.append(theater);
                            q4.append(" ORDER BY s.seat_id ASC LIMIT 1");
                            System.out.println(q4.toString());
                            ResultSet offset = statement.executeQuery(q4.toString());
                            offset.first();
                            int begin = offset.getInt("s.seat_id");

                            randomSeat = rand.nextInt(totalSeats) + begin;

                            var q5 = new StringBuilder("SELECT count(*) FROM tickets WHERE seat_id = ");
                            q5.append(randomSeat);
                            q5.append(" and schedule_id = ");
                            q5.append(selectedSchedule[5]);
                            q5.append(" GROUP BY schedule_id");
                            System.out.println(q5.toString());
                            ResultSet getOccupied = statement.executeQuery(q5.toString());

                            if (!getOccupied.next()) {
                                break;
                            }
                        }

                        var ticketQuery = new StringBuilder("INSERT INTO tickets ");
                        ticketQuery.append(
                                "(movie_id, schedule_id, theater_id, seat_id, reservation_id, username, status, standard_price, sale_price) ");
                        ticketQuery.append("VALUES (");
                        ticketQuery.append(movieId);
                        ticketQuery.append(", ");
                        ticketQuery.append(selectedSchedule[5]);
                        ticketQuery.append(", ");
                        ticketQuery.append(selectedSchedule[1]);
                        ticketQuery.append(", ");
                        ticketQuery.append(randomSeat);
                        ticketQuery.append(", ");
                        ticketQuery.append(reservationId);
                        ticketQuery.append(", \'");
                        ticketQuery.append(username);
                        ticketQuery.append("\', \'Y\', \'10000\', \'10000\')");

                        System.out.println(ticketQuery.toString());
                        isSuccessed = statement.executeUpdate(ticketQuery.toString(), statement.RETURN_GENERATED_KEYS);
                        lastInserted = statement.getGeneratedKeys();
                        if (isSuccessed < 1 || !lastInserted.next()) {
                            throw new FailedToBookException("예매 오류");
                        }
                        ticketId = lastInserted.getInt(1);
                        mainLayout.next(contentPanel);
                    } catch (FailedToBookException err) {
                        JOptionPane.showMessageDialog(null, err.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        err.printStackTrace();
                        return;
                    } catch (FailedToPayException err) {
                        JOptionPane.showMessageDialog(null, err.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        err.printStackTrace();
                        return;
                    } catch (SQLException err) {
                        JOptionPane.showMessageDialog(null, "SQL문 쿼리 오류", "ERROR", JOptionPane.ERROR_MESSAGE);
                        err.printStackTrace();
                        return;
                    }

                    try {
                        var resultReservation = new StringBuilder(
                                "SELECT m.name, s.date, s.start_time, t.theater_id, t.seat_id, r.username, r.pay_method, r.pay_price "
                                        + "FROM tickets t, movies m, reservations r, schedules s ");
                        resultReservation.append(
                                "WHERE t.movie_id = m.movie_id and r.reservation_id = t.reservation_id and s.schedule_id = t.schedule_id and t.ticket_id = ");
                        resultReservation.append(ticketId);

                        var statement = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);
                        System.out.println(resultReservation.toString());
                        var ticket = statement.executeQuery(resultReservation.toString());
                        if (!ticket.next())
                            throw new Exception();

                        JLabel lblNewLabel_6_5 = new JLabel(ticket.getString(1));
                        lblNewLabel_6_5.setHorizontalAlignment(SwingConstants.CENTER);
                        lblNewLabel_6_5.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
                        lblNewLabel_6_5.setBounds(179, 37, 181, 14);
                        infoPanel.add(lblNewLabel_6_5);

                        JLabel lblNewLabel_6_5_1 = new JLabel(ticket.getString(2));
                        lblNewLabel_6_5_1.setHorizontalAlignment(SwingConstants.CENTER);
                        lblNewLabel_6_5_1.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
                        lblNewLabel_6_5_1.setBounds(179, 64, 181, 14);
                        infoPanel.add(lblNewLabel_6_5_1);

                        JLabel lblNewLabel_6_5_2 = new JLabel(ticket.getString(3));
                        lblNewLabel_6_5_2.setHorizontalAlignment(SwingConstants.CENTER);
                        lblNewLabel_6_5_2.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
                        lblNewLabel_6_5_2.setBounds(179, 89, 181, 14);
                        infoPanel.add(lblNewLabel_6_5_2);

                        JLabel lblNewLabel_6_5_3 = new JLabel(ticket.getString(4));
                        lblNewLabel_6_5_3.setHorizontalAlignment(SwingConstants.CENTER);
                        lblNewLabel_6_5_3.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
                        lblNewLabel_6_5_3.setBounds(179, 114, 181, 14);
                        infoPanel.add(lblNewLabel_6_5_3);

                        JLabel lblNewLabel_6_5_4 = new JLabel(ticket.getString(5));
                        lblNewLabel_6_5_4.setHorizontalAlignment(SwingConstants.CENTER);
                        lblNewLabel_6_5_4.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
                        lblNewLabel_6_5_4.setBounds(179, 139, 181, 14);
                        infoPanel.add(lblNewLabel_6_5_4);

                        JLabel lblNewLabel_6_5_5 = new JLabel(ticket.getString(6));
                        lblNewLabel_6_5_5.setHorizontalAlignment(SwingConstants.CENTER);
                        lblNewLabel_6_5_5.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
                        lblNewLabel_6_5_5.setBounds(179, 164, 181, 14);
                        infoPanel.add(lblNewLabel_6_5_5);

                        JLabel lblNewLabel_6_5_6 = new JLabel(ticket.getString(7));
                        lblNewLabel_6_5_6.setHorizontalAlignment(SwingConstants.CENTER);
                        lblNewLabel_6_5_6.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
                        lblNewLabel_6_5_6.setBounds(179, 189, 181, 14);
                        infoPanel.add(lblNewLabel_6_5_6);
                        
                        JLabel lblNewLabel_6_5_7 = new JLabel(ticket.getString(8));
                        lblNewLabel_6_5_7.setHorizontalAlignment(SwingConstants.CENTER);
                        lblNewLabel_6_5_7.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
                        lblNewLabel_6_5_7.setBounds(179, 212, 181, 14);
                        infoPanel.add(lblNewLabel_6_5_7);
                        
                    } catch (Exception err) {
                        JOptionPane.showMessageDialog(null, "티켓 출력 실패. 관리자에게 연락하십시오.", "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        err.printStackTrace();
                    }

                }
            });
            btnNewButton_2.setBounds(313, 275, 89, 23);
            panel_1.add(btnNewButton_2);

        }
        {
            JPanel completedReservation = new JPanel();
            contentPanel.add(completedReservation, "completedReservation");
            completedReservation.setLayout(new BorderLayout(0, 0));
            completedReservation.add(infoPanel, BorderLayout.CENTER);

            JLabel lblNewLabel_5 = new JLabel("예매가 완료되었습니다.");
            lblNewLabel_5.setFont(new Font("Gulim", Font.BOLD, 15));
            lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
            completedReservation.add(lblNewLabel_5, BorderLayout.NORTH);

            // 영화명
            JLabel lblNewLabel_6 = new JLabel("영화명");
            lblNewLabel_6.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
            lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_6.setBounds(74, 37, 46, 14);
            infoPanel.add(lblNewLabel_6);

            // 날짜
            JLabel lblNewLabel_6_1 = new JLabel("날짜");
            lblNewLabel_6_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_6_1.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
            lblNewLabel_6_1.setBounds(74, 62, 46, 14);
            infoPanel.add(lblNewLabel_6_1);

            // 시작 시간
            JLabel lblNewLabel_6_2 = new JLabel("시작 시간");
            lblNewLabel_6_2.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_6_2.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
            lblNewLabel_6_2.setBounds(64, 87, 66, 14);
            infoPanel.add(lblNewLabel_6_2);

            // 상영관
            JLabel lblNewLabel_6_3 = new JLabel("상영관");
            lblNewLabel_6_3.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_6_3.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
            lblNewLabel_6_3.setBounds(74, 112, 46, 14);
            infoPanel.add(lblNewLabel_6_3);

            // 좌석
            JLabel lblNewLabel_6_4 = new JLabel("좌석");
            lblNewLabel_6_4.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_6_4.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
            lblNewLabel_6_4.setBounds(74, 137, 46, 14);
            infoPanel.add(lblNewLabel_6_4);

            // 결제 방법
            JLabel lblNewLabel_6_4_1 = new JLabel("예매자");
            lblNewLabel_6_4_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_6_4_1.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
            lblNewLabel_6_4_1.setBounds(64, 162, 66, 14);
            infoPanel.add(lblNewLabel_6_4_1);

            // 예매자
            JLabel lblNewLabel_6_4_1_1 = new JLabel("결제방법");
            lblNewLabel_6_4_1_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_6_4_1_1.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
            lblNewLabel_6_4_1_1.setBounds(64, 187, 66, 14);
            infoPanel.add(lblNewLabel_6_4_1_1);
            
            JLabel lblNewLabel_6_4_1_2 = new JLabel("결제금액");
            lblNewLabel_6_4_1_2.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_6_4_1_2.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
            lblNewLabel_6_4_1_2.setBounds(64, 212, 66, 14);
            infoPanel.add(lblNewLabel_6_4_1_2);

            JPanel panel_1 = new JPanel();
            completedReservation.add(panel_1, BorderLayout.SOUTH);

            JButton btnNewButton_3 = new JButton("확인");
            btnNewButton_3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            panel_1.add(btnNewButton_3);

        }

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton confirmButton = new JButton("예매");
                confirmButton.setActionCommand("OK");

                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int rowCount = resultTable.getRowCount();
                        int selectCount = 0;
                        int lastSelectIdx = -1;
                        try {
                            for (int i = 0; i < rowCount; i++) {
                                if (Boolean.TRUE.equals((boolean) resultTable.getValueAt(i, 0))) {
                                    selectCount++;
                                    lastSelectIdx = i;
                                }
                                if (selectCount > 1)
                                    throw new Exception("한 번에 한 개의 영화만 예약할 수 있습니다.");

                            }
                            if (selectCount == 0 && lastSelectIdx == -1)
                                throw new Exception("영화를 선택하십시오.");
                            else {
                                // 상영 시간 표시 테이블 창 표시
                                // 영화 예매하기
                                int colCount = resultTable.getColumnCount() - 1;
                                selectedSchedule = new Object[colCount + 1];
                                for (int i = 1; i < colCount; i++) {
                                    selectedSchedule[i] = resultTable.getValueAt(lastSelectIdx, i);
                                }
                                selectedSchedule[colCount] = (Object) scheduleIds.get(lastSelectIdx);

                                confirmButton.setEnabled(false);
                                mainLayout.next(contentPanel);

                            }
                        } catch (Exception err) {
                            JOptionPane.showMessageDialog(null, err.getMessage(), "Message", JOptionPane.ERROR_MESSAGE);
                            err.printStackTrace();
                        }

                    }
                });
                buttonPane.add(confirmButton);
                getRootPane().setDefaultButton(confirmButton);
            }
            {
                JButton cancelButton = new JButton("취소");
                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                buttonPane.add(cancelButton);
            }
        }
    }
}
