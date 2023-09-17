package vendingMachineSystem;

import vendingMachineSystem.model.Database;

public class SetUpDatabase {

    public static void main(String[] args) {
        Database db = Database.getInstance();
        db.connect("database.db");
    }
}