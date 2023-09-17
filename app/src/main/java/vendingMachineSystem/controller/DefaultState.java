package vendingMachineSystem.controller;

import vendingMachineSystem.*;
import vendingMachineSystem.model.DataModel;
import vendingMachineSystem.model.Product;
import vendingMachineSystem.view.DefaultView;

import java.sql.SQLException;
import java.util.List;

public class DefaultState extends VendingMachineState {

	public DefaultState(VendingMachine vm) {
		super(vm);
	}

	@Override
	public void run(){
		DefaultView view = new DefaultView(this);
		view.display();
	}
	
	public void changeToLoginPage() {
		vm.setState(new LoginState(vm));
	}
	
	public void changeToPurchaseItemsPage() {
		vm.setState(new PurchaseItemState(vm, this,false));
	}

}
