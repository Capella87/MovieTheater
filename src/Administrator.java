import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.Font;

public class Administrator extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private Database db;

    class Action_modify implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Modify sub = new Modify(db);
            sub.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
            sub.setVisible(true);
            sub.toFront();
            sub.requestFocus();
        }

    }

    public Administrator(Database db) {
        setTitle("관리자 패널");
        this.db = db;
        setBounds(100, 100, 617, 436);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        {
            JLabel lblNewLabel = new JLabel("Menu");
            lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
            lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel.setBounds(265, 47, 99, 55);
            contentPanel.add(lblNewLabel);
        }
        {
            JButton btnNewButton = new JButton("초기화");
            btnNewButton.setActionCommand("");
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton sour = (JButton) e.getSource();
                    if (sour.getText().equals("초기화")) {
                        try {
                            boolean rt = db.resetDatabase();
                            if (rt == false)
                                throw new Exception();
                            JOptionPane.showMessageDialog(btnNewButton, "초기화에 성공했습니다.", "Message",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ee) {
                            JOptionPane.showMessageDialog(btnNewButton, "초기화에 실패했습니다.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    }

                }
            });
            btnNewButton.setBounds(85, 171, 94, 55);
            contentPanel.add(btnNewButton);
        }
        {
            JButton btnNewButton = new JButton("DB 수정");
            btnNewButton.addActionListener(new Action_modify());
            btnNewButton.setBounds(270, 171, 94, 55);
            contentPanel.add(btnNewButton);
        }
        {
            JButton btnNewButton = new JButton("테이블 조회");
            btnNewButton.setBounds(452, 171, 105, 55);
            contentPanel.add(btnNewButton);
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    var SearchAllTable = new SearchAllTable(db);
                    SearchAllTable.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
                    SearchAllTable.setVisible(true);
                }
            });
        }
        {
            JPanel buttonPane = new JPanel();
            FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT);
            buttonPane.setLayout(fl_buttonPane);
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

}
