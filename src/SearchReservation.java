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

    private static StringBuilder queryBuilder = new StringBuilder("SELECT	name, release_date, theater_id, seat_id, sale_price" 
                                                                 + "FROM	reservations"
                                                                 + "WHERE	username = ");

    String username, reservation_id, name;

    public SearchReservation(Database db)
    {
        this.db = db;
        setBounds(100, 100, 582, 432);
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
                JLabel lblNewLabel_3 = new JLabel("<입력 후 Enter를 누르세요>");
                lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
                lblNewLabel_3.setFont(new Font("Gulim", Font.PLAIN, 15));
                lblNewLabel_3.setBounds(197, 10, 173, 19);
                panel.add(lblNewLabel_3);
            }

            {
                JLabel lb_username = new JLabel("Username");
                lb_username.setHorizontalAlignment(SwingConstants.CENTER);
                lb_username.setBounds(76, 39, 74, 19);
                panel.add(lb_username);

                JTextField tf_username = new JTextField();
                tf_username.setBounds(171, 39, 260, 19);
                tf_username.setColumns(10);
                tf_username.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField t = (JTextField)e.getSource();
                        username = t.getText();
                        t.setText("저장 완료");
                    }
                });
                panel.add(tf_username);

                /*
                JLabel lb_reservaiton = new JLabel("reservation_id");
                lb_reservaiton.setHorizontalAlignment(SwingConstants.CENTER);
                lb_reservaiton.setBounds(76, 39, 74, 19);
                panel.add(lb_reservaiton);

                JTextField tf_reservation = new JTextField();
                tf_reservation.setBounds(171, 39, 260, 19);
                tf_reservation.setColumns(10);
                tf_reservation.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField t = (JTextField)e.getSource();
                        reservation_id = t.getText();
                        t.setText("저장 완료");
                    }
                });
                panel.add(tf_reservation);

                JLabel lb_name = new JLabel("name");
                lb_name.setHorizontalAlignment(SwingConstants.CENTER);
                lb_name.setBounds(76, 39, 74, 19);
                panel.add(lb_reservaiton);

                JTextField tf_name = new JTextField();
                tf_name.setBounds(171, 39, 260, 19);
                tf_name.setColumns(10);
                tf_name.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField t = (JTextField)e.getSource();
                        name = t.getText();
                        t.setText("저장 완료");
                    }
                });
                panel.add(tf_reservation);
                */
            }

            JButton search = new JButton("검색");
            search.setFont(new Font("Gulim", Font.PLAIN, 17));
            search.setBounds(233, 155, 96, 35);
            panel.add(search);
            
            JTextArea ta = new JTextArea();
            contentPanel.add(ta, BorderLayout.CENTER);
            
            search.addActionListener(new ActionListener() {
                
	   			Statement stmt = null;
                
                @Override
                public void actionPerformed(ActionEvent e) {
                  
                    queryBuilder.append("\'");
                    queryBuilder.append(username);
                     queryBuilder.append("\'");
                    queryBuilder.append(" ");
                       

                    String result = queryBuilder.toString();

                    try {
                        
	   					stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		  	  	        ResultSet rs=stmt.executeQuery(result);
		  	  	        int resultCount = 0;

		  	  	        while (rs.next()) {
		  	  	            resultCount++;
		  	  	        }
		  	  	    
                        if (rs == null || resultCount == 0) {
                            JOptionPane.showMessageDialog(null, "예매 정보가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        } else {

                            ResultSetMetaData columns = rs.getMetaData();
                            int columnCount = columns.getColumnCount();

                            var resultModel = new DefaultTableModel(resultCount, 0);
                            for (int i = 2; i <= columnCount; i++) {
                                resultModel.addColumn(columns.getColumnName(i));
                            }
                            
                            rs.first();
                            for (int i = 0; i < resultCount; i++)
                            {
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
                }
            });

            /*
            JButton delete = new JButton("삭제");
            delete.setFont(new Font("Gulim", Font.PLAIN, 17));
            panel.add(delete);

            delete.addActionListener(new ActionListener() {
                
	   			Statement stmt = null;
                
                @Override
                public void actionPerformed(ActionEvent e) {

                    String result = "delete reservaiton where reservation_id = " + reservation_id;

                    try {
	   					stmt = db.con.createStatement();
		  	  	        ResultSet rs=stmt.executeQuery(result);
                        
                        if (rs == null) {
                             ta.append("\n예매 정보가 없습니다.\n");
                        } else {
                             JOptionPane.showMessageDialog(null, "삭제 완료!!");
                        }                   
	   				} catch (SQLException e1) {
                           JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                           e1.printStackTrace();
                        }
                }
            });

            JButton name_update = new JButton("영화 변경");
            name_update.setFont(new Font("Gulim", Font.PLAIN, 17));
            panel.add(name_update);

            name_update.addActionListener(new ActionListener() {
                
	   			Statement stmt = null;
                
                @Override
                public void actionPerformed(ActionEvent e) {

                    String result = "update set name = \'" + name + "\' where = " + reservation_id;

                    try {
	   					stmt = db.con.createStatement();
		  	  	        ResultSet rs=stmt.executeQuery(result);
                        
                        if (rs == null) {
                             ta.append("\n예매 정보가 없습니다.\n");
                        } else {
                             JOptionPane.showMessageDialog(null, "영화 변경 완료!!");
                        }                   
	   				} catch (SQLException e1) {
                           JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                           e1.printStackTrace();
                        }
                }
            });

            JButton date_update = new JButton("상영일정 변경");
            date_update.setFont(new Font("Gulim", Font.PLAIN, 17));
            panel.add(date_update);

            name_update.addActionListener(new ActionListener() {
                
	   			Statement stmt = null;
                
                @Override
                public void actionPerformed(ActionEvent e) {

                    String result = "update set name = \'" + name + "\' where = " + reservation_id;

                    try {
	   					stmt = db.con.createStatement();
		  	  	        ResultSet rs=stmt.executeQuery(result);
                        
                        if (rs == null) {
                             ta.append("\n예매 정보가 없습니다.\n");
                        } else {
                             JOptionPane.showMessageDialog(null, "영화 변경 완료!!");
                        }                   
	   				} catch (SQLException e1) {
                           JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
                           e1.printStackTrace();
                        }
                }
            });
            */
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
