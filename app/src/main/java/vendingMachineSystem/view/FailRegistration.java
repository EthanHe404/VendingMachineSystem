package vendingMachineSystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FailRegistration extends JDialog implements ActionListener {

    public FailRegistration() {

        setBounds(150, 120, 300, 100);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Please try again with a different username!", SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JButton close = new JButton("Close");
        close.addActionListener(this);
        add(close, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        dispose();
    }
}