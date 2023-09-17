package vendingMachineSystem.model;

public class Change {
    private String name; //$50, 10c
    private float value; //50.0, 0.1
    private int qty; //5, 5

    public Change(String name, float value, int qty){
        this.name = name;
        this.value = value;
        this.qty = qty;
    }

    public Change(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return (double) value;
    }

    public int getQty() {
        return qty;
    }


}
