import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class Credit extends JDialog {

    private final JPanel contentPanel = new JPanel();
    
    public Credit() {
        setResizable(false);
        setTitle("Credit");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        {
            JLabel lblNewLabel = new JLabel("CREDIT");
            lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
            lblNewLabel.setBounds(149, 28, 117, 26);
            contentPanel.add(lblNewLabel);
        }
        {
            JLabel lblNewLabel_1 = new JLabel("제작: ");
            lblNewLabel_1.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_1.setBounds(91, 65, 64, 17);
            contentPanel.add(lblNewLabel_1);
        }
        {
            JLabel lblNewLabel_1 = new JLabel("Capella87 and ParkRootSeok");
            lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_1.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            lblNewLabel_1.setBounds(149, 65, 195, 17);
            contentPanel.add(lblNewLabel_1);
        }
        {
            JLabel lblNewLabel_1 = new JLabel("기간: ");
            lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_1.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            lblNewLabel_1.setBounds(91, 93, 64, 17);
            contentPanel.add(lblNewLabel_1);
        }
        {
            JLabel lblNewLabel_1 = new JLabel("2022.05.31-2022.06.05");
            lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_1.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            lblNewLabel_1.setBounds(149, 93, 150, 17);
            contentPanel.add(lblNewLabel_1);
        }
        {
            JLabel lblNewLabel_1 = new JLabel("MovieTheater v1.0.0");
            lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_1.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            lblNewLabel_1.setBounds(124, 121, 173, 17);
            contentPanel.add(lblNewLabel_1);
        }
        {
            JLabel lblNewLabel_1 = new JLabel("Released on 2022.06.07");
            lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_1.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            lblNewLabel_1.setBounds(124, 147, 173, 17);
            contentPanel.add(lblNewLabel_1);
        }
        {
            JLabel lblNewLabel_1 = new JLabel("github.com/Capella87/MovieTheater");
            lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_1.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            lblNewLabel_1.setBounds(91, 175, 250, 17);
            contentPanel.add(lblNewLabel_1);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
    }

}
