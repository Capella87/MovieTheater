import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.sql.ResultSetMetaData;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ChooseAnotherSchedule extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private Database db;
    private Object[] bookingInfo;
    private ArrayList<Object[]> altScheduleInfo;
    private int origSchId;
    private int movieId;
    private JTable resultTable;
    final int saleprice = 10000;
    private TableResult wrapperModel = null;
    private JScrollPane scrollPane;

    /**
     * Create the dialog.
     */
    public ChooseAnotherSchedule(Database db, Object[] bookingInfo) {
        this.db = db;
        this.bookingInfo = bookingInfo; // 각각 영화id, 티켓id, 예약id, username, 스케줄id
        this.movieId = (Integer) bookingInfo[0];
        origSchId = (Integer) bookingInfo[4]; // 원래 스케줄
        setTitle("예매 일정 변경");
        setBounds(100, 100, 450, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        scrollPane = new JScrollPane();
        altScheduleInfo = new ArrayList<Object[]>();

        try {
            var getAltSchedule = new StringBuilder(
                    "SELECT theater_id, date, start_time, schedule_id FROM schedules " + "WHERE schedule_id != ");
            getAltSchedule.append(origSchId); // 주의
            getAltSchedule.append(" and movie_id = ");
            getAltSchedule.append(movieId);

            int altScheduleCount = 0;
            var statement = this.db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(getAltSchedule.toString());
            ResultSet getSchedules = statement.executeQuery(getAltSchedule.toString());

            while (getSchedules.next()) {
                altScheduleCount++;
            }

            if (getSchedules == null || altScheduleCount == 0) {
                JOptionPane.showMessageDialog(null, "해당 영화의 다른 일정이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // 화면 닫기
            } else {
                ResultSetMetaData columns = getSchedules.getMetaData();
                int columnCount = columns.getColumnCount();

                var resultModel = new DefaultTableModel(altScheduleCount, 0);
                for (int i = 2; i <= columnCount - 1; i++) {
                    resultModel.addColumn(columns.getColumnName(i));
                }

                getSchedules.first();

                for (int i = 0; i < altScheduleCount; i++) {

                    for (int j = 2; j <= columnCount - 1; j++) {
                        resultModel.setValueAt(getSchedules.getString(j), i, j - 2); // 주의
                    }
                    this.altScheduleInfo.add(new Object[] { getSchedules.getInt(columnCount), getSchedules.getInt(1) }); // schedule_id,
                                                                                                                         // theater_id
                                                                                                                         // 삽입
                    getSchedules.next();
                }

                wrapperModel = new TableResult(resultModel, "선택");

                resultTable = new JTable(wrapperModel);
                scrollPane.setViewportView(resultTable);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL문 실행 오류", "ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        }
        {
            JLabel lblNewLabel = new JLabel("다른 일정을 선택하세요.");
            lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(lblNewLabel, BorderLayout.NORTH);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton changeScheduleButton = new JButton("일정 변경");
                changeScheduleButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (wrapperModel == null || resultTable == null)
                            return;

                        try {
                            int rowCount = resultTable.getRowCount();
                            int lastSelectedIdx = -1;
                            int selectedCount = 0;

                            for (int i = 0; i < rowCount; i++) {
                                Boolean isSelected = (Boolean) resultTable.getValueAt(i, 0);

                                if (isSelected && selectedCount == 0) {
                                    lastSelectedIdx = i;
                                    selectedCount++;
                                } else if (isSelected && selectedCount > 0) {
                                    throw new MultipleSelectedException("한 개만 선택할 수 있습니다.");
                                }
                            }
                            if (lastSelectedIdx == -1) {
                                JOptionPane.showMessageDialog(null, "영화를 선택하십시오.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                return;
                            } else {
                                try {
                                    var st = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                            ResultSet.CONCUR_READ_ONLY);
                                    // 영화가 매진된 경우
                                    StringBuilder q = new StringBuilder(
                                            "SELECT t.seat_count FROM theaters t WHERE t.theater_id = (SELECT theater_id FROM schedules where schedule_id = ");
                                    q.append((Integer) altScheduleInfo.get(lastSelectedIdx)[0]);
                                    q.append(')');
                                    System.out.println(q.toString());
                                    ResultSet getTotalSeats = st.executeQuery(q.toString());
                                    int totalSeats = 0;
                                    getTotalSeats.first();
                                    totalSeats = getTotalSeats.getInt("t.seat_count");

                                    StringBuilder q2 = new StringBuilder(
                                            "SELECT count(*) as 'count' FROM tickets t, schedules s WHERE t.schedule_id = s.schedule_id and t.schedule_id = ");
                                    q2.append(Integer.toString((Integer) altScheduleInfo.get(lastSelectedIdx)[0]));
                                    
                                    System.out.println(q2.toString());
                                    ResultSet getUnavailableSeats = st.executeQuery(q2.toString());
                                    getUnavailableSeats.first();
                                    int availableSeats = totalSeats - getUnavailableSeats.getInt("count");
                                    if (availableSeats == 0) {
                                        JOptionPane.showMessageDialog(null, "매진된 일정입니다.", "안내",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        wrapperModel.removeRow(lastSelectedIdx); // 매진된 것이므로 대상에서 제외
                                        return;
                                    }

                                    Random rand = new Random();
                                    int randomSeat = -1;
                                    while (true) {
                                        var q3 = new StringBuilder(
                                                "SELECT s.theater_id FROM schedules s where s.schedule_id = ");
                                        q3.append(altScheduleInfo.get(lastSelectedIdx)[0]);
                                        System.out.println(q3.toString());
                                        ResultSet whereIs = st.executeQuery(q3.toString());
                                        whereIs.first();
                                        int theater = whereIs.getInt("s.theater_id");

                                        var q4 = new StringBuilder(
                                                "SELECT s.seat_id FROM seats s WHERE s.theater_id = ");
                                        q4.append(theater);
                                        q4.append(" ORDER BY s.seat_id ASC LIMIT 1");
                                        System.out.println(q4.toString());
                                        ResultSet offset = st.executeQuery(q4.toString());
                                        offset.first();
                                        int begin = offset.getInt("s.seat_id");

                                        randomSeat = rand.nextInt(totalSeats) + begin;
                                        var statement = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                                ResultSet.CONCUR_READ_ONLY);

                                        var q5 = new StringBuilder("SELECT count(*) FROM tickets WHERE seat_id = ");
                                        q5.append(Integer.toString(randomSeat));
                                        q5.append(" and schedule_id = ");
                                        q5.append(Integer.toString((Integer) altScheduleInfo.get(lastSelectedIdx)[0]));
                                        q5.append(" GROUP BY schedule_id");
                                        System.out.println(q5.toString());
                                        ResultSet getOccupied = statement.executeQuery(q5.toString());

                                        if (!getOccupied.next()) {
                                            break;
                                        }
                                    }
                                    // 예매 변경
                                    var getAltSchedule = new StringBuilder("UPDATE tickets SET schedule_id = ");
                                    // + "WHERE schedule_id != ");
                                    getAltSchedule.append(altScheduleInfo.get(lastSelectedIdx)[0]); // 주의
                                    getAltSchedule.append(" , theater_id = ");
                                    getAltSchedule.append((Integer) altScheduleInfo.get(lastSelectedIdx)[1]);
                                    getAltSchedule.append(" , seat_id = ");
                                    getAltSchedule.append(randomSeat);
                                    getAltSchedule.append(" WHERE ticket_id = ");
                                    getAltSchedule.append((Integer) bookingInfo[1]);

                                    var statement = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                            ResultSet.CONCUR_READ_ONLY);
                                    System.out.println(getAltSchedule.toString());
                                    var isUpdated = statement.executeUpdate(getAltSchedule.toString());
                                    if (isUpdated < 1)
                                        throw new Exception();
                                    else {
                                        JOptionPane.showMessageDialog(null, "예매 변경 성공. 검색에서 username을 다시 입력하시면 바뀌어 있을 겁니다.", "안내",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        dispose();
                                    }

                                } catch (Exception err) {
                                    JOptionPane.showMessageDialog(null, "예매 변경 실패", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    System.out.println(err.getMessage());
                                    err.printStackTrace();
                                }
                            }
                        } catch (MultipleSelectedException err) {
                            JOptionPane.showMessageDialog(null, err.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                            err.printStackTrace();
                        }
                    }
                });
                buttonPane.add(changeScheduleButton);
                getRootPane().setDefaultButton(changeScheduleButton);
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
