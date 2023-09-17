package vendingMachineSystem.controller;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.ChangeModel;
import vendingMachineSystem.view.FillCashView;

import java.sql.SQLException;

public class FillCashState extends VendingMachineState{
    private String role;

    public FillCashState(VendingMachine vm, String role){
        super(vm);
        this.role = role;
    }
    @Override
    public void run() {
        FillCashView view = new FillCashView(this);
        view.display();
    }

    public void changeToLoggedInState(){
        vm.setState(new LoggedInState(vm, this.role));
    }






}
