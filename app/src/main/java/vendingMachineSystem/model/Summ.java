package vendingMachineSystem.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Summ {
    //when,item,paid,change,method

    private String timestamp;
    private String item;
    private String paid;
    private String change;
    private String method;

    public Summ(Timestamp timestamp, String item, double paid, double change, String reason){
        this.timestamp = (new SimpleDateFormat("yyyyMMdd HH:mm:ss.S")).format(timestamp);
        this.item = item;
        this.paid = String.format("%.3f", paid);
        this.change =String.format("%.3f", change);
        this.method = reason;
    }

    public String getWhen() {
        return timestamp;
    }
    public String getItem(){
        return item;
    }
    public String getPaid(){return paid;}
    public String getChange(){ return change;}
    public String getMethod(){return method;}
}
