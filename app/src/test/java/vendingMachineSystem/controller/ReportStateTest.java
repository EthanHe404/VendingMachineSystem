package vendingMachineSystem.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class ReportStateTest {
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
    void cancelTest(){
        ReportsState rs = new ReportsState(vm,"OWNER");
        rs.clickedCancel();
        assertTrue(vm.getState() instanceof LoggedInState);
    }

    @Test
    void oneLineOutString(){
        ReportsState rs = new ReportsState(vm,"OWNER");
        String expected = "expected\na,b,c\n";
        String[][] inData = new String[1][3];
        inData[0][0] = "a";
        inData[0][1] = "b";
        inData[0][2] = "c";
        String actual = rs.getOutString( inData, "expected\n" );
        assertEquals(expected, actual);
    }
    @Test
    void noLineOutString(){
        ReportsState rs = new ReportsState(vm,"OWNER");
        String expected = "expected\n";
        String[][] inData = null;
        String actual = rs.getOutString( inData, "expected\n" );
        assertEquals(expected, actual);
    }
    @Test
    void moreLineOutString(){
        ReportsState rs = new ReportsState(vm,"OWNER");
        String expected = "expected\na,b,c\nd,e,f\n";
        String[][] inData = new String[2][3];
        inData[0][0] = "a";
        inData[0][1] = "b";
        inData[0][2] = "c";
        inData[1][0] = "d";
        inData[1][1] = "e";
        inData[1][2] = "f";
        String actual = rs.getOutString( inData, "expected\n" );
        assertEquals(expected, actual);
    }
    @Test
    void oneColOutString(){
        ReportsState rs = new ReportsState(vm,"OWNER");
        String expected = "expected\na\nb\n";
        String[][] inData = new String[2][1];
        inData[0][0] = "a";
        inData[1][0] = "b";
        String actual = rs.getOutString( inData, "expected\n" );
        assertEquals(expected, actual);
    }

    @Test
    void filenameEnds(){
        ReportsState rs = new ReportsState(vm,"OWNER");
        String actual;
        actual = rs.getFileName(true,"a");
        assertEquals( actual.substring(actual.length()-6), "_a.csv"); // is csv
        actual = rs.getFileName(false,"a");
        assertEquals( actual.substring(actual.length()-6), "_a.txt"); // is txt
    }
}
