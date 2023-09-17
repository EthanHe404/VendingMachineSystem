package vendingMachineSystem.controller;

import java.sql.SQLException;
import java.util.*;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.CardModel;
import vendingMachineSystem.model.TransactionModel;
import vendingMachineSystem.view.CardPaymentView;

public class CardPaymentState extends VendingMachineState {

	Map<String, Integer> itemsToPurchase;
	String[][] itemData;
	String[][] changeData;
	private VendingMachineState prevState;

	int timeoutPeriodSeconds = 120;

	Boolean loggedIn;
	
	public CardPaymentState(VendingMachine vm, Map<String, Integer> itemsToPurchase,VendingMachineState prevState, boolean loggedIn) {
		super(vm);
		this.itemsToPurchase = itemsToPurchase;
		itemData = super.getItemData();
		changeData = super.getCashData();
		this.prevState = prevState;
		this.loggedIn =loggedIn;
	}

	@Override
	public void run() {
		CardPaymentView view = new CardPaymentView(this,loggedIn);
		view.display();
	}
	
	public float calculateTotal() {
		float total = 0;
		for (Map.Entry<String, Integer> entry: itemsToPurchase.entrySet()) {
			String itemName = entry.getKey();
			int qty = entry.getValue();
			for (String[] itemLine: itemData) {
				if (itemName.equals(itemLine[1])) {
					total += Float.parseFloat(itemLine[3]) * qty;
				}
			}
			
		}
		return total;
	}

	public String getCardNumber(String name, String cardNumber) throws SQLException {
		CardModel cardDB = new CardModel();
		String number = cardDB.getCardNumber(name, cardNumber);
		return number;
	}

	public void storeCardDetails(String username, String cardName, String cardNum) throws SQLException {
		CardModel cardDB = new CardModel();
		cardDB.storeCardDetails(username, cardName, cardNum);
	}

	public List<String> getCardStoredByUser(String username) throws SQLException{
		CardModel cardDB = new CardModel();
		List<String> cardStored = cardDB.getCardStoredByUser(username);
		return cardStored;
	}

	public void changeToPurchaseState(){
		vm.setState(prevState);
	}

	public boolean checkTransactionTimeout() {
		boolean timedout = super.checkTimedOut(timeoutPeriodSeconds);
		if (timedout) {
			TransactionModel tm = new TransactionModel(vm.getUserName(), "Timed out");
			tm.addFailedTransaction();
		}
		return timedout;
	}

	public String getUser(){
		return vm.getUserName();
	}

	public void setTimeout(int sec) {
		this.timeoutPeriodSeconds = sec;
	}
	
	public void finishTransaction() {
		String user = super.getVm().getUserName();
		float moneyPaid = calculateTotal();
		TransactionModel tm = new TransactionModel(user, moneyPaid, 0, "card", itemsToPurchase);
		tm.addSuccessfulTransaction();
		vm.setState(new DefaultState(vm));
	}

}
