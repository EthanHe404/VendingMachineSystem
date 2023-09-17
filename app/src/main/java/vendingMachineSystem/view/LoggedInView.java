package vendingMachineSystem.view;

import vendingMachineSystem.controller.LoggedInState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;

import vendingMachineSystem.controller.*;


public class LoggedInView extends AbstractView {

    LoggedInState state;

    public LoggedInView(LoggedInState state) {
        this.state = state;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void display() {
        Window window = Window.getInstance();

        Panel p = new Panel();
        p.setLayout(null);
        //p.setLayout(new BorderLayout());

        //menu
        JLabel menuLabel = new JLabel("Menu");
        Dimension size = menuLabel.getPreferredSize();
        menuLabel.setBounds(0,0,size.width,size.height);
        p.add(menuLabel);
        String[][] data = state.getItemData(false);
        String[] names = {"Category", "Item", "Quantity", "Price"};
        JTable tab = new JTable(data, names);
        JScrollPane tab_scroller = new JScrollPane(tab);
        tab_scroller.setBounds(0,20,650,125);
        p.add(tab_scroller);

        //recent purchases
        String[][] rec_data;
        rec_data = state.getRecentData(state.getVm().getUserName());
        if (rec_data != null) {
            JLabel recLabel = new JLabel("Recent Purchases");
            Dimension rec_size = recLabel.getPreferredSize();
            recLabel.setBounds(0, 150, rec_size.width, rec_size.height);
            p.add(recLabel);
            String[] rec_names = {"", "Item"};
            JTable rec_tab = new JTable(rec_data, rec_names);
            JScrollPane rec_tab_scroller = new JScrollPane(rec_tab);
            rec_tab_scroller.setBounds(0, 170, 650, 60);
            p.add(rec_tab_scroller);
        }
        JButton purchaseButton = new JButton("Purchase");
        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggedInView.this.state.clickedPurchase();
            }
        });
        purchaseButton.setBounds(0,230,100,40); //300x300 window, 100
        //purchaseButton.setFont(new Font("Arial", Font.PLAIN, 10));
        p.add(purchaseButton);

        // cancel
        JButton cancelButton = new JButton("Log Out");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoggedInView.this.state.clickedCancel();
            }
        });
        cancelButton.setBounds(560,230,100,40);
        //cancelButton.setFont(new Font("Arial", Font.PLAIN, 10));
        p.add(cancelButton);

        // buttons based on role
            // modify/restock
        if ( state.getRole().equals("OWNER") || state.getRole().equals("SELLER") ){
            JButton modButton = new JButton("Modify/Restock");
            modButton.setBounds(100,230,120,40);
            //modButton.setFont(new Font("Arial", Font.PLAIN, 10));
            modButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoggedInView.this.state.clickedModifyRestock();
                }
            });
            p.add(modButton);
        }

            // update change
        if ( state.getRole().equals("OWNER") || state.getRole().equals("CASHIER") ){
            JButton updateButton = new JButton("Update Change");
            updateButton.setBounds(220,230,120,40); // 340
            //updateButton.setFont(new Font("Arial", Font.PLAIN, 10));
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoggedInView.this.state.clickedUpdateChange();
                }
            });
            p.add(updateButton);
        }

            // update user
        if ( state.getRole().equals("OWNER") ){
            JButton mgUsrButton = new JButton("Manage Users");
            mgUsrButton.setBounds(340,230,120,40); // 460
            //mgUsrButton.setFont(new Font("Arial", Font.PLAIN, 10));
            mgUsrButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoggedInView.this.state.clickedManageUsers();
                }
            });
            p.add(mgUsrButton);
        }

            // reports
        if ( state.getRole().equals("OWNER") || state.getRole().equals("SELLER") || state.getRole().equals("CASHIER") ){
            JButton reportButton = new JButton("Reports");
            reportButton.setBounds(460,230,100,40); // 560
            //reportButton.setFont(new Font("Arial", Font.PLAIN, 10));
            reportButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoggedInView.this.state.clickedReports();
                }
            });
            p.add(reportButton);
        }

        //the products scroll table

        if ( state.getRole().equals("SELLER") ) {
            /* Have commented this out, as I've already written the code for this and am not sure if it's needed - Nick
            //TODO: connect the table with database, this is a sample table
            String[][] data = { {"Mineral Water", "1001", "Drinks", "7", "2.5"},
                    {"Sprite", "1002", "Drinks", "7", "3"}};
            String[] columns = {"Item", "Code", "Category", "Quantity", "Price"};

            JTable productTable = new JTable(data, columns);
            JScrollPane scrollPane = new JScrollPane(productTable);
            scrollPane.setBounds(30, 10, 600, 200);
            p.add(scrollPane);
             */
        }



        window.updateWindow(p);
    }


}
