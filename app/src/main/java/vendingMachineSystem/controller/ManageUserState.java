package vendingMachineSystem.controller;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.CardModel;
import vendingMachineSystem.model.ManageUserModel;
import vendingMachineSystem.model.TransactionModel;
import vendingMachineSystem.model.UserModel;
import vendingMachineSystem.view.DefaultView;
import vendingMachineSystem.view.ManageUserView;

import java.sql.SQLException;
import java.util.List;

public class ManageUserState extends VendingMachineState {

	private VendingMachineState prevState;

	int timeoutPeriodSeconds = 120;
	boolean showPassword;
	public ManageUserState(VendingMachine vm, VendingMachineState prevState, boolean showPassword) {
		super(vm);
		this.prevState = prevState;
		this.showPassword = showPassword;
	}

	@Override
	public void run(){
		ManageUserView view = new ManageUserView(this, showPassword);
		view.display();
	}

	public String[][] getAllUsers(String except, boolean showPassword) throws SQLException {
		ManageUserModel db = new ManageUserModel();
		return db.getAllUsers(except,showPassword);
	}

	public String[] getAllUsernames(String except) throws SQLException {
		ManageUserModel db = new ManageUserModel();
		return db.getAllUsernames(except);
	}

	public void removeUser(String username) throws SQLException{
		ManageUserModel db = new ManageUserModel();
		db.removeUser(username);
	}

	public String getUser(){
		return vm.getUserName();
	}
	public void returnToLoggedInState(){
		vm.setState(prevState);
	}

	public void refresh(){

		vm.setState( new ManageUserState(vm,prevState,showPassword) );
	}

	public void passwordRefresh(boolean checked){
		vm.setState( new ManageUserState(vm,prevState,checked) );
	}

	public boolean checkTransactionTimeout() {
		boolean timedout = super.checkTimedOut(timeoutPeriodSeconds);
		if (timedout) {
			TransactionModel tm = new TransactionModel(vm.getUserName(), "Timed out");
			tm.addFailedTransaction();
		}
		return timedout;
	}

	public boolean createAccount(String username, String password, String type){
		ManageUserModel db = new ManageUserModel();
		try {
			db.createUser(username,password,type);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public String[] getUserInfo(String username) throws SQLException{
		ManageUserModel db = new ManageUserModel();
		return db.getUserInfo(username);
	}

	public boolean updateUser(String oldUsername, String username, String password, String type){

		try {
			ManageUserModel db = new ManageUserModel();
			db.updateUser(oldUsername,username,password,type);
			return true;
		} catch (SQLException e) {
			return false;
		}

	}

	public void setTimeout(int sec) {
		this.timeoutPeriodSeconds = sec;
	}
}

