import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.util.*;

// 다중 선택 미허용시 발생하는 예외 정의
class MultipleSelectedException extends Exception {
    public MultipleSelectedException(String errorMessage) {
        super(errorMessage);
    }
}

public class SearchReservation extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private Database db;
    private JTable resultTable = null;
    private TableResult wrapperModel = null;
    private JScrollPane scrollPane;

    private ArrayList<Object[]> bookingInfoObjects;

    private StringBuilder querybBuilder;

    private StringBuilder checkUserquery;

    String username, reservation_id, name;

    public SearchReservation(Database db) {
        setTitle("예매 조회");
        this.db = db;
        setBounds(100, 100, 582, 432);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(100, 100));
            contentPanel.add(panel, BorderLayout.NORTH);
            panel.setLayout(null);

            {
                JLabel lblNewLabel_3 = new JLabel("<입력 후 Enter를 누르세요>");
                lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel_3.setFont(new Font("Gulim", Font.PLAIN, 15));
                lblNewLabel_3.setBounds(188, 10, 205, 19);
                panel.add(lblNewLabel_3);
            }

            {
                JLabel lb_username = new JLabel("Username");
                lb_username.setHorizontalAlignment(SwingConstants.CENTER);
                lb_username.setBounds(76, 39, 74, 19);
                panel.add(lb_username);

                scrollPane = new JScrollPane();
                contentPanel.add(scrollPane, BorderLayout.CENTER);

                JTextField tf_username = new JTextField();
                tf_username.setBounds(171, 39, 260, 19);
                tf_username.setColumns(10);
                tf_username.addActionListener(new ActionListener() {

                    Statement stmt = null;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField t = (JTextField) e.getSource();
                        username = t.getText();

                        querybBuilder = new StringBuilder(
                                "select m.name, sc.date, sc.start_time, t.theater_id, t.seat_id, t.sale_price, t.movie_id, t.ticket_id, t.reservation_id, t.username "
                                        + "from tickets t, movies m, madang.schedules sc, reservations re "
                                        + "where  t.movie_id = m.movie_id and t.schedule_id = sc.schedule_id and re.reservation_id = t.reservation_id");
                        checkUserquery = new StringBuilder(" and re.username = ");
                        checkUserquery.append("\'");
                        checkUserquery.append(username);
                        checkUserquery.append("\'");
                        querybBuilder.append(checkUserquery.toString());

                        String result = querybBuilder.toString();

                        try {
                            stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

                            ResultSet rs = stmt.executeQuery(result);
                            int resultCount = 0;

                            while (rs.next())
                                resultCount++;

                            if (resultCount == 0) {
                                JOptionPane.showMessageDialog(null, "예매 정보가 없습니다.", "알림",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {

                                ResultSetMetaData columns = rs.getMetaData();
                                int columnCount = columns.getColumnCount();
                                bookingInfoObjects = new ArrayList<Object[]>();

                                var resultModel = new DefaultTableModel(resultCount, 0);

                                for (int i = 1; i <= columnCount - 4; i++) {//
                                    resultModel.addColumn(columns.getColumnName(i));
                                }

                                rs.first();

                                for (int i = 0; i < resultCount; i++) { //
                                    for (int j = 1; j <= columnCount - 4; j++) {
                                        resultModel.setValueAt(rs.getString(j), i, j - 1); // 주의
                                    }
                                    // t.movie_id, t.reservation_id, t.username
                                    bookingInfoObjects.add(new Object[] { (Object)rs.getInt(columnCount - 3), (Object) rs.getInt(columnCount - 2),
                                            (Object) rs.getInt(columnCount - 1), (Object) rs.getString(columnCount) });
                                    rs.next();
                                }

                                wrapperModel = new TableResult(resultModel, "선택");

                                resultTable = new JTable(wrapperModel);
                                resultTable.addMouseListener(new MouseAdapter() {
                                    public void mouseClicked (MouseEvent e) {
                                        
                                        if (e.getClickCount() >= 2) {
                                            var target = (JTable)e.getSource();
                                            if (target.getSelectedColumn() == 0) return; // Ignore first column.
                                            
                                            // 예매 정보 띄우기; DB 정보 전달 필요
                                            // var info = new ReservationInfo(db);
                                            
                                        }
                                    }
                                });
                                scrollPane.setViewportView(resultTable);

                                t.setText("저장 완료");
                                resultTable.setVisible(true);
                            }
                        } catch (SQLException e1) {
                            JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                            e1.printStackTrace();
                        }

                    }
                });
                panel.add(tf_username);
            }

            {
                JPanel buttonPane = new JPanel();
                buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
                getContentPane().add(buttonPane, BorderLayout.SOUTH);
                {
                    JButton cancelReservationButton = new JButton("예매 취소");
                    buttonPane.add(cancelReservationButton);
                    cancelReservationButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (wrapperModel == null || resultTable == null) return;
                            
                            try {
                                int rowCount = resultTable.getRowCount();
                                int lastSelectedIdx = -1;
                                int selectedCount = 0;
                                
                                for (int i = 0; i < rowCount; i++) {
                                    Boolean isSelected = (Boolean)resultTable.getValueAt(i, 0);
                                    
                                    if (isSelected && selectedCount == 0) {
                                        lastSelectedIdx = i;
                                        selectedCount++;
                                    }
                                    else if (isSelected && selectedCount > 0) {
                                        throw new MultipleSelectedException("한 개만 선택할 수 있습니다.");
                                    }
                                }
                                if (lastSelectedIdx == -1) {
                                    JOptionPane.showMessageDialog(null, "영화를 선택하십시오.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                else {
                                    try {
                                        // 예매 취소; 테이블에 있는 튜플 정보로 SQL로 검색해 해당 정보 삭제
                                        var builder = new StringBuilder("delete from reservations where reservation_id = ");
                                        builder.append((Integer)bookingInfoObjects.get(lastSelectedIdx)[2]);
                                        var stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                        int isConverted = stmt.executeUpdate(builder.toString());
                                        // 예매 취소 완료
                                        if (isConverted == 1)
                                        {
                                            JOptionPane.showMessageDialog(null, "예매가 정상적으로 취소되었습니다.", "알림",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                            wrapperModel.removeRow(lastSelectedIdx);   
                                        }
                                        else throw new Exception();
                                    }
                                    catch (Exception err) {
                                        JOptionPane.showMessageDialog(null, "예매 취소 실패", "ERROR",
                                                JOptionPane.ERROR_MESSAGE);
                                        err.printStackTrace();
                                    }
                                }
                            }
                            catch (MultipleSelectedException err) {
                                JOptionPane.showMessageDialog(null, err.getMessage(), "ERROR",
                                        JOptionPane.ERROR_MESSAGE);
                                err.printStackTrace();
                            }
                        }
                    });
                }
                {
                    JButton changeMovieButton = new JButton("다른 영화로 변경 ");
                    buttonPane.add(changeMovieButton);
                    changeMovieButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (wrapperModel == null || resultTable == null) return;
                            
                            try {
                                int rowCount = resultTable.getRowCount();
                                int lastSelectedIdx = -1;
                                int selectedCount = 0;
                                
                                for (int i = 0; i < rowCount; i++) {
                                    Boolean isSelected = (Boolean)resultTable.getValueAt(i, 0);
                                    
                                    if (isSelected && selectedCount == 0) {
                                        lastSelectedIdx = i;
                                        selectedCount++;
                                    }
                                    else if (isSelected && selectedCount > 0) {
                                        throw new MultipleSelectedException("한 개만 선택할 수 있습니다.");
                                    }
                                }
                                if (lastSelectedIdx == -1) {
                                    JOptionPane.showMessageDialog(null, "영화를 선택하십시오.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                else {
                                    try {
                                        // 예매 변경; 테이블에 있는 튜플 정보로 SQL로 검색해 찾고 다른 영화를 검색; 다른 영화가 없는 경우 메시지 띄우고 종료
                                        
                                        // 예매 변경 완료
                                        JOptionPane.showMessageDialog(null, "예매가 정상적으로 변경되었습니다.", "알림",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        wrapperModel.removeRow(lastSelectedIdx);
                                    }
                                    catch (Exception err) {
                                        JOptionPane.showMessageDialog(null, "예매 변경 실패", "ERROR",
                                                JOptionPane.ERROR_MESSAGE);
                                        err.printStackTrace();
                                    }
                                }
                            }
                            catch (MultipleSelectedException err) {
                                JOptionPane.showMessageDialog(null, err.getMessage(), "ERROR",
                                        JOptionPane.ERROR_MESSAGE);
                                err.printStackTrace();
                            }
                        }
                    });
                }
                {
                    JButton changeScheduleButton = new JButton("다른 일정으로 변경");
                    changeScheduleButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (wrapperModel == null || resultTable == null) return;
                            
                            try {
                                int rowCount = resultTable.getRowCount();
                                int lastSelectedIdx = -1;
                                int selectedCount = 0;
                                
                                for (int i = 0; i < rowCount; i++) {
                                    Boolean isSelected = (Boolean)resultTable.getValueAt(i, 0);
                                    
                                    if (isSelected && selectedCount == 0) {
                                        lastSelectedIdx = i;
                                        selectedCount++;
                                    }
                                    else if (isSelected && selectedCount > 0) {
                                        throw new MultipleSelectedException("한 개만 선택할 수 있습니다.");
                                    }
                                }
                                if (lastSelectedIdx == -1) {
                                    JOptionPane.showMessageDialog(null, "영화를 선택하십시오.", "ERROR", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                else {
                                    try {
                                        // 예매 변경; 테이블에 있는 튜플 정보로 SQL로 검색해 찾고 다른 일정을 검색; 같은 영화인 다른 일정이 없는 경우 메시지 띄우고 종료
                                        
                                        // 예매 변경 완료
                                        JOptionPane.showMessageDialog(null, "예매가 정상적으로 변경되었습니다.", "알림",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        wrapperModel.removeRow(lastSelectedIdx);
                                    }
                                    catch (Exception err) {
                                        JOptionPane.showMessageDialog(null, "예매 변경 실패", "ERROR",
                                                JOptionPane.ERROR_MESSAGE);
                                        err.printStackTrace();
                                    }
                                }
                            }
                            catch (MultipleSelectedException err) {
                                JOptionPane.showMessageDialog(null, err.getMessage(), "ERROR",
                                        JOptionPane.ERROR_MESSAGE);
                                err.printStackTrace();
                            }
                        }
                    });
                    buttonPane.add(changeScheduleButton);
                }
                {
                    JButton cancelButton = new JButton("취소");
                    cancelButton.setActionCommand("Cancel");
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
}