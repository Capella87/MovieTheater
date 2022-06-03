import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.sql.ResultSetMetaData;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ChooseAnotherSchedule extends JDialog
{

    private final JPanel contentPanel = new JPanel();
    private Database db;
    private Object[] bookingInfo;
    private int origSchId;
    private int movieId;
    private JTable resultTable;
    final int saleprice = 10000;
    private JScrollPane scrollPane;

    /**
     * Create the dialog.
     */
    public ChooseAnotherSchedule(Database db, int movieId, Object[] bookingInfo, int origScheduleId)
    {
        this.db = db;
        this.bookingInfo = bookingInfo;
        this.movieId = movieId;
        origSchId = origScheduleId; // 원래 스케줄
        setTitle("예매 일정 변경");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        
        try {
            var getAltSchedule = new StringBuilder("SELECT theater_id, date FROM schedules "
                    + "WHERE schedule_id != ");
            getAltSchedule.append(origSchId); // 주의
            getAltSchedule.append(" and movie_id = ");
            getAltSchedule.append(movieId);
            
            int altScheduleCount = 0;
            var statement = this.db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet getSchedules = statement.executeQuery(getAltSchedule.toString());
            
            while (getSchedules.next()) {
                altScheduleCount++;
            }

            if (getSchedules == null || altScheduleCount == 0) {
                JOptionPane.showMessageDialog(null, "해당 영화가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                 // ta.append("\n해당 영화가 없습니다. \n\n");
            } else {
                ResultSetMetaData columns = getSchedules.getMetaData();
                int columnCount = columns.getColumnCount();

                var resultModel = new DefaultTableModel(altScheduleCount, 0);
                for (int i = 2; i <= columnCount; i++) {
                        resultModel.addColumn(columns.getColumnName(i));
                }
                            
                getSchedules.first();
              
                            
                for (int i = 0; i < altScheduleCount; i++) {
                            
                    for (int j = 2; j <= columnCount; j++) {
                         resultModel.setValueAt(getSchedules.getString(j), i, j - 2); // 주의
                    }
                    getSchedules.next();
                 }
                            
                var wrapperModel = new TableResult(resultModel, "선택");
                            
                resultTable = new JTable(wrapperModel);
                scrollPane.setViewportView(resultTable);
             } 
           
            
        }
        catch (SQLException e) {
            
        }
       
        
        
        
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JScrollPane scrollPane = new JScrollPane();
            contentPanel.add(scrollPane, BorderLayout.CENTER);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton changeScheduleButton = new JButton("일정 변경");
                changeScheduleButton.setActionCommand("OK");
                buttonPane.add(changeScheduleButton);
                getRootPane().setDefaultButton(changeScheduleButton);
            }
            {
                JButton cancelButton = new JButton("취소");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

}
