package vendingMachineSystem.model;

public class Product {
    private int id;
    private String name;
    private String category;
    private int quantity;
    private float price;

    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void setPrice(float price){
        this.price = price;
    }

    public int getId(){
        return id;
    }
    public int getQuantity(){
        return quantity;
    }
    public float getPrice() {
        return price;
    }
    public String getName(){
        return name;
    }
    public String getCategory() {
        return category;
    }

    public Product( int id, String name,String category, int quantity, float price ){
        this.category = category;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.id= id;
    }
    public Product(){

    }

    public void displayProduct(){
        System.out.println(String.format("id: %d, name: %s, cat: %s, q: %d, price: %f", id, name, category, quantity, price) );
    }
}
