package vendingMachineSystem.controller;

import org.junit.jupiter.api.*;
import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrationStateTest {

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


	@Test
	@DisplayName("Change To Logged In Page")
	void changeToLoggedInPage() {
		RegistrationState state = new RegistrationState(vm);
		state.changeToLoggedInPage("John");
		assertTrue(state.getVm().getState() instanceof LoggedInState);
	}

	
	@Test
	@DisplayName("Timeout")
	void timeout() {
		vm.setUser("John", "CUSTOMER");
		RegistrationState state = new RegistrationState(vm);
		db.connect(dbName + ".db");
		state.setTimeout(0);
		boolean timedOut = state.checkTransactionTimeout();
		assert(timedOut);
	}
	
	@Test
	@DisplayName("No Timeout")
	void notTimedout() {
		vm.setUser("John", "CUSTOMER");
		RegistrationState state = new RegistrationState(vm);
		db.connect(dbName + ".db");
		boolean timedOut = state.checkTransactionTimeout();
		assert(!timedOut);
	}

	@Test
	@DisplayName("Successfully create account")
	void createAccountSuccess () throws SQLException {
		RegistrationState state = new RegistrationState(vm);
		db.connect(dbName + ".db");
		boolean success = state.createAccount("john","123456","CUSTOMER");
		state.removeUser("john");
		assert(success);
	}

	@Test
	@DisplayName("Fail to create account")
	void createAccountFail () throws SQLException {
		RegistrationState state = new RegistrationState(vm);
		db.connect(dbName + ".db");
		state.createAccount("john","123456","CUSTOMER");
		boolean success = state.createAccount("john","123456","CUSTOMER");
		state.removeUser("john");
		assert(!success);
	}

}
