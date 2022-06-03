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

public class SearchReservation extends JDialog
{

    private final JPanel contentPanel = new JPanel();
    private static Database db;
    private JTable resultTable;
    private JScrollPane scrollPane;

    private ArrayList<Object[]> bookingInfoObjects;
    

    private StringBuilder querybBuilder;

    private StringBuilder checkUserquery;
    
    String username, reservation_id, name;

    public SearchReservation(Database db)
    {
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
                        JTextField t = (JTextField)e.getSource();
                        username = t.getText();

                        querybBuilder = new StringBuilder("select m.name, sc.date, sc.start_time, t.theater_id, t.seat_id, t.sale_price, t.movie_id, t.reservation_id, t.username "
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
                            
		  	  	            while (rs.next()) resultCount++;

		  	  	            
                            if (resultCount == 0 ) {
                                JOptionPane.showMessageDialog(null, "예매 정보가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                            } else {

                                ResultSetMetaData columns = rs.getMetaData();
                                int columnCount = columns.getColumnCount();
                                bookingInfoObjects = new ArrayList<Object[]>();

                                var resultModel = new DefaultTableModel(resultCount, 0);
                                
                                for (int i = 1; i <= columnCount - 3; i++) {//
                                    resultModel.addColumn(columns.getColumnName(i));
                                }
                        
                                rs.first();
                            
                                for (int i = 0; i < resultCount; i++)
                                { //
                                    for (int j = 1; j <= columnCount - 3; j++) {
                                        resultModel.setValueAt(rs.getString(j), i, j - 1); // 주의
                                    }
                                    // t.movie_id, t.reservation_id, t.username
                                    bookingInfoObjects.add(new Object[] { (Object)rs.getInt(columnCount - 2), 
                                    (Object)rs.getInt(columnCount - 1), (Object)rs.getString(columnCount)});
                                    rs.next();
                                }
                                
                                
                                var wrapperModel = new TableResult(resultModel, "선택");

                                resultTable = new JTable(wrapperModel);
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
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
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