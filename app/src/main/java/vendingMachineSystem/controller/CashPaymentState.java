package vendingMachineSystem.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.TransactionModel;
import vendingMachineSystem.view.CashPaymentView;


public class CashPaymentState extends VendingMachineState {

	Map<String, Integer> itemsToPurchase;
	private VendingMachineState prevState;
	//the prevState is the purchaseItemState
	String[][] itemData;
	String[][] changeData;
	int timeoutPeriodSeconds = 120;
	
	public CashPaymentState(VendingMachine vm, Map<String, Integer> itemsToPurchase, VendingMachineState prevState) {
		super(vm);
		this.itemsToPurchase = itemsToPurchase;
		itemData = super.getItemData();
		changeData = super.getCashData();
		this.prevState = prevState;
	}

	@Override
	public void run() {
		CashPaymentView view = new CashPaymentView(this);
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

		DecimalFormat df = new DecimalFormat("#.##");
//		df.format(total);
		total = Float.parseFloat(df.format(total));

		BigDecimal convertTotal = round(new BigDecimal(String.valueOf(total)));

		return convertTotal.floatValue();
//		return total;
	}
	
	public String[][] getChangeData() {
		return changeData;
	}

	public void changeToPurchaseState(){
		vm.setState(prevState);
	}

	public static BigDecimal round(BigDecimal value) {
		RoundingMode roundingMode = RoundingMode.UP;
		BigDecimal increment = new BigDecimal("0.05");

		if (increment.signum() == 0) {
			// 0 increment does not make much sense, but prevent division by 0
			return value;
		} else {
			BigDecimal divided = value.divide(increment, 0, roundingMode);
			BigDecimal result = divided.multiply(increment);
			return result;
		}
	}
	
	public void finishTransaction(float change) {
		String user = super.getVm().getUserName();
		float moneyPaid = calculateTotal();
		
		//TODO moneyPaid and change 
		TransactionModel tm = new TransactionModel(user, moneyPaid, change, "cash", itemsToPurchase);
		tm.addSuccessfulTransaction();
		
		//TODO update change
		vm.setState(new DefaultState(vm));
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
