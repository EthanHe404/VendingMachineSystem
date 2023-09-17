package vendingMachineSystem.controller;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.ManageUserModel;
import vendingMachineSystem.model.TransactionModel;
import vendingMachineSystem.model.UserModel;
import vendingMachineSystem.view.DefaultView;
import vendingMachineSystem.view.LoginView;
import vendingMachineSystem.view.RegistrationView;

import java.lang.reflect.Type;
import java.sql.SQLException;

public class RegistrationState extends VendingMachineState {

    int timeoutPeriodSeconds = 120;
    public RegistrationState(VendingMachine vm) {
        super(vm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        RegistrationView view = new RegistrationView(this);
        view.display();
    }

    public void changeToLoggedInPage(String username) {
        vm.setState(new LoggedInState(vm,"CUSTOMER"));
        vm.setUser(username, "CUSTOMER");
    }

    public boolean createAccount(String username, String password, String type){
        UserModel userDB = new UserModel();
        try {
            userDB.createUser(username,password,type);
            return true;
        } catch (SQLException e) {
            return false;
        }
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

    //for unit testing only
    public void removeUser(String username) throws SQLException{
        ManageUserModel db = new ManageUserModel();
        db.removeUser(username);
    }

}
