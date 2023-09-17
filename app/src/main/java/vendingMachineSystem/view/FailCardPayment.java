package vendingMachineSystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FailCardPayment extends JDialog implements ActionListener {

    public FailCardPayment() {

        setBounds(150, 120, 300, 150);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("<html>Payment Failed!<br>Incorrect Cardholder Name or Card Number<br>  Try again</html>", SwingConstants.CENTER);
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