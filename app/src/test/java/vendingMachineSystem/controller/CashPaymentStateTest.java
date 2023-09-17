package vendingMachineSystem.controller;

import org.junit.jupiter.api.*;
import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CashPaymentStateTest {
    static Database db;
    static String dbName = "test_database";
    static Connection dbConnection;
    VendingMachine vm;
    VendingMachineState prevState;
    Map<String, Integer> itemsToPurchase;

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
    }

    @Test
    @DisplayName("Return To purchase Page")
    void changeToPurchaseState() {
        CashPaymentState state = new CashPaymentState(vm,itemsToPurchase,prevState);
        state.changeToPurchaseState();
        assertTrue(state.getVm().getState() instanceof PurchaseItemState);
    }

    @Test
    @DisplayName("Timeout")
    void timeout() {
        vm.setUser("John", "CUSTOMER");
        CashPaymentState state = new CashPaymentState(vm,itemsToPurchase,prevState);
        db.connect(dbName + ".db");
        state.setTimeout(0);
        boolean timedOut = state.checkTransactionTimeout();
        assert(timedOut);
    }

    @Test
    @DisplayName("No Timeout")
    void notTimedout() {
        vm.setUser("John", "CUSTOMER");
        CashPaymentState state = new CashPaymentState(vm,itemsToPurchase,prevState);
        db.connect(dbName + ".db");
        boolean timedOut = state.checkTransactionTimeout();
        assert(!timedOut);
    }

    @Test
    @DisplayName("calculateTotal")
    void calculateTotal(){
        itemsToPurchase.put("Pebis",0);
        itemsToPurchase.put("Conk",0);
        itemsToPurchase.put("Spronk",0);
        CashPaymentState state = new CashPaymentState(vm,itemsToPurchase,prevState);
        float ans = state.calculateTotal();
        assertEquals("0.0",String.format("%.1f", ans));
    }

    @Test
    @DisplayName("Finish Transaction")
    void finishTransaction() {
        CashPaymentState state = new CashPaymentState(vm,itemsToPurchase,prevState);
        state.finishTransaction(0);
        assertTrue(state.getVm().getState() instanceof DefaultState);
    }

    @Test
    void testUpdateCash(){
        CashPaymentState state = new CashPaymentState(vm,itemsToPurchase,prevState);
        state.updateCash("$5", "100");
        String[][] cash =  state.getCashData();
        for (int i = 0; i < cash.length; i++){
            if (cash[i][0].equals("$5")){
                assertEquals("100", cash[i][1]);
                break;
            }
        }
    }

    @Test
    void testGetChangeData(){
        CashPaymentState state = new CashPaymentState(vm,itemsToPurchase,prevState);
        String[][] cash =  state.getCashData();
        String[][] change = state.getChangeData();
        assertEquals(cash.length, change.length);
        for (int i = 0; i < cash.length; i++){
            assertEquals(cash[i][0], change[i][0]);
            assertEquals(cash[i][1], change[i][1]);
        }
    }

}
