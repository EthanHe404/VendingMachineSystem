package vendingMachineSystem.model;

import java.sql.SQLException;
import java.util.List;

public class UserModel {

    Database db;

    public UserModel(){
        db = Database.getInstance();
        db.connect("database.db");

        // hard-coded to add user
//        try {
//            createUser("billyowner", "owner123","OWNER");
//            createUser("billyseller", "seller123","SELLER");
//            createUser("billycashier", "cashier123","CASHIER");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public void createUser(String username, String password, String type) throws SQLException {
        db.addUser(username,password,type);
    }

    public String getPassword(String username) throws SQLException {
        String pw = db.getPassword(username);
        return pw;
    }

    public String getUserType(String username) throws SQLException {
        String type = db.getUserType(username);
        return type;
    }

    public List<User> getUserReport() throws SQLException {
        List<User> ret = db.getUserReport();
        return ret;
    }

}
