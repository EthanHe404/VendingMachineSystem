package vendingMachineSystem.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;

import vendingMachineSystem.VendingMachine;

class VendingMachineStateTest {
	
	private class ConcreteState extends VendingMachineState {

		public ConcreteState(VendingMachine vm) {
			super(vm);
		}

		@Override
		public void run() {
			
		}
		
	}

	@Test
	@DisplayName("Cancelling a transaction")
	void cancelTransaction() {
		VendingMachine vm = new VendingMachine();
		ConcreteState state = new ConcreteState(vm);
		state.cancelTransaction();
		assertTrue(vm.getState() instanceof DefaultState);
	}
	
	@Test
	@DisplayName("Transaction not timed out")
	void transactionNotTimedout() {
		VendingMachine vm = new VendingMachine();
		ConcreteState state = new ConcreteState(vm);
		vm.setState(state);
		try {
			TimeUnit.SECONDS.sleep(1);
			boolean timedout = state.checkTimedOut(2);
			assertTrue(!timedout);
			assertTrue(vm.getState() instanceof ConcreteState);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Transaction Timeout")
	void transactionTimeout() {
		VendingMachine vm = new VendingMachine();
		ConcreteState state = new ConcreteState(vm);
		vm.setState(state);
		try {
			TimeUnit.SECONDS.sleep(5);
			boolean timedout = state.checkTimedOut(1);
			assertTrue(timedout);
			assertTrue(vm.getState() instanceof DefaultState);
		} catch (InterruptedException e) {
			assertTrue(false);
		}
	}

}
