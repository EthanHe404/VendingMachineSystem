package vendingMachineSystem.model;

import java.sql.SQLException;
import java.util.List;

public class CardModel {

    Database db;

    public CardModel(){
        db = Database.getInstance();
        db.connect("database.db");
    }

    public String getCardNumber(String name, String cardNumber) throws SQLException {
        String number = db.getCardNumber(name,cardNumber);
        return number;
    }

    public void storeCardDetails(String username, String cardName, String cardNum) throws SQLException {
        db.storeCardDetails(username, cardName, cardNum);
    }

    public List<String> getCardStoredByUser(String username) throws SQLException{
        return db.getCardStoredByUser(username);
    }

}
