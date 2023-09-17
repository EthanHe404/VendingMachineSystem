package vendingMachineSystem.controller;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.view.DefaultView;
import vendingMachineSystem.view.LoggedInView;
import vendingMachineSystem.view.LoginView;

import java.util.LinkedList;


public class LoggedInState extends VendingMachineState {

    private String role;

    public String getRole(){return role;}
    public LoggedInState(VendingMachine vm, String role){
        super(vm);
        this.role = role;
    }

    @Override
    public void run(){
        LoggedInView view = new LoggedInView(this);
        view.display();
    }

    public void clickedPurchase(){
        vm.setState(new PurchaseItemState(vm, this,true));
    }

    public void clickedModifyRestock(){
        // (STUB) TODO: purchase page
        System.out.println("Clicked Modify/Restock");

        vm.setState(new RestockState(vm, role));
    }
    public void clickedUpdateChange(){
        // (STUB) TODO: update change page
        System.out.println("Clicked Update Change");

        vm.setState(new FillCashState(vm, role));
    }
    public void clickedManageUsers(){
        // (STUB) TODO: manage users page
        System.out.println("Clicked Manage Users");
        vm.setState(new ManageUserState(vm,this,false));
    }
    public void clickedReports(){
        // (STUB) TODO: reports page
        vm.setState( new ReportsState(vm, role) );
    }

    public void clickedCancel(){
        role = null; // update this when roles are updated?
        vm.setState( new DefaultState(vm) );
    }

}
