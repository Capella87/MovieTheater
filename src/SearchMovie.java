import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Dimension;

public class SearchMovie extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private static Database db;
    private static StringBuilder queryBuilder;
    private Map<String, String> map;
    private JTable resultTable;
    private JScrollPane scrollPane;
    private ArrayList<Integer> movieIds;

    public SearchMovie(Database db) {
        this.db = db;
        map = new HashMap<String, String>();

        setBounds(100, 100, 582, 432);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(200, 200));
            contentPanel.add(panel, BorderLayout.NORTH);
            panel.setLayout(null);

            {
                JLabel lblNewLabel_3 = new JLabel("입력 후 Enter를 누르세요.");
                lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel_3.setFont(new Font("Gulim", Font.PLAIN, 15));
                lblNewLabel_3.setBounds(197, 10, 173, 19);
                panel.add(lblNewLabel_3);
            }

            {
                JLabel lblNewLabel = new JLabel("영화명");
                lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel.setBounds(76, 39, 74, 19);
                panel.add(lblNewLabel);

                JTextField tf_name = new JTextField();
                tf_name.setBounds(171, 39, 260, 19);
                tf_name.setColumns(10);
                tf_name.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField t = (JTextField) e.getSource();
                        String name = t.getText();
                        if (!name.equals(""))
                            map.put("name", name);

                        t.setText("저장 완료");
                    }
                });
                panel.add(tf_name);
            }

            {
                JLabel lblNewLabel = new JLabel("감독명");
                lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel.setBounds(76, 68, 74, 19);
                panel.add(lblNewLabel);

                JTextField tf_director = new JTextField();
                tf_director.setColumns(10);
                tf_director.setBounds(171, 68, 260, 19);
                tf_director.addActionListener(new ActionListener() {

                    PreparedStatement pstmt = null;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField t = (JTextField) e.getSource();
                        String director = t.getText();
                        if (!director.equals(""))
                            map.put("director", director);

                        t.setText("저장 완료");
                    }

                });
                panel.add(tf_director);
            }

            {
                JLabel lblNewLabel_1 = new JLabel("배우명");
                lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel_1.setBounds(76, 100, 74, 19);
                panel.add(lblNewLabel_1);

                JTextField tf_actor = new JTextField();
                tf_actor.setColumns(10);
                tf_actor.setBounds(171, 97, 260, 19);
                tf_actor.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        JTextField t = (JTextField) e.getSource();
                        String actor = t.getText();
                        if (!actor.equals(""))
                            map.put("actor", actor);

                        t.setText("저장 완료");
                    }
                });
                panel.add(tf_actor);
            }

            {
                JLabel lblNewLabel_2 = new JLabel("장르명");
                lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel_2.setBounds(76, 129, 74, 19);
                panel.add(lblNewLabel_2);

                JTextField tf_genre = new JTextField();
                tf_genre.setColumns(10);
                tf_genre.setBounds(171, 126, 260, 19);
                tf_genre.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        JTextField t = (JTextField) e.getSource();
                        String genre = t.getText();
                        if (!genre.equals(""))
                            map.put("genre", genre);

                        t.setText("저장 완료");
                    }
                });

                panel.add(tf_genre);
            }

            JButton btnNewButton = new JButton("검색");
            btnNewButton.setFont(new Font("Gulim", Font.PLAIN, 17));
            btnNewButton.setBounds(233, 155, 96, 35);
            panel.add(btnNewButton);

            scrollPane = new JScrollPane();
            contentPanel.add(scrollPane, BorderLayout.CENTER);

            btnNewButton.addActionListener(new ActionListener() {

                Statement stmt = null;

                @Override
                public void actionPerformed(ActionEvent e) {

                    int count = 0;
                    int mapCount = map.size();
                    queryBuilder = new StringBuilder("SELECT * from movies ");

                    if (!map.isEmpty())
                        queryBuilder.append("where ");
                    for (var entry : map.entrySet()) {
                        queryBuilder.append(entry.getKey());
                        queryBuilder.append(" = ");
                        queryBuilder.append("\'");
                        queryBuilder.append(entry.getValue());
                        queryBuilder.append("\'");
                        queryBuilder.append(" ");
                        if (count != mapCount - 1)
                            queryBuilder.append("and ");
                        count++;
                    }

                    String result = queryBuilder.toString();
                    System.out.println(result);

                    try {
                        stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery(result);
                        int resultCount = 0;

                        while (rs.next()) {
                            resultCount++;
                        }

                        if (rs == null || resultCount == 0) {
                            JOptionPane.showMessageDialog(null, "해당 영화가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                            // ta.append("\n해당 영화가 없습니다. \n\n");
                        } else {
                            ResultSetMetaData columns = rs.getMetaData();
                            int columnCount = columns.getColumnCount();

                            var resultModel = new DefaultTableModel(resultCount, 0);
                            for (int i = 2; i <= columnCount; i++) {
                                resultModel.addColumn(columns.getColumnName(i));
                            }

                            rs.first();
                            movieIds = new ArrayList<Integer>();

                            for (int i = 0; i < resultCount; i++) {
                                movieIds.add(rs.getInt(1));
                                // resultModel.setValueAt(rs.getInt(1), i, 1);
                                for (int j = 2; j <= columnCount; j++) {
                                    resultModel.setValueAt(rs.getString(j), i, j - 2); // 주의
                                }
                                rs.next();
                            }

                            var wrapperModel = new TableResult(resultModel, "선택");
                            resultTable = new JTable(wrapperModel);
                            scrollPane.setViewportView(resultTable);
                        }
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                        e1.printStackTrace();
                    }

                    resultTable.setVisible(true);
                    map.clear();
                }
            });
        }

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okReservation = new JButton("예매");
                okReservation.addActionListener(new ActionListener() {
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
                                Object[] target = new Object[colCount];
                                for (int i = 1; i < colCount; i++) {
                                    target[i] = resultTable.getValueAt(lastSelectIdx, i); // !
                                }
                                var selScheduleWindow = new SelectSchedule(target, db, movieIds.get(lastSelectIdx));
                                selScheduleWindow.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
                                selScheduleWindow.setVisible(true);
                            }
                        } catch (Exception err) {
                            JOptionPane.showMessageDialog(null, err.getMessage(), "Message", JOptionPane.ERROR_MESSAGE);
                            err.printStackTrace();
                        }
                    }
                });
                okReservation.setActionCommand("OK");
                buttonPane.add(okReservation);
                getRootPane().setDefaultButton(okReservation);
            }
            {
                JButton closeButton = new JButton("닫기");
                closeButton.setActionCommand("Cancel");
                closeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                buttonPane.add(closeButton);
            }
        }
    }
}
