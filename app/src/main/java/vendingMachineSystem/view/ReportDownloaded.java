package vendingMachineSystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ReportDownloaded extends JDialog implements ActionListener {

    public ReportDownloaded() {

        setBounds(150, 120, 300, 150);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("<html>Report Downloaded!<br>  <br>Please check source folder!</html>", SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JButton close = new JButton("Close");
        close.addActionListener(this);
        add(close, BorderLayout.SOUTH);

        setVisible(true);
        setAlwaysOnTop(true);
    }

    public void actionPerformed(ActionEvent e) {
        dispose();
    }
}