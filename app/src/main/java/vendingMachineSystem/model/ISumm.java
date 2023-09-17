package vendingMachineSystem.model;

public class ISumm {
    int id;
    int quantity;
    String name;

    public ISumm( int id, String name,int quantity){
        this.id = id;
        this.quantity = quantity;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}
