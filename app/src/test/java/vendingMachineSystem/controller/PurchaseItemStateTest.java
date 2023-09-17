package vendingMachineSystem.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.Database;

class PurchaseItemStateTest {

	static Database db;
	static String dbName = "test_database";
	static Connection dbConnection;
	VendingMachine vm;
	VendingMachineState prevState;
	
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
		prevState = new DefaultState(vm);
	}
	
	@AfterEach
    void afterEach() {
		try {
			Statement statement = dbConnection.createStatement();
			statement.execute("DELETE FROM transactions;");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

	@Test
	@DisplayName("Change To Cash Payment Page")
	void changeToCashPayment() {
		PurchaseItemState state = new PurchaseItemState(vm, prevState,false);
		state.changeToCashPaymentPage(new HashMap<String, Integer>());
		assertTrue(state.getVm().getState() instanceof CashPaymentState);
	}
	
	@Test
	@DisplayName("Change To Card Payment Page")
	void changeToCardPayment() {
		PurchaseItemState state = new PurchaseItemState(vm, prevState,false);
		state.changeToCardPaymentPage(new HashMap<String, Integer>());
		assertTrue(state.getVm().getState() instanceof CardPaymentState);
	}
	
	@Test
	@DisplayName("Cancel Transaction")
	void cancelTransaction() {
		vm.setUser("John", "CUSTOMER");
		PurchaseItemState state = new PurchaseItemState(vm, prevState,false);
		db.connect(dbName + ".db");
		state.cancelTransaction();
		assertEquals(state.getVm().getState(), prevState);
		
		String sql = "SELECT * FROM transactions;";
		Statement statement;
		try {
			statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			String reason = rs.getString("Cancelled_reason");
			String name = rs.getString("User");
			assertEquals("Cancelled", reason);
			assertEquals("John", name);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Timeout")
	void timeout() {
		vm.setUser("John", "CUSTOMER");
		PurchaseItemState state = new PurchaseItemState(vm, prevState,false);
		db.connect(dbName + ".db");
		state.setTimeout(0);
		boolean timedOut = state.checkTransactionTimeout();
		assert(timedOut);
	}
	
	@Test
	@DisplayName("No Timeout")
	void notTimedout() {
		vm.setUser("John", "CUSTOMER");
		PurchaseItemState state = new PurchaseItemState(vm, prevState,false);
		db.connect(dbName + ".db");
		boolean timedOut = state.checkTransactionTimeout();
		assert(!timedOut);
	}

}
