package vendingMachineSystem.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.*;
import vendingMachineSystem.view.TimeoutDialog;

public abstract class VendingMachineState {
	
	VendingMachine vm;
	LocalDateTime lastAction;
	
	public VendingMachineState(VendingMachine vm) {
		this.vm = vm;
		this.lastAction = LocalDateTime.now();
	}
	
	public abstract void run();
	
	/**
	 * Switches state to the default state if action has timed out
	 * (Timeout is >120 sec since last action was taken)
	 * @return boolean true if timed out otherwise false
	 */
	public boolean checkTimedOut() {
		return checkTimedOut(120);
	}
	
	/**
	 * Switches state to the default state if action has timed out
	 * @param seconds time since last action was performed
	 * @return boolean true if timed out otherwise false
	 */
	public boolean checkTimedOut(long seconds) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		if (currentDateTime.minusSeconds(seconds).compareTo(lastAction) > 0) {
			this.cancelTransaction();
			new TimeoutDialog();
			return true;
		}
		else
			this.lastAction = currentDateTime;
		return false;
	}
	
	public void cancelTransaction() {
		this.vm.setState(new DefaultState(vm));
	}
	
	public String readInput() {
		return "0";
	}
	
	public VendingMachine getVm() {
		return this.vm;
	}

	public String[][] getRecentData(String username){ // STUB TODO: implement
		TransactionModel tm = new TransactionModel();
		List<RecentTransaction> ls;
		ls = tm.getRecentTransactions(username);
		if (ls == null) return null; // if no history, do nothing
		String[][] ret = new String[ls.size()][2];
		for (int i = 0; i < ls.size(); i++){
			ret[i][0] = Integer.toString(i+1);
			ret[i][1] = ls.get(i).getItemName();
		}
		return ret;
	}
	public String[][] getRecentData(){
		return getRecentData(null);
	}

	public String[][] getItemData(){ // original function that does not return id (overloading as many usages already)
		return getItemData(true);
	}
	public String[][] getItemData(boolean needs_id){
		// get products
		DataModel dm = new DataModel(false);
		List<Product> ls;
		try {
			ls = dm.allProducts();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		int arraySize;
		if (needs_id) {arraySize = 5;}
		else {arraySize = 4;}

		// now get 2d list of items needed
		// category, item, quantity, price
		String[][] ret = new String[ls.size()][arraySize];
		for ( int prod_n = 0; prod_n < ls.size(); prod_n++ ){
			ret[prod_n][0] = ls.get(prod_n).getCategory();
			ret[prod_n][1] = ls.get(prod_n).getName();
			ret[prod_n][2] = Integer.toString(ls.get(prod_n).getQuantity());
			ret[prod_n][3] = Float.toString(ls.get(prod_n).getPrice());

			if (needs_id){
				ret[prod_n][4] = Integer.toString(ls.get(prod_n).getId());
			}

		}

		return ret;
	}

	public String[][] getCashData(){
		return getCashData(true);
	}

	public String[][] getCashData(boolean needValue){
		// get changes
		ChangeModel cm = new ChangeModel(false);
		List<Change> changes;
		try {
			changes = cm.allChanges();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// now get 2d list of cash needed
		// name, value, quantity
		int ret_subsize = 2;
		if (needValue){ ret_subsize = 3; }
		String[][] ret = new String[changes.size()][ret_subsize];

		for ( int n = 0; n < changes.size(); n++ ){
			ret[n][0] = changes.get(n).getName();
			ret[n][1] = Integer.toString(changes.get(n).getQty());
			if (needValue) ret[n][2] = Double.toString(changes.get(n).getValue());
		}

		return ret;
	}

	public void updateCash(String name, String newQty) {
		ChangeModel cm = new ChangeModel(false);
		try{
			cm.updateCash(name, newQty);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public String[][] getFailedData(){
		TransactionModel tm = new TransactionModel();
		List<FailedTrans> fails;
		fails = tm.getFailedReport();

		if (fails == null) return null;

		String[][] ret = new String[fails.size()][3];
		for ( int n = 0; n < fails.size(); n++ ){
			ret[n][0] = fails.get(n).getWhen();
			ret[n][1] = fails.get(n).getName();
			ret[n][2] = fails.get(n).getWhy();
		}
		return ret;
	}

	//when,item,paid,change,method
	public String[][] getSummData(){
		TransactionModel tm = new TransactionModel();
		List<Summ> summary;
		summary = tm.getSummReport();

		if (summary == null) return null;

		String[][] ret = new String[summary.size()][5];
		for ( int n = 0; n < summary.size(); n++ ){
			ret[n][0] = summary.get(n).getWhen();
			ret[n][1] = summary.get(n).getItem();
			ret[n][2] = summary.get(n).getPaid();
			ret[n][3] = summary.get(n).getChange();
			ret[n][4] = summary.get(n).getMethod();
		}
		return ret;
	}

	public String[][] getISummData(){
		TransactionModel tm = new TransactionModel();
		List<ISumm> summary;
		summary = tm.getISummReport();

		if (summary == null) return null;

		String[][] ret = new String[summary.size()][3];
		for ( int n = 0; n < summary.size(); n++ ){
			ret[n][0] = Integer.toString(summary.get(n).getId());
			ret[n][1] = summary.get(n).getName();
			ret[n][2] = Integer.toString(summary.get(n).getQuantity());
		}
		return ret;
	}
	public String[][] getUserReport(){
		UserModel um = new UserModel();
		List<User> users;
		try{
			users = um.getUserReport();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		String[][] ret = new String[users.size()][2];
		for ( int n = 0; n < users.size(); n++ ){
			ret[n][0] = users.get(n).getUsername();
			ret[n][1] = users.get(n).getRole();
		}
		return ret;
	}
	
}