import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SelectSchedule extends JDialog
{

    private final JPanel contentPanel = new JPanel();

    /**
     * Launch the application.
     */
    /*
    public static void main(String[] args)
    {
        try
        {
            SelectSchedule dialog = new SelectSchedule();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    */

    /**
     * Create the dialog.
     */
    public SelectSchedule()
    {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new CardLayout(0, 0));
        {
            JPanel selectSchedule = new JPanel();
            contentPanel.add(selectSchedule, "selectSchedule");
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
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

}
