package vendingMachineSystem.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import vendingMachineSystem.controller.CardPaymentState;
import vendingMachineSystem.controller.CashPaymentState;
import vendingMachineSystem.controller.LoginState;
import java.util.ArrayList;
import java.util.List;

public class CardPaymentView extends AbstractView {
	CardPaymentState state;
	Dimension size;
	JTextField name;
	JPasswordField number;

	String amountToPay;

	boolean loggedIn;
	boolean SaveCardOrNot;

	public CardPaymentView(CardPaymentState state, boolean loggedIn) {
		this.state = state;
		this.amountToPay = String.valueOf(state.calculateTotal());
		this.loggedIn = loggedIn;
	}

	@Override
	public void display() {
		Window window = Window.getInstance();

		Panel p = new Panel();
		p.setLayout(null);
		JLabel pageLabel = new JLabel(String.format("<html>Total: $%s <br>   <br> Please enter your card details below <html>",amountToPay));
		size = pageLabel.getPreferredSize();
		pageLabel.setBounds(170, 30, size.width, size.height);
		p.add(pageLabel);

		JLabel nameLabel = new JLabel("Cardholder Name:");
		size = nameLabel.getPreferredSize();
		nameLabel.setBounds(70, 130, size.width, size.height);
		p.add(nameLabel);

		name = new JTextField(18);
		name.setBounds(200, 125, 150, 26);
		p.add(name);

		JLabel numberLabel = new JLabel("Card Number:");
		size = numberLabel.getPreferredSize();
		numberLabel.setBounds(70, 160, size.width, size.height);
		p.add(numberLabel);

		number = new JPasswordField(18);
		number.setEchoChar('*');
		number.setBounds(200, 155, 150, 26);
		p.add(number);

		if (loggedIn) {
			JLabel usePreviousLabel = new JLabel(String.format("<html>Or you can pay with the cards you stored before: <html>",amountToPay));
			size = usePreviousLabel.getPreferredSize();
			usePreviousLabel.setBounds(170, 90, size.width, size.height);
			p.add(usePreviousLabel);

			JButton payCardStoredButton = new JButton("Click Here");
			payCardStoredButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					boolean timedout = CardPaymentView.this.state.checkTransactionTimeout();
					if (timedout) {
						return;
					}

					List<String> itemList;
					try {
						itemList = CardPaymentView.this.state.getCardStoredByUser(CardPaymentView.this.state.getUser());
					} catch (SQLException ex) {
						throw new RuntimeException(ex);
					}

					String[] choices = new String[itemList.size()];
					choices = itemList.toArray(choices);
					try {
						Object selectionObject = JOptionPane.showInputDialog(payCardStoredButton,
								"Selected the card you stored: \n Cardholder Name  |  Card Number",
								null, JOptionPane.PLAIN_MESSAGE,
								null, choices, choices[0]);
						String selectionString = selectionObject.toString();
						new FinishedPurchase();
						CardPaymentView.this.state.finishTransaction();
						System.out.println(selectionString);
					}catch (Exception exx){
					}

				}

			});
			size = payCardStoredButton.getPreferredSize();
			payCardStoredButton.setBounds(500, 85, size.width, size.height);
			p.add(payCardStoredButton);
		}


		JButton payButton = new JButton("Pay now");
		payButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean timedout = CardPaymentView.this.state.checkTransactionTimeout();
				if (timedout) {
					return;
				}
				String nameEntered = name.getText();
				String numberEntered = new String(number.getPassword());
				try {
					String numberFound = CardPaymentView.this.state.getCardNumber(nameEntered,numberEntered);
					if (numberEntered.equals(numberFound)){
						if(loggedIn == true) {

							int save = JOptionPane.showConfirmDialog(payButton,
									"Do you want to save your card details for future purchases?",
									null,
									JOptionPane.YES_NO_OPTION,
									JOptionPane.PLAIN_MESSAGE);
							// 0=yes, 1=no, 2=cancel
							if (save == 0) {
								CardPaymentView.this.state.storeCardDetails(CardPaymentView.this.state.getUser(), nameEntered, numberEntered);
								new FinishedPurchase();
								CardPaymentView.this.state.finishTransaction();
							} else if (save == 1) {
								new FinishedPurchase();
								CardPaymentView.this.state.finishTransaction();
							}

						}else{
							new FinishedPurchase();
							CardPaymentView.this.state.finishTransaction();
						}
					}else{
						new FailCardPayment();
					}
				} catch (SQLException ex) {
					new FinishedPurchase();
					CardPaymentView.this.state.cancelTransaction();
				}

			}

		});

		size = payButton.getPreferredSize();
		payButton.setBounds(300, 200, size.width, size.height);
		p.add(payButton);

		JButton CancelButton;
		if(loggedIn){
			CancelButton = new JButton("Log Out");
		}else{
			CancelButton = new JButton("Cancel Transaction");
		}

		CancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardPaymentView.this.state.cancelTransaction();
			}

		});

		size = CancelButton.getPreferredSize();
		CancelButton.setBounds(450, 200, size.width, size.height);
		p.add(CancelButton);

		window.updateWindow(p);

		JButton ReturnButton = new JButton("<<< Return to Purchase Page");
		ReturnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean timedout = CardPaymentView.this.state.checkTransactionTimeout();
				if (timedout) {
					return;
				}
				CardPaymentView.this.state.changeToPurchaseState();
			}

		});

		size = ReturnButton.getPreferredSize();
		ReturnButton.setBounds(60, 200, size.width, size.height);
		p.add(ReturnButton);

		window.updateWindow(p);

	}
	
	private void addTotalPanel(Panel p) {
		Panel totalPanel = new Panel();
		totalPanel.setPreferredSize(new Dimension(650, 20));
		
		JLabel label = new JLabel("Card Payment");
		totalPanel.add(label);
		
		JLabel total = new JLabel("Total");
		totalPanel.add(total);
		
		JLabel totalAmount = new JLabel(String.valueOf(state.calculateTotal()));
		totalPanel.add(totalAmount);
		
		p.add(totalPanel);
	}

}
