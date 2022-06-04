import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.Font;

public class Modify extends JDialog {

    JTextField tf1 = new JTextField(50);
    JTextField tf2 = new JTextField(50);
    JTextField tf3 = new JTextField(50);

    int opernum;

    Database db;
    String SQL_oper;
    String SQL_table;
    String SQL;

    public Modify(Database db) {

        this.db = new Database();
        setTitle("테이블 수정");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container c = getContentPane();
        c.setLayout(new FlowLayout());

        c.add(new JLabel("<수행 할 작업(insert, delete, update)을 작성 후 Enter를 누르시오>"));
        tf1.addActionListener(new ActionListener() {

            PreparedStatement pstmt = null;

            @Override
            public void actionPerformed(ActionEvent e) {

                JTextField t = (JTextField) e.getSource();
                SQL_oper = t.getText();
                t.setText("");

                // 1 : 삽입, 2 : 삭제, 3 : 변경
                if (SQL_oper.equals("insert") | SQL_oper.equals("INSERT")) {
                    opernum = 1;
                } else if (SQL_oper.equals("delete") | SQL_oper.equals("DELETE")) {
                    opernum = 2;
                } else if (SQL_oper.equals("update") | SQL_oper.equals("UPDATE")) {
                    opernum = 3;
                }
            }

        });
        c.add(tf1);

        c.add(new JLabel("<명령할 수행할 table 입력 후 Enter를 누르시오>"));
        tf2.addActionListener(new ActionListener() {

            PreparedStatement pstmt = null;

            @Override
            public void actionPerformed(ActionEvent e) {

                JTextField t = (JTextField) e.getSource();
                SQL_table = t.getText();
                t.setText("");

                if (SQL_table.equals("movies")) {
                    if (opernum == 1) {
                        SQL_table = "insert into movies ";
                    } else if (opernum == 2) {
                        SQL_table = "delete from movies ";
                    } else if (opernum == 3) {
                        SQL_table = "update movies ";
                    }
                } else if (SQL_table.equals("schdules")) {
                    if (opernum == 1) {
                        SQL_table = "insert into schdules ";
                    } else if (opernum == 2) {
                        SQL_table = "delete from schdules ";
                    } else if (opernum == 3) {
                        SQL_table = "update schdules ";
                    }
                } else if (SQL_table.equals("theaters")) {
                    if (opernum == 1) {
                        SQL_table = "insert into theaters ";
                    } else if (opernum == 2) {
                        SQL_table = "delete from theaters ";
                    } else if (opernum == 3) {
                        SQL_table = "update theaters ";
                    }
                } else if (SQL_table.equals("seats")) {
                    if (opernum == 1) {
                        SQL_table = "insert into seats ";
                    } else if (opernum == 2) {
                        SQL_table = "delete from seats ";
                    } else if (opernum == 3) {
                        SQL_table = "update seats ";
                    }
                } else if (SQL_table.equals("reservations")) {
                    if (opernum == 1) {
                        SQL_table = "insert into reservations ";
                    } else if (opernum == 2) {
                        SQL_table = "delete from reservations ";
                    } else if (opernum == 3) {
                        SQL_table = "update reservations ";
                    }
                } else if (SQL_table.equals("members")) {
                    if (opernum == 1) {
                        SQL_table = "insert into members ";
                    } else if (opernum == 2) {
                        SQL_table = "delete from members ";
                    } else if (opernum == 3) {
                        SQL_table = "update members ";
                    }
                } else if (SQL_table.equals("tickets")) {
                    if (opernum == 1) {
                        SQL_table = "insert into tickets ";
                    } else if (opernum == 2) {
                        SQL_table = "delete from tickets ";
                    } else if (opernum == 3) {
                        SQL_table = "update tickets ";
                    }
                }
            }
        });
        c.add(tf2);

        c.add(new JLabel("<쿼리 작성 후 Enter를 누르시오>"));
        tf3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JTextField t = (JTextField) e.getSource();
                SQL = t.getText();
                String result = SQL_table + SQL;

                System.out.println(result);
                try {
                    var pstmt = db.con.prepareStatement(result); // SQL문 처리용 Statement 객체 생성
                    pstmt.executeUpdate();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, "SQL문 실행 오류", "ERROR", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
                /*
                 * catch (SQLException e2) { JOptionPane.showMessageDialog(null, "SQL문 실행 오류",
                 * "ERROR", JOptionPane.ERROR_MESSAGE); e2.printStackTrace(); }
                 */
                t.setText("");
            }
        });

        c.add(tf3);

        setSize(600, 300);

    }
}