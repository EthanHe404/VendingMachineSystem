package vendingMachineSystem.controller;

import org.junit.jupiter.api.*;
import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestockStateTest {
    static Database db;
    static String dbName = "test_database";
    static Connection dbConnection;
    VendingMachine vm;

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
    }

    @Test
    @DisplayName("Change To Logged In Page")
    void changeToLoggedInPage() {
        RestockState state = new RestockState(vm, "SELLER");
        state.changeToLoggedInState();
        assertTrue(state.getVm().getState() instanceof LoggedInState);
    }

    @Test
    void testChangeItemByID(){
        RestockState state = new RestockState(vm, "SELLER");
        state.updateItemByID("1", "Pebis test", "Drink", "1.1", "5");
        String[][] items = state.getItemData(true);
        for (int i = 0; i < items.length; i++){
            if (items[i][4].equals("1")){
                assertEquals("Pebis test", items[i][1]);
                break;
            }
        }
    }

    @Test
    void testGetItemName(){
        RestockState state = new RestockState(vm, "SELLER");
        state.updateItemByID("1", "Pebis test", "Drink", "1.1", "5");
        String[][] items = state.getItemNameList();
        for (int i = 0; i < items.length; i++){
            if (items[i][1].equals("1")){
                assertEquals("Pebis test", items[i][0]);
                break;
            }
        }
    }

    @Test
    void testUpdateItemID(){
        RestockState state = new RestockState(vm, "SELLER");
        state.updateItemByID("1", "Pebis test", "Drink", "1.1", "5");
        state.updateItemID("Pebis test", "100");
        String[][] items = state.getItemNameList();
        for (int i = 0; i < items.length; i++){
            if (items[i][0].equals("Pebis test")){
                assertEquals("100", items[i][1]);
                break;
            }
        }

    }

    @Test
    void testRun(){
        RestockState state = new RestockState(vm, "SELLER");
        state.run();
    }

}
