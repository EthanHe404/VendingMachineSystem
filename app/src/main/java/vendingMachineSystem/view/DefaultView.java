package vendingMachineSystem.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import vendingMachineSystem.controller.DefaultState;

public class DefaultView extends AbstractView {

	DefaultState state;
	
	public DefaultView(DefaultState state) {
		this.state = state;
	}

	@Override
	public void display() {
		//TODO Example code
		Window window = Window.getInstance();
		
		Panel p = new Panel();
		//p.setLayout(new BorderLayout());
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		//menu
		JLabel menuLabel = new JLabel("Menu");
		menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		Dimension size = menuLabel.getPreferredSize();
		menuLabel.setBounds(0,0,size.width,size.height);
		p.add(menuLabel);
		String[][] data = state.getItemData(false);
		String[] names = {"Category", "Item", "Quantity", "Price"};
		JTable tab = new JTable(data, names);
		JScrollPane tab_scroller = new JScrollPane(tab);
		tab_scroller.setBounds(0,20,650,125);
		p.add(tab_scroller);

		//recent purchases
		String[][] rec_data;
		rec_data = state.getRecentData();
		if ( rec_data != null ) {
			JLabel recLabel = new JLabel("Recent Purchases");
			recLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			Dimension rec_size = recLabel.getPreferredSize();
			recLabel.setBounds(0, 150, rec_size.width, rec_size.height);
			p.add(recLabel);
			String[] rec_names = {"", "Item"};
			JTable rec_tab = new JTable(rec_data, rec_names);
			JScrollPane rec_tab_scroller = new JScrollPane(rec_tab);
			rec_tab_scroller.setBounds(0, 170, 650, 75);
			p.add(rec_tab_scroller);
		}
		JPanel buttonPanel = new JPanel();
		
		//Purchase Button
		JButton purchaseButton = new JButton("Purchase Items");
		purchaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultView.this.state.changeToPurchaseItemsPage();
			}
			
		});
		
//		purchaseButton.setBounds( 150, 250, 350, 25 );
		buttonPanel.add(purchaseButton);
		
		//Login Button
		JButton loginButton = new JButton("Log In/Register");
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultView.this.state.changeToLoginPage();
			}
			
		});

//		loginButton.setBounds( 150, 250, 350, 25 );
		buttonPanel.add(loginButton);
		
		p.add(buttonPanel);
		
		window.updateWindow(p);
	}

}
