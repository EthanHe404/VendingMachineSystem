package vendingMachineSystem.controller;


import vendingMachineSystem.VendingMachine;
import vendingMachineSystem.view.LoggedInView;
import vendingMachineSystem.view.ReportsView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ReportsState extends VendingMachineState {

        private String role;

        public String getRole(){return role;}
        public ReportsState(VendingMachine vm, String role){
            super(vm);
            this.role = role;
        }

    @Override
    public void run(){
        ReportsView view = new ReportsView(this);
        view.display();
    }

    void output_csv(String filename, String plaintext){
        File file = new File(filename);
        try
        {
            file.createNewFile();
            BufferedWriter file_w = new BufferedWriter(new FileWriter(filename, true));
            file_w.append(plaintext);
            file_w.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void clickedCancel(){
            if (super.checkTimedOut()){vm.setState(new DefaultState(vm));}
            else{
                vm.setState(new LoggedInState(vm, role));
            }
    }


    String getOutString(String[][] data, String outString){
            if (data == null) return outString;
        for ( int i =0; i < data.length; i++){
            for ( int j = 0; j < data[i].length; j++){
                outString = outString + data[i][j];
                if ( j == data[i].length-1 ){ // last
                    outString = outString + "\n";
                } else{
                    outString = outString + ",";
                }
            }

        }
        return outString;
    }
    String getFileName( boolean is_csv, String repType ){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String filename = new SimpleDateFormat("yyyyMMdd HH:mm:ss.S").format(timestamp) + "_" + repType;
        if (is_csv){
            filename = filename +".csv";
        } else {
            filename = filename + ".txt";
        }
        return filename;
    }

    public void failedReport( boolean is_csv ){
        String outString = "timestamp,user,reason\n";
        String[][] failedData = super.getFailedData();
        outString = getOutString( failedData, outString );
        String filename = getFileName( is_csv, "failed_transactions");
        output_csv(filename, outString);
    }

    public void changeReport(boolean is_csv) {
        String outString = "name,value,quantity\n";
        String[][] cashData = super.getCashData();
        outString = getOutString( cashData, outString );
        String filename = getFileName( is_csv, "change");
        output_csv(filename, outString);
    }

    public void summaryReport(boolean is_csv) {
        String outString = "when,item,paid,change,method\n";
        String[][] summData = super.getSummData();
        outString = getOutString( summData, outString );
        String filename = getFileName( is_csv, "transaction_summary");
        output_csv(filename, outString);
    }

    public void IsummaryReport(boolean is_csv) {
        String outString = "code,name,quantity\n";
        String[][] IsummData = super.getISummData();
        outString = getOutString( IsummData, outString );
        String filename = getFileName( is_csv, "item_summary");
        output_csv(filename, outString);
    }

    public void detailsReport(boolean is_csv) {
        String outString = "category,name,quantity,price,id\n";
        String[][] cashData = super.getItemData(true);
        outString = getOutString( cashData, outString );
        String filename = getFileName( is_csv, "itemDetails");
        output_csv(filename, outString);
    }

    public void userReport(boolean is_csv) {
        String outString = "username,role\n";
        String[][] userData = super.getUserReport();
        outString = getOutString( userData, outString );
        String filename = getFileName( is_csv, "users");
        output_csv(filename, outString);
    }


}
