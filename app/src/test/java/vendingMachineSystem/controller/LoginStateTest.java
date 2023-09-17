package vendingMachineSystem.controller;

import org.junit.jupiter.api.*;
import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.Database;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginStateTest {

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
		LoginState state = new LoginState(vm);
		state.changeToLoggedInPage("John","CUSTOMER");
		assertTrue(state.getVm().getState() instanceof LoggedInState);
	}
	
	@Test
	@DisplayName("Change To Registration Page")
	void changeToRegistrationPage() {
		LoginState state = new LoginState(vm);
		state.changeToRegistrationPage();
		assertTrue(state.getVm().getState() instanceof RegistrationState);
	}

	
	@Test
	@DisplayName("Timeout")
	void timeout() {
		vm.setUser("John", "CUSTOMER");
		LoginState state = new LoginState(vm);
		db.connect(dbName + ".db");
		state.setTimeout(0);
		boolean timedOut = state.checkTransactionTimeout();
		assert(timedOut);
	}
	
	@Test
	@DisplayName("No Timeout")
	void notTimedout() {
		vm.setUser("John", "CUSTOMER");
		LoginState state = new LoginState(vm);
		db.connect(dbName + ".db");
		boolean timedOut = state.checkTransactionTimeout();
		assert(!timedOut);
	}

	@Test
	@DisplayName("Get Password")
	void getPassword () throws SQLException {
		LoginState state = new LoginState(vm);
		db.connect(dbName + ".db");
		String password = state.getPassword("billy");
		assertEquals("123456",password);
	}

	@Test
	@DisplayName("Get User Type")
	void getUserType () throws SQLException {
		LoginState state = new LoginState(vm);
		db.connect(dbName + ".db");
		String type = state.getUserType("billy");
		assertEquals("CUSTOMER",type);
	}

}
