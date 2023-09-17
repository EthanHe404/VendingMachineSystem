package vendingMachineSystem.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

import org.checkerframework.common.subtyping.qual.Bottom;
import vendingMachineSystem.controller.CashPaymentState;
import vendingMachineSystem.model.Change;
import vendingMachineSystem.model.ChangeModel;

import static java.lang.Math.round;

public class CashPaymentView extends AbstractView {
	
	CashPaymentState state;
	private float payment;
	private boolean success;
	private ArrayList<Change> income;
	
	
	public CashPaymentView(CashPaymentState state) {
		this.state = state;
	}

	@Override
	public void display() {
		Window window = Window.getInstance();
		
		Panel p = new Panel();
//		BoxLayout layout = new BoxLayout(p, BoxLayout.Y_AXIS);
//		p.setLayout(layout);
		p.setLayout(null);
		
		this.addTotalPanel(p);

		addPaymentButtons(p);

		confirmPayment(p);

		window.updateWindow(p);
	}
	
	private void addTotalPanel(Panel p) {
//		Panel totalPanel = new Panel();
//		totalPanel.setPreferredSize(new Dimension(650, 20));
		
		JLabel label = new JLabel("Cash Payment");
		Dimension size = label.getPreferredSize();
		label.setBounds(280, 10, size.width, size.height);
		p.add(label);
		
		JLabel total = new JLabel("Total Price:");
		Dimension sizeTotal = label.getPreferredSize();
		total.setBounds(30, 30, sizeTotal.width, sizeTotal.height);
		p.add(total);
		
		JLabel totalAmount = new JLabel(String.valueOf(state.calculateTotal()));
		size = label.getPreferredSize();
		totalAmount.setBounds(30 + sizeTotal.width,30, size.width, size.height);
		p.add(totalAmount);
		
//		p.add(totalPanel);
	}

