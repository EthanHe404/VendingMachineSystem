package vendingMachineSystem.controller;

import org.junit.jupiter.api.Test;
import vendingMachineSystem.VendingMachine;

public class LoggedInStateTest {

    @Test
    void clickPurchase(){
        VendingMachine vm = new VendingMachine();
        LoggedInState s = new LoggedInState(vm, "OWNER");
        s.clickedPurchase();
        assert(vm.getState() instanceof PurchaseItemState);
    }

    @Test
    void clickMod(){
        VendingMachine vm = new VendingMachine();
        LoggedInState s = new LoggedInState(vm, "OWNER");
        s.clickedModifyRestock();
        assert(vm.getState() instanceof RestockState);
    }

    @Test
    void clickChange(){
        VendingMachine vm = new VendingMachine();
        LoggedInState s = new LoggedInState(vm, "OWNER");
        s.clickedUpdateChange();
        assert(vm.getState() instanceof FillCashState);
    }

    @Test
    void clickUsr(){
        VendingMachine vm = new VendingMachine();
        LoggedInState s = new LoggedInState(vm, "OWNER");
        s.clickedManageUsers();
        assert(vm.getState() instanceof ManageUserState);
    }

    @Test
    void clickReports(){
        VendingMachine vm = new VendingMachine();
        LoggedInState s = new LoggedInState(vm, "OWNER");
        s.clickedReports();
        assert(vm.getState() instanceof ReportsState);
    }

    @Test
    void clickCancel(){
        VendingMachine vm = new VendingMachine();
        LoggedInState s = new LoggedInState(vm, "OWNER");
        s.clickedCancel();
        assert(vm.getState() instanceof DefaultState);
    }
}
