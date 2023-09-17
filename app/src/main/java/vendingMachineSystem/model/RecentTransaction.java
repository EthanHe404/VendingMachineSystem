package vendingMachineSystem.model;

public class RecentTransaction {
    private String itemName;

    public RecentTransaction(String itemName){
        this.itemName = itemName;
    }

    //methods
    public String getItemName(){return itemName;}

}
