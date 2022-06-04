import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
import java.sql.SQLException;

public class SelectSchedule extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private Object[] target;
    private int targetColumnCount;
    private Database db;
    private int movieId;

    public SelectSchedule(Object[] targetToReserve, Database db, int movieId) {
        this.db = db;
        target = targetToReserve;
        targetColumnCount = target.length;

        setBounds(100, 100, 450, 300);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new CardLayout(0, 0));
        {
            JPanel selectSchedule = new JPanel();
            contentPanel.add(selectSchedule, "selectSchedule");
            selectSchedule.setLayout(new BorderLayout(0, 0));

            JScrollPane scrollPane = new JScrollPane();
            selectSchedule.add(scrollPane, BorderLayout.CENTER);

            JLabel lblNewLabel = new JLabel("예매할 영화 시간");
            lblNewLabel.setFont(new Font("Gulim", Font.BOLD, 16));
            lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            selectSchedule.add(lblNewLabel, BorderLayout.NORTH);
            // target에서 영화 id로 시간을 조회해서 Table로 출력한다.
            try {
                var statement = db.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String searchScheduleQuery = "";
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "SQL 쿼리 오류", "ERROR", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                dispose();
            }

        }
        {
            JPanel completedReservation = new JPanel();
            contentPanel.add(completedReservation, "completedReservation");
            {
                JScrollPane scrollPane = new JScrollPane();
                completedReservation.add(scrollPane);

            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton confirmButton = new JButton("예매");
                confirmButton.setActionCommand("OK");
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
