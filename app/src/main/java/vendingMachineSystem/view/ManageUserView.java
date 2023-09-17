package vendingMachineSystem.view;

import vendingMachineSystem.controller.DefaultState;
import vendingMachineSystem.controller.ManageUserState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ManageUserView extends AbstractView {

	ManageUserState state;
	boolean showPassword;

	public ManageUserView(ManageUserState state, boolean showPassword) {
		this.state = state;
		this.showPassword = showPassword;
	}

	@Override
	public void display() {
		Window window = Window.getInstance();
		
		Panel p = new Panel();
		//p.setLayout(new BorderLayout());
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		//menu
		JLabel menuLabel = new JLabel("User List");
		menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension size = menuLabel.getPreferredSize();
		menuLabel.setBounds(0,0,size.width,size.height);
		p.add(menuLabel);

		String[][] data = new String[0][];
		try {
			if (showPassword){
				data = ManageUserView.this.state.getAllUsers(ManageUserView.this.state.getUser(),true);
			}else{
				data = ManageUserView.this.state.getAllUsers(ManageUserView.this.state.getUser(),false);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		String[] names = {"Username", "Password", "Role"};
		JTable tab = new JTable(data, names);
		JScrollPane tab_scroller = new JScrollPane(tab);
		tab_scroller.setBounds(0,20,650,125);
		p.add(tab_scroller);



		JCheckBox checkbox = new JCheckBox("Show Password", showPassword);

		checkbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox cbLog = (JCheckBox) e.getSource();
				if (cbLog.isSelected()) {
					ManageUserView.this.state.passwordRefresh(true);

				} else {
					ManageUserView.this.state.passwordRefresh(false);
				}
			}
		});

		p.add(checkbox);

		JPanel buttonPanel = new JPanel();



		JButton addButton = new JButton("Add User");
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//ManageUserView.this.state.changeToPurchaseItemsPage();
				JTextField username = new JTextField(18);
				JPasswordField password = new JPasswordField(18);
				password.setEchoChar('*');
				JTextField type = new JTextField(8);
				Object[] message = {
						"Username:", username,
						"Password:", password,
						"Role (OWNER/SELLER/CASHIER/CUSTOMER)", type
				};

				int option = JOptionPane.showConfirmDialog(addButton, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					String usernameEntered = username.getText().toLowerCase();
					String passwordEntered = new String(password.getPassword());
					String typeEntered = type.getText().toUpperCase();
					if (typeEntered.equals("OWNER") ||
							typeEntered.equals("SELLER") ||
							typeEntered.equals("CASHIER") ||
							typeEntered.equals("CUSTOMER")) {
						Boolean registrationStatus = ManageUserView.this.state.createAccount(usernameEntered, passwordEntered, typeEntered);
						System.out.println(registrationStatus);
						if (registrationStatus) {
							ManageUserView.this.state.refresh();
						} else {
							new FailCreatingUser();
						}
					}else{
						new FailCreatingUser();
					}
				} else {
					System.out.println("Canceled");
				}
			}
			
		});

		buttonPanel.add(addButton);

		JButton assignButton = new JButton("Modify User");
		assignButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//ManageUserView.this.state.changeToPurchaseItemsPage();

				String[] choices = new String[0];
				try {
					choices = ManageUserView.this.state.getAllUsernames(ManageUserView.this.state.getUser());
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
				Object selectionObject = JOptionPane.showInputDialog(assignButton,
						"Selected the user you want to modify:",
						null, JOptionPane.PLAIN_MESSAGE,
						null, choices, choices[0]);

				String selectionString = "";

				try {
					selectionString = selectionObject.toString();
					System.out.println(selectionString);
					String[] userInfo = ManageUserView.this.state.getUserInfo(selectionString);

					JTextField username = new JTextField(userInfo[0],18);
					JPasswordField password = new JPasswordField(userInfo[1],18);
					if (!showPassword) {
						password.setEchoChar('*');
					}else{
						password.setEchoChar((char)0);
					}
					JTextField type = new JTextField(userInfo[2],8);
					Object[] message = {
							"Username:", username,
							"Password:", password,
							"Role (OWNER/SELLER/CASHIER/CUSTOMER)", type
					};

					int option = JOptionPane.showConfirmDialog(assignButton, message, "Modify User", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) {
						String usernameEntered = username.getText().toLowerCase();
						String passwordEntered = new String(password.getPassword());
						String typeEntered = type.getText().toUpperCase();
						if (typeEntered.equals("OWNER") ||
								typeEntered.equals("SELLER") ||
								typeEntered.equals("CASHIER") ||
								typeEntered.equals("CUSTOMER")) {
							ManageUserView.this.state.updateUser(selectionString,usernameEntered,passwordEntered,typeEntered);
							ManageUserView.this.state.refresh();
						}else{
							new FailCreatingUser();
						}
					} else {
						System.out.println("Canceled");
					}
				} catch(NullPointerException ne){
				} catch (SQLException ex) {
				}





			}

		});

		buttonPanel.add(assignButton);
		
		//Login Button
		JButton removeButton = new JButton("Remove User");
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//ManageUserView.this.state.changeToLoginPage();
				boolean timedout = ManageUserView.this.state.checkTransactionTimeout();
				if (timedout) {
					return;
				}
				String[] choices = new String[0];
				try {
					choices = ManageUserView.this.state.getAllUsernames(ManageUserView.this.state.getUser());
				} catch (SQLException ex) {
					throw new RuntimeException(ex);
				}
				Object selectionObject = JOptionPane.showInputDialog(removeButton,
						"Selected the user you want to remove:",
						null, JOptionPane.PLAIN_MESSAGE,
						null, choices, choices[0]);

				String selectionString = "";

				try {
					selectionString = selectionObject.toString();
					System.out.println(selectionString);
					ManageUserView.this.state.removeUser(selectionString);
				} catch(NullPointerException ne){
				} catch (SQLException ex) {
				}
				ManageUserView.this.state.refresh();
			}
			
		});
		buttonPanel.add(removeButton);


		JButton cancelButton = new JButton("Return");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ManageUserView.this.state.returnToLoggedInState();
			}

		});

		buttonPanel.add(cancelButton);

		p.add(buttonPanel);
		
		window.updateWindow(p);
	}

}
