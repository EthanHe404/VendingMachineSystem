package vendingMachineSystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FailReturnAfterCashPay extends JDialog implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        dispose();
    }

    public FailReturnAfterCashPay(){
        setBounds(150, 120, 400, 100);
        setLayout(new BorderLayout());

        String msg = String.format("Payment confirmed, too late to cancel. Please click Done.");
        JLabel title = new JLabel(msg, SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JButton close = new JButton("Close");
        close.addActionListener(this);
        add(close, BorderLayout.SOUTH);

        setVisible(true);
    }
}
