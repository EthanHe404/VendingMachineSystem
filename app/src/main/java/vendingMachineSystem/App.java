package vendingMachineSystem;

import vendingMachineSystem.model.Database;

public class App {

    public static void main(String[] args) {
    	new VendingMachine();
    	Database db = Database.getInstance();
    	db.connect("database.db");
    }
}
