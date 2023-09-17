package vendingMachineSystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FinishedPurchase extends JDialog implements ActionListener {

    public FinishedPurchase() {

        setBounds(150, 120, 300, 150);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("<html>Thanks for your purchase!<br><br>See you next time!</html>", SwingConstants.CENTER);
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