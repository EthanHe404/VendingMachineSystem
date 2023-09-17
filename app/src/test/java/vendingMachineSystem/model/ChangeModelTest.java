package vendingMachineSystem.model;

import org.junit.jupiter.api.*;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ChangeModelTest {
    static Database db;
    static String dbName = "test_database";

    @BeforeAll
    static void initialiseDB() throws Exception {
        db = Database.getInstance();
        db.connect(dbName + ".db");

        try {
            db.dropTable("Changes");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void cleanup() throws Exception {
        File dbFile = new File(dbName);
        dbFile.delete();
        db.connection.close();
    }

    @Test
    public void testGetAllCash(){
        String pop = """
                INSERT INTO Changes(name, value, quantity)
                    VALUES
                        ("$100", 100, 70),
                        ("$1", 1, 70)
                ;
                """;

        Database db = Database.getInstance();
        db.connect("test_database.db");
        try{
            db.doStatement(pop);
        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }

        ChangeModel cm = new ChangeModel(true);
        List<Change> changes;
        List<Change> expect = new ArrayList<Change>();

        try{
            changes = cm.allChanges();
            //check size
            assertEquals(changes.size(), 2);
            //check all data
            expect.add(new Change("$100", 100F, 70));
            expect.add(new Change("$1", 1F, 70));

            for (int i = 0; i < changes.size(); i++) {
                assertEquals(expect.get(i).getName(), changes.get(i).getName());
                assertEquals(expect.get(i).getValue(), changes.get(i).getValue());
                assertEquals(expect.get(i).getQty(), changes.get(i).getQty());
            }
        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }

    }

    @Test
    public void testUpdateCash(){
        ChangeModel cm = new ChangeModel(true);

        try{
            cm.updateCash("$100", "60");
        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }

        try{
            List<Change> changes = cm.allChanges();
            List<Change> expect = new ArrayList<Change>();
            expect.add(new Change("$100", 100, 60));
            expect.add(new Change("$1", 1, 70));

            for (int i = 0; i < changes.size(); i++) {
                assertEquals(expect.get(i).getName(), changes.get(i).getName());
                assertEquals(expect.get(i).getValue(), changes.get(i).getValue());
                assertEquals(expect.get(i).getQty(), changes.get(i).getQty());
            }
        } catch (SQLException e){
            e.printStackTrace();
            fail();
        }



    }



}