	public void addPaymentButtons(Panel p){
		JButton doneButton = new JButton("Done");
		doneButton.setBounds(560 - 100,220,100,40);
		//doneButton.setFont(new Font("Arial", Font.PLAIN, 10));
		doneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (success) {
					if (payment == state.calculateTotal()){
						new FinishedPurchase();
						CashPaymentView.this.state.finishTransaction(0);
					} else{
						new FinishedPurchase();
						CashPaymentView.this.state.finishTransaction(payment - state.calculateTotal());
					}
				} else{
					new FailDoneCashPay();
				}
			}
		});
		p.add(doneButton);

		// cancel
		JButton returnButton = new JButton("Return");
		returnButton.setBounds(560,220,100,40);
		//cancelButton.setFont(new Font("Arial", Font.PLAIN, 10));
		returnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) {
					System.out.println("Too late to cancel! Payment done!");
					new FailReturnAfterCashPay();
					return;
				}
				CashPaymentView.this.state.changeToPurchaseState();
			}
		});
		p.add(returnButton);


		JLabel amountLabel = new JLabel("Current Payment Amount:");
		Dimension sizeAmount = amountLabel.getPreferredSize();
		amountLabel.setBounds(30, 150, sizeAmount.width, sizeAmount.height);
		p.add(amountLabel);

		JLabel paymentLabel = new JLabel(String.valueOf(payment));
		Dimension paymentSize = new Dimension(100, sizeAmount.height);
		paymentLabel.setBounds(30 + sizeAmount.width + 5, 150, paymentSize.width, paymentSize.height);
		p.add(paymentLabel);

		JLabel payLabel = new JLabel("Select Payment");
		Dimension sizePay = payLabel.getPreferredSize();
		payLabel.setBounds(30, 50, sizePay.width, sizePay.height);
		p.add(payLabel);

		ChangeModel cm = new ChangeModel(false);
		try{
			income = cm.allChanges();
		} catch (SQLException e){
			e.printStackTrace();
		}
		for (Change c: income) c.setQty(0);


		JButton button100 = new JButton("$100");
		Dimension size100 = button100.getPreferredSize();
		button100.setBounds(30,sizePay.height + 50 + 10, size100.width, size100.height);
		button100.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("$100");
				payment += 100;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button100);

		JButton button50 = new JButton("$50");
		Dimension size50 = button50.getPreferredSize();
		button50.setBounds(30 + size100.width,sizePay.height + 60, size50.width, size50.height);
		button50.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("$50");
				payment += 50;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button50);

		JButton button20 = new JButton("$20");
		Dimension size20 = button20.getPreferredSize();
		button20.setBounds(30 + size100.width + size50.width,sizePay.height + 60, size20.width, size20.height);
		button20.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("$20");
				payment += 20;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button20);

		JButton button10 = new JButton("$10");
		Dimension size10 = button10.getPreferredSize();
		button10.setBounds(30 + size100.width + size50.width +size20.width,sizePay.height + 60, size10.width, size10.height);
		button10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("$10");
				payment += 10;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button10);

		JButton button5 = new JButton("$5");
		Dimension size5 = button5.getPreferredSize();
		button5.setBounds(30 + size100.width + size50.width +size20.width +size10.width,sizePay.height + 60, size5.width, size5.height);
		button5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("$5");
				payment += 5;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button5);

		JButton button2 = new JButton("$2");
		Dimension size2 = button2.getPreferredSize();
		button2.setBounds(30 + size100.width + size50.width +size20.width +size10.width + size5.width,sizePay.height + 60, size2.width, size2.height);
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("$2");
				payment += 2;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button2);

		JButton button1 = new JButton("$1");
		Dimension size1 = button1.getPreferredSize();
		button1.setBounds(30 + size100.width + size50.width +size20.width +size10.width + size5.width + size2.width,sizePay.height + 60, size1.width, size1.height);
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("$1");
				payment += 1;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button1);

		JButton button50c = new JButton("50c");
		Dimension size50c = button50c.getPreferredSize();
		button50c.setBounds(30 ,sizePay.height + 60 + size100.height, size50c.width, size50c.height);
		button50c.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("50c");
				payment += 0.5;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button50c);

		JButton button20c = new JButton("20c");
		Dimension size20c = button20c.getPreferredSize();
		button20c.setBounds(30 + size50c.width,sizePay.height + 60 + size100.height, size20c.width, size20c.height);
		button20c.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("20c");
				payment += 0.2;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button20c);

		JButton button10c = new JButton("10c");
		Dimension size10c = button10c.getPreferredSize();
		button10c.setBounds(30 + size50c.width + size20c.width,sizePay.height + 60 + size100.height, size10c.width, size10c.height);
		button10c.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("10c");
				payment += 0.1;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button10c);

		JButton button5c = new JButton("5c");
		Dimension size5c = button5c.getPreferredSize();
		button5c.setBounds(30 + size50c.width + size20c.width + size10c.width,sizePay.height + 60 + size100.height, size5c.width, size5c.height);
		button5c.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				addOneChange("5c");
				payment += 0.05;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(button5c);

		JButton clearButton = new JButton("Clear");
		Dimension clearSize = clearButton.getPreferredSize();
		clearButton.setBounds(200,180,clearSize.width,clearSize.height);
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (success) return;
				clearAllIncome();
				payment = 0;
				paymentLabel.setText(String.valueOf(round(payment * 100.0) / 100.0));
			}
		});
		p.add(clearButton);



	}

	public void addOneChange(String name){
		for (Change c: income){
			if (c.getName().equals(name)){
				c.setQty(c.getQty() + 1);
			}
		}
	}

	public void clearAllIncome(){
		for (Change c : income){
			c.setQty(0);
		}
	}

	public void confirmPayment(Panel p){
		JLabel change = new JLabel();
//		Dimension sizeChange = change.getPreferredSize();
//		change.setBounds(30, 210, sizeChange.width, sizeChange.height);
		change.setBounds(30, 210, 600, 25);
		p.add(change);

		JButton confirm = new JButton("Confirm Payment");
		Dimension size = confirm.getPreferredSize();
		confirm.setBounds(30,180, size.width, size.height);
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean timedout = CashPaymentView.this.state.checkTransactionTimeout();
				if (timedout) {
					return;
				}

				if (success) return;

				if (payment < state.calculateTotal()){
					float shortage = state.calculateTotal() - payment;
					new FailPayment(shortage);

				} else if (payment == state.calculateTotal()) {
					success = true;
					change.setText("Payment Success! No Changes Need.");
					addIncomeToMachine();

				} else{
					float extra = payment - state.calculateTotal();
					success = true;

					//gives back the changes
					ArrayList<Change> changes = returnChanges((double) extra);

					if (success) {
						//display changes to customer
						displayChanges(changes, change);

						//add income cash to machine after return change successfully
						addIncomeToMachine();
					} else{
						change.setText("Payment Fail, not enough changes in Vending Machine.");
					}




				}
			}
		});
		p.add(confirm);



	}

	public ArrayList<Change> returnChanges(double extra){
		//check available cash in the machine
		ChangeModel cm = new ChangeModel(false);
		ArrayList<Change> cash;
		ArrayList<Change> changes;
		try{
			cash = cm.allChanges();
			changes = cm.allChanges();
		} catch (SQLException e){
			throw new RuntimeException(e);
		}

		//set quantity of all change in changes list to zero
		for (Change c: changes){
			c.setQty(0);
		}

		while (extra > 0.05){
			for (Change change: cash){
				//find the first available cash that is smaller than extra
				if (round(change.getValue() * 100.0)/100.0 <= round(extra * 100.0)/100.0 && change.getQty() > 0){
					//took away the cash from machine
					extra -= round(change.getValue() * 100.0)/100.0;
					extra = round(extra * 100.0)/100.0;
					change.setQty(change.getQty() - 1);

//					System.out.println("extra: "+extra);

					//add the cash to changes
					for (Change c: changes) {
						if (c.getName().equals(change.getName())){
							c.setQty(c.getQty() + 1);
						}
					}
					break;
				}
			}

			boolean hasChange = false;
			//check all cash smaller than extra, all qty = 0 means not enough change, fail!
			for (Change c: cash){
				if (round(c.getValue() * 100.0)/100.0 <= round(extra * 100.0)/100.0 && c.getQty() > 0){
					hasChange = true;
				}
			}
			if (!hasChange && extra > 0.05){
				System.out.println("Payment Fail, Not Enough Change.");
				success = false;
				break;
			}


		}

//		//update the Change table in database
		for (Change c: cash){
			CashPaymentView.this.state.updateCash(c.getName(), Integer.toString(c.getQty()));
		}


		return changes;
	}

	public void displayChanges(ArrayList<Change> changes, JLabel changeLabel){
		String msg = "Payment Success! Changes:";
		for (Change c: changes){
//			System.out.println(c.getName() +" "+ c.getQty());
			if (c.getQty() > 0){
				msg += c.getName() +": "+ c.getQty() + "  ";
			}
		}

		changeLabel.setText(msg);

	}

	public void addIncomeToMachine(){
		ChangeModel cm = new ChangeModel(false);
		ArrayList<Change> original;
		try{
			original = cm.allChanges();
			for (int i = 0; i < income.size(); i++){
				int newQty = income.get(i).getQty() + original.get(i).getQty();
				CashPaymentView.this.state.updateCash(income.get(i).getName(), Integer.toString(newQty));
			}
		} catch (SQLException ec){
			ec.printStackTrace();
		}
	}



}
