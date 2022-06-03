import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class Customer extends JDialog
{

    private final JPanel contentPanel = new JPanel();
    private Database db;

    /**
     * Create the dialog.
     */
    public Customer(Database db)
    {
        this.db = db;
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        {
            JButton btnNewButton = new JButton("조회 및 예매");
            btnNewButton.setBounds(59, 90, 108, 21);
            contentPanel.add(btnNewButton);
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    var a = new SearchMovie(db);
                    a.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
                    a.setVisible(true);
                }
            });
        }
        {
            JButton btnNewButton = new JButton("예매 조회");
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    var searchReservation = new SearchReservation(db);
                    searchReservation.setModalityType(JDialog.ModalityType.APPLICATION_MODAL);
                    searchReservation.setVisible(true);
                }
            });
            btnNewButton.setBounds(252, 90, 85, 21);
            contentPanel.add(btnNewButton);
        }
        {
            JLabel lblNewLabel = new JLabel("안녕하세요");
            lblNewLabel.setFont(new Font("Gulim", Font.BOLD, 20));
            lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel.setBounds(150, 33, 116, 33);
            contentPanel.add(lblNewLabel);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton cancelButton = new JButton("Close");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // this.();
                    }
                });
            }
        }
    }

}
