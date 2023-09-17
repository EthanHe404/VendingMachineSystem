package vendingMachineSystem.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FailLogin extends JDialog implements ActionListener {

    public FailLogin() {

        setBounds(150, 120, 300, 150);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("<html>Login Failed!<br>Incorrect Username or Password<br>Try again or Register</html>", SwingConstants.CENTER);
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