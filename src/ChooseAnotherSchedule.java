import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

public class ChooseAnotherSchedule extends JDialog
{

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
    public ChooseAnotherSchedule(Database db, Object[] bookingInfo)
    {
        this.db = db;
        this.bookingInfo = bookingInfo;
        this.movieId = (Integer)bookingInfo[0];
        origSchId = (Integer)bookingInfo[4]; // 원래 스케줄
        setTitle("예매 일정 변경");
        setBounds(100, 100, 450, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        
        scrollPane = new JScrollPane();
        altScheduleInfo = new ArrayList<Object[]>();
        
        try {
            var getAltSchedule = new StringBuilder("SELECT theater_id, date, start_time, schedule_id FROM schedules "
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
                    this.altScheduleInfo.add(new Object[] { getSchedules.getInt(columnCount) }); // schedule_id 삽입
                    getSchedules.next();
                 }
                            
                var wrapperModel = new TableResult(resultModel, "선택");
                            
                resultTable = new JTable(wrapperModel);
                scrollPane.setViewportView(resultTable);
             } 
           
            
        }
        catch (SQLException e) {
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
                                    // 예매 변경
                                    
                                    
                                    // var choose = new ChooseAnotherSchedule(db, altScheduleInfo.get(lastSelectedIdx)[0]);
                                    // choose.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
                                    // choose.setVisible(true);
                                }
                                catch (Exception err) {
                                    JOptionPane.showMessageDialog(null, "예매 변경 실패", "ERROR",
                                            JOptionPane.ERROR_MESSAGE);
                                    err.printStackTrace();
                                }
                            }
                        }
                            catch (MultipleSelectedException e) {
                                JOptionPane.showMessageDialog(null, err.getMessage(), "ERROR",
                                        JOptionPane.ERROR_MESSAGE);
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
