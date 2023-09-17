package vendingMachineSystem.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class FailedTrans {
    private String timestamp;
    private String username;
    private String reason;
    public FailedTrans(Timestamp timestamp, String username, String reason){
        this.reason = reason;
        this.timestamp = (new SimpleDateFormat("yyyyMMdd HH:mm:ss.S")).format(timestamp);
        this.username = username;
    }

    public String getWhen() {
        return timestamp;
    }

    public String getWhy() {
        return reason;
    }

    public String getName() {
        return username;
    }
}
