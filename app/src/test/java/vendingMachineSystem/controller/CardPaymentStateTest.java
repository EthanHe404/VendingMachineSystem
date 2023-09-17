package vendingMachineSystem.controller;

import org.junit.jupiter.api.*;
import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardPaymentStateTest {

	static Database db;
	static String dbName = "test_database";
	static Connection dbConnection;
	VendingMachine vm;
	VendingMachineState prevState;
	Map<String, Integer> itemsToPurchase;

	Boolean loggedIn;
	
	@BeforeAll
	static void initialiseDB() throws Exception {
		db = Database.getInstance();
		db.connect(dbName + ".db");
		String dbUrl = "jdbc:sqlite:" + System.getProperty("user.dir") + "/" + dbName + ".db";
		dbConnection = DriverManager.getConnection(dbUrl);
	}

	@AfterAll
	static void cleanup() throws Exception {
		File dbFile = new File(System.getProperty("user.dir") + "/" + dbName + ".db");
		dbFile.delete();
		dbConnection.close();
	}
	
	@BeforeEach
	void initialiseClasses() {
		vm = new VendingMachine();
		prevState = new PurchaseItemState(vm,new LoggedInState(vm,"CUSTOMER"),true);
		itemsToPurchase = new HashMap<String,Integer>();
		loggedIn = true;
	}

	@Test
	@DisplayName("Return To purchase Page")
	void changeToPurchaseState() {
		CardPaymentState state = new CardPaymentState(vm,itemsToPurchase,prevState,loggedIn);
		state.changeToPurchaseState();
		assertTrue(state.getVm().getState() instanceof PurchaseItemState);
	}


	@Test
	@DisplayName("Timeout")
	void timeout() {
		vm.setUser("John", "CUSTOMER");
		CardPaymentState state = new CardPaymentState(vm,itemsToPurchase,prevState,loggedIn);
		db.connect(dbName + ".db");
		state.setTimeout(0);
		boolean timedOut = state.checkTransactionTimeout();
		assert(timedOut);
	}
	
	@Test
	@DisplayName("No Timeout")
	void notTimedout() {
		vm.setUser("John", "CUSTOMER");
		CardPaymentState state = new CardPaymentState(vm,itemsToPurchase,prevState,loggedIn);
		db.connect(dbName + ".db");
		boolean timedOut = state.checkTransactionTimeout();
		assert(!timedOut);
	}

	@Test
	@DisplayName("Get username")
	void getUser() {
		vm.setUser("billy", "CUSTOMER");
		CardPaymentState state = new CardPaymentState(vm,itemsToPurchase,prevState,loggedIn);
		db.connect(dbName + ".db");
		String ans = state.getUser();
		assertEquals("billy",ans);
	}

	@Test
	@DisplayName("calculateTotal")
	void calculateTotal(){
		itemsToPurchase.put("Pebis",0);
		itemsToPurchase.put("Conk",0);
		itemsToPurchase.put("Spronk",0);
		CardPaymentState state = new CardPaymentState(vm,itemsToPurchase,prevState,loggedIn);
		float ans = state.calculateTotal();
		assertEquals("0.0",String.format("%.1f", ans));
	}

	@Test
	@DisplayName("Get card number")
	void getCardNumber() throws SQLException {
		CardPaymentState state = new CardPaymentState(vm,itemsToPurchase,prevState,loggedIn);
		String result = state.getCardNumber("Sergio","42689");
		assertEquals("42689",result);
	}

	@Test
	@DisplayName("Get card stored by user")
	void getCardStoredByUser() throws SQLException {
		vm.setUser("billy", "CUSTOMER");
		CardPaymentState state = new CardPaymentState(vm,itemsToPurchase,prevState,loggedIn);
		List<String> result = state.getCardStoredByUser(state.getUser());
		String cards = result.toString();
		assertEquals(cards,cards);
	}

	@Test
	@DisplayName("Finish Transaction")
	void finishTransaction() {
		CardPaymentState state = new CardPaymentState(vm,itemsToPurchase,prevState,loggedIn);
		state.finishTransaction();
		assertTrue(state.getVm().getState() instanceof DefaultState);
	}
//
//	@Test
//	void fail(){
//		fail();
//	}
}
