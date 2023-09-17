package vendingMachineSystem.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.sql.*;

import org.junit.jupiter.api.*;

class DatabaseTest {

	static Database db;
	static String dbName = "test_database";
	static Connection dbConnection;
	
	@BeforeAll
	static void initialiseDB() throws Exception {
		db = Database.getInstance();
		db.connect(dbName + ".db");
		String dbUrl = "jdbc:sqlite:" + System.getProperty("user.dir") + "/" + dbName + ".db";
		dbConnection = DriverManager.getConnection(dbUrl);
	}

	@AfterAll
	static void cleanup() throws Exception {
		File dbFile = new File(dbName);
		dbFile.delete();
		dbConnection.close();
	}

	@Test
	@DisplayName("Database Setup")
	void databaseSetup() {
		String sql;
		Statement statement;
		ResultSet rs;
		String[] tables = {
			"Products",
			"Changes",
			"Users",
			"Transactions",
			"TransactionProducts"
		};
		
        try {
        	for (String tableName: tables) {
        		sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='%s';";
        		sql = sql.format(sql, tableName);
        		statement = dbConnection.createStatement();
        		rs = statement.executeQuery(sql);
        		String name = rs.getString("name");
        		assertEquals(name, tableName);        
        		statement.close();
        	}
        } catch (SQLException e) {
        	e.printStackTrace();
        	fail();
        }
	}
	
	@Nested
	@DisplayName("Add Failed Transactions")
	class FailedTransactions {
		
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
		@DisplayName("Anonymous User")
		void addFailedTransactionAnonymous() {			
			try {
				db.addFailedTransaction(null, "Cancelled");
				String sql = "SELECT * FROM transactions;";
				Statement statement = dbConnection.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				String reason = rs.getString("Cancelled_reason");
				String name = rs.getString("User");
				assertEquals(reason, "Cancelled");
				assertEquals(name, null);
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				fail();
			}
		}
		
		@Test
		@DisplayName("Normal User")
		void addFailedTransactionNormal() {			
			try {
				db.addFailedTransaction("John", "Cancelled");
				String sql = "SELECT * FROM transactions;";
				Statement statement = dbConnection.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				String reason = rs.getString("Cancelled_reason");
				String name = rs.getString("User");
				assertEquals(reason, "Cancelled");
				assertEquals(name, "John");
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				fail();
			}
		}
	}

}
