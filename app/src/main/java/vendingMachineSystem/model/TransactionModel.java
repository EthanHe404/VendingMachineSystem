package vendingMachineSystem.model;

import vendingMachineSystem.VendingMachine;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TransactionModel {

    Database db = Database.getInstance();
    String user;
    String reason;
    float moneyPaid;
    float returnedChange;
    String paymentMethod;
    Map<String, Integer> purchasedItems;
    
    public TransactionModel(){

    }
    
    public TransactionModel(String user, String reason) {
    	this.user = user;
    	this.reason = reason;
    }

    public TransactionModel(String user, float moneyPaid, float returnedChange, String paymentMethod, Map<String, Integer> purchasedItems) {
    	this.user = user;
    	this.moneyPaid = moneyPaid;
    	this.returnedChange = returnedChange;
    	this.paymentMethod = paymentMethod;
    	this.purchasedItems = purchasedItems;
    }
    
    public void addFailedTransaction() {
    	try {
    		db.addFailedTransaction(user, reason);    		
    	} catch (SQLException e) {
    		System.err.println(e);
    	}
    }
    
    public void addSuccessfulTransaction() {
    	try {
    		long transactionId = db.addSuccessfulTransaction(user, moneyPaid, returnedChange, paymentMethod);
    		for (Map.Entry<String, Integer> entry: purchasedItems.entrySet()) {
    			db.addTransactionItems(transactionId, entry.getKey(), entry.getValue());
    			db.removeProductQty(entry.getKey(), entry.getValue());
    		}
    	} catch (SQLException e) {
    		System.err.println(e);
    	}
    }

	public List<RecentTransaction> getRecentTransactions(String username){
		try {
			return db.getAllRecent(username);
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public List<FailedTrans> getFailedReport(){
		try {
			return db.getFailed();
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public List<Summ> getSummReport(){
		try {
			return db.getSumm();
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}

	public List<ISumm> getISummReport(){
		try {
			return db.getISumm();
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
	}
}
