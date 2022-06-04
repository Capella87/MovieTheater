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
public class ReservationInfo extends JDialog
{

    private final JPanel contentPanel = new JPanel();
    private Database db;
    private StringBuilder queryBuilder = new StringBuilder ("select s.*, t.ticket_id, t.seat_id, t.reservation_id, t.username, t.status, t.standard_price, t.sale_price from schedules s, tickets t where s.schedule_id = ");
    private String result;
    private Statement stmt;
    private JTable resultTable;

    public ReservationInfo(Database db, Object[] bookingInfo)
    {
        setTitle("상세 정보 조회");
        setBounds(100, 100, 450, 373);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            JPanel panel = new JPanel();
            contentPanel.add(panel, BorderLayout.NORTH);
            
            {
                JLabel lblNewLabel = new JLabel("상영 정보");
                lblNewLabel.setFont(new Font("Gulim", Font.BOLD, 15));
                lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(lblNewLabel, BorderLayout.NORTH);
            }
            panel.setLayout(new BorderLayout(0, 0));
            {
                JScrollPane scrollPane = new JScrollPane();
                            
                queryBuilder.append(bookingInfo[4]);
                queryBuilder.append(" and ");
                queryBuilder.append("t.schedule_id = ");
                queryBuilder.append(bookingInfo[4]);

                result = queryBuilder.toString();

                try {
                    stmt = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet rs = stmt.executeQuery(result);

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
                                            
                } catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, "SQL문 실행 오류");
						e1.printStackTrace();
				}
                resultTable.setVisible(true);
                panel.add(scrollPane, BorderLayout.CENTER);
            }
        }

        {
            JPanel buttonPane = new JPanel();
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            buttonPane.setLayout(new BorderLayout(0, 0));
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
    }

}
