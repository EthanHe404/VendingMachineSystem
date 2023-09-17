package vendingMachineSystem.controller;

import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.model.DataModel;
import vendingMachineSystem.model.Product;
import vendingMachineSystem.view.RestockView;

import java.sql.SQLException;
import java.util.List;

public class RestockState extends VendingMachineState{
    private String role;

    public RestockState(VendingMachine vm, String role) {
        super(vm);
        this.role = role;
    }

    @Override
    public void run() {
        RestockView view = new RestockView(this);
        view.display();
    }

    public void changeToLoggedInState(){
        vm.setState(new LoggedInState(vm, this.role));
    }

    public void updateItemByID(String id, String name, String category, String price, String qty){
        DataModel dm = new DataModel(false);
        try{
            dm.updateItemByID(id, name, category, price, qty);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public void updateItemID( String name, String id){
        DataModel dm = new DataModel(false);
        try{
            dm.updateItemID(name,id);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public String[][] getItemNameList(){
        // get products
        DataModel dm = new DataModel(false);
        List<Product> ls;
        try {
            ls = dm.allProducts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // now get 2d list of items needed
        // category, item, quantity, price
        String[][] ret = new String[ls.size()][2];
        for ( int prod_n = 0; prod_n < ls.size(); prod_n++ ){
            ret[prod_n][0] = ls.get(prod_n).getName();
            ret[prod_n][1] = Integer.toString(ls.get(prod_n).getId());
        }

        return ret;
    }
}
