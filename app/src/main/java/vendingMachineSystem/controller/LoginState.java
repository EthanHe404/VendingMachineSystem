package vendingMachineSystem.controller;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.TransactionModel;
import vendingMachineSystem.model.UserModel;
import vendingMachineSystem.view.DefaultView;
import vendingMachineSystem.view.LoginView;

import java.sql.SQLException;
import java.util.List;

public class LoginState extends VendingMachineState {

	int timeoutPeriodSeconds = 120;
	public LoginState(VendingMachine vm) {
		super(vm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		LoginView view = new LoginView(this);
		view.display();
	}


	public void changeToRegistrationPage() {
		vm.setState(new RegistrationState(vm));
	}


	public void changeToLoggedInPage(String username, String type){
		this.checkTimedOut(120);
		vm.setUser(username, type);
		vm.setState( new LoggedInState(vm, type));
	}

	public String getPassword(String username) throws SQLException {
		UserModel userDB = new UserModel();
		String pw = userDB.getPassword(username);
		return pw;
	}

	public String getUserType(String username) throws SQLException {
		UserModel userDB = new UserModel();
		String type = userDB.getUserType(username);
		return type;
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
