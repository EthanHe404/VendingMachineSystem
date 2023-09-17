package vendingMachineSystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FailCreatingUser extends JDialog implements ActionListener {

    public FailCreatingUser() {

        setBounds(150, 120, 300, 100);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Username duplicated or user's role invalid!", SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JButton close = new JButton("Try again");
        close.addActionListener(this);
        add(close, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        dispose();
    }
}