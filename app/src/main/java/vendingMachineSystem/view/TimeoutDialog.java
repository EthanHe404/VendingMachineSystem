package vendingMachineSystem.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class TimeoutDialog extends JDialog implements ActionListener {
	
	public TimeoutDialog() {
		
		setSize(200, 100);
		setLayout(new BorderLayout());
		
		JLabel title = new JLabel("Action Timed Out", SwingConstants.CENTER);
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
