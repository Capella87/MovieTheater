import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SearchAllTable extends JDialog {

    private Database db;
    private Statement stmt;
    private String query;
    private JTable resultTable;
    private JScrollPane scrollPane;

    public SearchAllTable(Database db) {
        this.db = new Database();

        setTitle("모든 테이블 조회");
        setBounds(100, 100, 552, 400);
        getContentPane().setLayout(new BorderLayout());
        {
            JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
            getContentPane().add(tabbedPane, BorderLayout.CENTER);
            {
                Panel tab_movies = new Panel();
                tabbedPane.addTab("movies", null, tab_movies, null);
                {
                    JScrollPane scrollPane = new JScrollPane();

                    /* movies table */
                    query = "SELECT * FROM movies";

                    try {
                        stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery(query);

                        int resultCount = 0;
                        while (rs.next())
                            resultCount++;

                        ResultSetMetaData columns = rs.getMetaData();
                        int columnCount = columns.getColumnCount();
                        var resultModel = new DefaultTableModel(resultCount, 0);

                        for (int i = 1; i <= columnCount; i++) {//

                            resultModel.addColumn(columns.getColumnName(i));

                        }

                        rs.first();

                        for (int i = 0; i < resultCount; i++) { //
                            for (int j = 1; j <= columnCount; j++) {
                                resultModel.setValueAt(rs.getString(j), i, j - 1); // 주의
                            }
                            rs.next();
                        }

                        resultTable = new JTable(resultModel);
                        scrollPane.setViewportView(resultTable);

                    }

                    catch (SQLException e1) {

                        JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                        e1.printStackTrace();

                    }
                    resultTable.setVisible(true);
                    tab_movies.add(scrollPane);
                }
            }
            {
                Panel tab_theaters = new Panel();
                tabbedPane.addTab("theaters", null, tab_theaters, null);
                {
                    JScrollPane scrollPane = new JScrollPane();

                    /* theaters table */
                    query = "SELECT * FROM theaters";

                    try {
                        stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery(query);

                        int resultCount = 0;
                        while (rs.next())
                            resultCount++;

                        ResultSetMetaData columns = rs.getMetaData();
                        int columnCount = columns.getColumnCount();
                        var resultModel = new DefaultTableModel(resultCount, 0);

                        for (int i = 1; i <= columnCount; i++) {//

                            resultModel.addColumn(columns.getColumnName(i));

                        }

                        rs.first();

                        for (int i = 0; i < resultCount; i++) { //
                            for (int j = 1; j <= columnCount; j++) {
                                resultModel.setValueAt(rs.getString(j), i, j - 1); // 주의
                            }
                            rs.next();
                        }

                        resultTable = new JTable(resultModel);
                        scrollPane.setViewportView(resultTable);

                    }

                    catch (SQLException e1) {

                        JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                        e1.printStackTrace();

                    }
                    resultTable.setVisible(true);
                    tab_theaters.add(scrollPane);
                }
            }
            {
                Panel tab_members = new Panel();
                tabbedPane.addTab("members", null, tab_members, null);
                {
                    JScrollPane scrollPane = new JScrollPane();
                    /* members table */
                    query = "SELECT * FROM members";

                    try {
                        stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery(query);

                        int resultCount = 0;
                        while (rs.next())
                            resultCount++;

                        ResultSetMetaData columns = rs.getMetaData();
                        int columnCount = columns.getColumnCount();
                        var resultModel = new DefaultTableModel(resultCount, 0);

                        for (int i = 1; i <= columnCount; i++) {//

                            resultModel.addColumn(columns.getColumnName(i));

                        }

                        rs.first();

                        for (int i = 0; i < resultCount; i++) { //
                            for (int j = 1; j <= columnCount; j++) {
                                resultModel.setValueAt(rs.getString(j), i, j - 1); // 주의
                            }
                            rs.next();
                        }

                        resultTable = new JTable(resultModel);
                        scrollPane.setViewportView(resultTable);

                    }

                    catch (SQLException e1) {

                        JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                        e1.printStackTrace();

                    }
                    resultTable.setVisible(true);
                    tab_members.add(scrollPane);
                }
            }
            {
                Panel tab_reservations = new Panel();
                tabbedPane.addTab("reservations", null, tab_reservations, null);
                {
                    JScrollPane scrollPane = new JScrollPane();
                    /* reservations table */
                    query = "SELECT * FROM reservations";

                    try {
                        stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery(query);

                        int resultCount = 0;
                        while (rs.next())
                            resultCount++;

                        ResultSetMetaData columns = rs.getMetaData();
                        int columnCount = columns.getColumnCount();
                        var resultModel = new DefaultTableModel(resultCount, 0);

                        for (int i = 1; i <= columnCount; i++) {//

                            resultModel.addColumn(columns.getColumnName(i));

                        }

                        rs.first();

                        for (int i = 0; i < resultCount; i++) { //
                            for (int j = 1; j <= columnCount; j++) {
                                resultModel.setValueAt(rs.getString(j), i, j - 1); // 주의
                            }
                            rs.next();
                        }

                        resultTable = new JTable(resultModel);
                        scrollPane.setViewportView(resultTable);

                    }

                    catch (SQLException e1) {

                        JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                        e1.printStackTrace();

                    }
                    resultTable.setVisible(true);
                    tab_reservations.add(scrollPane);
                }
            }
            {
                Panel tab_seats = new Panel();
                tabbedPane.addTab("seats", null, tab_seats, null);
                {
                    JScrollPane scrollPane = new JScrollPane();
                    /* seats table */
                    query = "SELECT * FROM seats";

                    try {
                        stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery(query);

                        int resultCount = 0;
                        while (rs.next())
                            resultCount++;

                        ResultSetMetaData columns = rs.getMetaData();
                        int columnCount = columns.getColumnCount();
                        var resultModel = new DefaultTableModel(resultCount, 0);

                        for (int i = 1; i <= columnCount; i++) {//

                            resultModel.addColumn(columns.getColumnName(i));

                        }

                        rs.first();

                        for (int i = 0; i < resultCount; i++) { //
                            for (int j = 1; j <= columnCount; j++) {
                                resultModel.setValueAt(rs.getString(j), i, j - 1); // 주의
                            }
                            rs.next();
                        }

                        resultTable = new JTable(resultModel);
                        scrollPane.setViewportView(resultTable);

                    }

                    catch (SQLException e1) {

                        JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                        e1.printStackTrace();

                    }
                    resultTable.setVisible(true);
                    tab_seats.add(scrollPane);
                }
            }
            {
                Panel tab_schedules = new Panel();
                tabbedPane.addTab("schedules", null, tab_schedules, null);
                {
                    JScrollPane scrollPane = new JScrollPane();
                    /* schedules table */
                    query = "SELECT * FROM schedules";

                    try {
                        stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery(query);

                        int resultCount = 0;
                        while (rs.next())
                            resultCount++;

                        ResultSetMetaData columns = rs.getMetaData();
                        int columnCount = columns.getColumnCount();
                        var resultModel = new DefaultTableModel(resultCount, 0);

                        for (int i = 1; i <= columnCount; i++) {//

                            resultModel.addColumn(columns.getColumnName(i));

                        }

                        rs.first();

                        for (int i = 0; i < resultCount; i++) { //
                            for (int j = 1; j <= columnCount; j++) {
                                resultModel.setValueAt(rs.getString(j), i, j - 1); // 주의
                            }
                            rs.next();
                        }

                        resultTable = new JTable(resultModel);
                        scrollPane.setViewportView(resultTable);

                    }

                    catch (SQLException e1) {

                        JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                        e1.printStackTrace();

                    }
                    resultTable.setVisible(true);
                    tab_schedules.add(scrollPane);
                }
            }
            {
                Panel tab_tickets = new Panel();
                tabbedPane.addTab("tickets", null, tab_tickets, null);
                {
                    JScrollPane scrollPane = new JScrollPane();
                    /* tickets table */
                    query = "SELECT * FROM tickets";

                    try {
                        stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        ResultSet rs = stmt.executeQuery(query);

                        int resultCount = 0;
                        while (rs.next())
                            resultCount++;

                        ResultSetMetaData columns = rs.getMetaData();
                        int columnCount = columns.getColumnCount();
                        var resultModel = new DefaultTableModel(resultCount, 0);

                        for (int i = 1; i <= columnCount; i++) {//

                            resultModel.addColumn(columns.getColumnName(i));

                        }

                        rs.first();

                        for (int i = 0; i < resultCount; i++) { //
                            for (int j = 1; j <= columnCount; j++) {
                                resultModel.setValueAt(rs.getString(j), i, j - 1); // 주의
                            }
                            rs.next();
                        }

                        resultTable = new JTable(resultModel);
                        scrollPane.setViewportView(resultTable);

                    }

                    catch (SQLException e1) {

                        JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                        e1.printStackTrace();

                    }
                    resultTable.setVisible(true);
                    tab_tickets.add(scrollPane);
                }
            }
        }

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

}
