package vendingMachineSystem.controller;

import java.util.LinkedList;
import java.util.Map;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.TransactionModel;
import vendingMachineSystem.view.PurchaseItemView;

public class PurchaseItemState extends VendingMachineState {

	VendingMachineState prevState;
	int timeoutPeriodSeconds = 120;
	Boolean loggedIn;
	
	public PurchaseItemState(VendingMachine vm, VendingMachineState prevState, Boolean loggedIn) {
		super(vm);
		this.prevState = prevState;
		this.loggedIn = loggedIn;
	}

	@Override
	public void run() {
        PurchaseItemView view = new PurchaseItemView(this);
        view.display();

	}

	public boolean getLoggedInStatus (){
		return loggedIn;
	}
	
	public void changeToCashPaymentPage(Map<String, Integer> itemsToPurchase) {
		vm.setState(new CashPaymentState(vm, itemsToPurchase, this));
	}
	
	public void changeToCardPaymentPage(Map<String, Integer> itemsToPurchase) {
		vm.setState(new CardPaymentState(vm, itemsToPurchase,this, loggedIn));
	}

	public void cancelTransaction() {
		TransactionModel tm = new TransactionModel(vm.getUserName(), "Cancelled");
		tm.addFailedTransaction();
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
	
	public void setTimeout(int sec) {
		this.timeoutPeriodSeconds = sec;
	}
	
}
