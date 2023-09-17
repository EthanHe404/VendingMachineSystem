package vendingMachineSystem.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;

import vendingMachineSystem.controller.*;

public class LoginView extends AbstractView {

	LoginState state;
	Dimension size;
	JTextField username;
	JPasswordField password;

	public LoginView(LoginState state) {
		this.state = state;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void display() {
		// TODO Auto-generated method stub
		Window window = Window.getInstance();
		
		Panel p = new Panel();
		p.setLayout(null);
		JLabel pageLabel = new JLabel("Welcome! Please Log In Below.");
		size = pageLabel.getPreferredSize();
		pageLabel.setBounds(100, 30, size.width, size.height);
		p.add(pageLabel);

		JLabel usernameLabel = new JLabel("Username:");
		size = usernameLabel.getPreferredSize();
		usernameLabel.setBounds(70, 70, size.width, size.height);
		p.add(usernameLabel);

		username = new JTextField(18);
		username.setBounds(140, 65, 150, 26);
		p.add(username);

		JLabel passwordLabel = new JLabel("Password:");
		size = passwordLabel.getPreferredSize();
		passwordLabel.setBounds(70, 100, size.width, size.height);
		p.add(passwordLabel);

		password = new JPasswordField(18);
		password.setEchoChar('*');
		password.setBounds(140, 95, 150, 26);
		p.add(password);


		JButton loginButton = new JButton("Log In");
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean timedout = LoginView.this.state.checkTransactionTimeout();
				if (timedout) {
					return;
				}
				String usernameEntered = username.getText().toLowerCase();
				String passwordEntered = new String(password.getPassword());
				try {
					String passwordFound = LoginView.this.state.getPassword(usernameEntered);
					if (passwordEntered.equals(passwordFound)){
						String type = LoginView.this.state.getUserType(usernameEntered);
						LoginView.this.state.changeToLoggedInPage(usernameEntered, type);
					}else{
						new FailLogin();
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}

			}
			
		});

		size = loginButton.getPreferredSize();
		loginButton.setBounds(150, 120, size.width, size.height);
		p.add(loginButton);

		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean timedout = LoginView.this.state.checkTransactionTimeout();
				if (timedout) {
					return;
				}
				LoginView.this.state.changeToRegistrationPage();
			}

		});

		size = registerButton.getPreferredSize();
		registerButton.setBounds(150, 150, size.width, size.height);
		p.add(registerButton);

		JButton CancelButton = new JButton("Cancel");
		CancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LoginView.this.state.cancelTransaction();
			}

		});

		size = CancelButton.getPreferredSize();
		CancelButton.setBounds(300, 200, size.width, size.height);
		p.add(CancelButton);
		
		window.updateWindow(p);
	}

}
