package vendingMachineSystem;

import vendingMachineSystem.controller.*;

public class VendingMachine {
	
	String userType = "";
	String userName = "";
	VendingMachineState vm;
	
	public VendingMachine() {
		this.setState(new DefaultState(this));
	}
	
	public void setState(VendingMachineState vm) {
		this.vm = vm;
		vm.run();
	}
	
	public VendingMachineState getState() {
		return vm;
	}
	
	public void setUser(String userName, String userType) {
		this.userName = userName;
		this.userType = userType;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getUserType() {
		return this.userType;
	}
	
	
}