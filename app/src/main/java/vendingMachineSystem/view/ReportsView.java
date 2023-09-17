package vendingMachineSystem.view;

import vendingMachineSystem.controller.LoggedInState;
import vendingMachineSystem.controller.ReportsState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportsView extends AbstractView{

    ReportsState state;
    String label_string;

    public ReportsView(ReportsState state) {
        this.state = state;
        label_string = state.getRole() + " Reports";
    }

    @Override
    public void display() {
        Window window = Window.getInstance();

        int report_stagger = 80;
        Dimension reportDim;
        int buttonWidth = 100;

        Panel p = new Panel();
        p.setLayout(null);

        // label
        JLabel label = new JLabel(label_string);
        Dimension size = label.getPreferredSize();
        label.setBounds(170,30,size.width,size.height);
        p.add(label);

        // report buttons

        // change, transaction summary
        if (state.getRole().equals("OWNER") || state.getRole().equals("CASHIER")) {
            JButton reportButtonCSV = new JButton("csv"); // button
            reportButtonCSV.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.changeReport(true);
                    new ReportDownloaded();
                }
            });
            JLabel reportLabel = new JLabel("Change"); // label
            reportDim = reportLabel.getPreferredSize(); // txt
            JButton reportButtonTXT = new JButton("text");
            reportButtonTXT.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.changeReport(false);
                    new ReportDownloaded();
                }
            });

            reportLabel.setBounds(80, report_stagger, reportDim.width, reportDim.height);
            reportButtonCSV.setBounds(reportDim.width + 110, report_stagger, buttonWidth, reportDim.height);
            reportButtonTXT.setBounds(reportDim.width + 120 + buttonWidth, report_stagger, buttonWidth, reportDim.height);
            p.add(reportLabel);
            p.add(reportButtonCSV);
            p.add(reportButtonTXT);
            report_stagger += reportDim.height;

            // summary
            JButton summaryButtonCSV = new JButton("csv"); // button
            summaryButtonCSV.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.summaryReport(true);
                    new ReportDownloaded();
                }
            });
            JLabel summaryLabel = new JLabel("Transaction summary"); // label
            reportDim = summaryLabel.getPreferredSize(); // txt
            JButton summaryButtonTXT = new JButton("text");
            summaryButtonTXT.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.summaryReport(false);
                    new ReportDownloaded();
                }
            });

            summaryLabel.setBounds(80, report_stagger + 10, reportDim.width, reportDim.height);
            summaryButtonCSV.setBounds(reportDim.width + 110, report_stagger + 10, buttonWidth, reportDim.height);
            summaryButtonTXT.setBounds(reportDim.width + 120 + buttonWidth, report_stagger + 10, buttonWidth, reportDim.height);
            p.add(summaryLabel);
            p.add(summaryButtonCSV);
            p.add(summaryButtonTXT);
            report_stagger += reportDim.height;
        }

        // item details, item summary
        if ( state.getRole().equals("OWNER") || state.getRole().equals("SELLER")){

            //details
            JButton detailsButtonCSV = new JButton("csv"); // button
            detailsButtonCSV.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.detailsReport(true);
                    new ReportDownloaded();
                }
            });
            JLabel detailsLabel = new JLabel("Item Details"); // label
            reportDim = detailsLabel.getPreferredSize(); // txt
            JButton detailsButtonTXT = new JButton("text");
            detailsButtonTXT.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.detailsReport(false);
                    new ReportDownloaded();
                }
            });

            detailsLabel.setBounds(80, report_stagger + 10, reportDim.width, reportDim.height);
            detailsButtonCSV.setBounds(reportDim.width + 110, report_stagger + 10, buttonWidth, reportDim.height);
            detailsButtonTXT.setBounds(reportDim.width + 120 + buttonWidth, report_stagger + 10, buttonWidth, reportDim.height);
            p.add(detailsLabel);
            p.add(detailsButtonCSV);
            p.add(detailsButtonTXT);
            report_stagger += reportDim.height;

            // Item summary
            JButton IsummaryButtonCSV = new JButton("csv"); // button
            IsummaryButtonCSV.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.IsummaryReport(true);
                    new ReportDownloaded();
                }
            });
            JLabel IsummaryLabel = new JLabel("Item summary"); // label
            reportDim = IsummaryLabel.getPreferredSize(); // txt
            JButton IsummaryButtonTXT = new JButton("text");
            IsummaryButtonTXT.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.IsummaryReport(false);
                    new ReportDownloaded();
                }
            });

            IsummaryLabel.setBounds(80, report_stagger + 10, reportDim.width, reportDim.height);
            IsummaryButtonCSV.setBounds(reportDim.width + 110, report_stagger + 10, buttonWidth, reportDim.height);
            IsummaryButtonTXT.setBounds(reportDim.width + 120 + buttonWidth, report_stagger + 10, buttonWidth, reportDim.height);
            p.add(IsummaryLabel);
            p.add(IsummaryButtonCSV);
            p.add(IsummaryButtonTXT);
            report_stagger += reportDim.height;
        }
        // users, failed transactions
        if ( state.getRole().equals("OWNER")){

            //users
            JButton usersButtonCSV = new JButton("csv"); // button
            usersButtonCSV.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.userReport(true);
                    new ReportDownloaded();
                }
            });
            JLabel usersLabel = new JLabel("Users"); // label
            reportDim = usersLabel.getPreferredSize(); // txt
            JButton usersButtonTXT = new JButton("text");
            usersButtonTXT.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.userReport(false);
                    new ReportDownloaded();
                }

            });

            usersLabel.setBounds(80, report_stagger+20, reportDim.width, reportDim.height);
            usersButtonCSV.setBounds(reportDim.width + 110, report_stagger +20, buttonWidth, reportDim.height);
            usersButtonTXT.setBounds(reportDim.width + 120 + buttonWidth, report_stagger+20, buttonWidth, reportDim.height);
            p.add(usersLabel);
            p.add(usersButtonCSV);
            p.add(usersButtonTXT);
            report_stagger += reportDim.height;

            // failed
            JButton failedButtonCSV = new JButton("csv"); // button
            failedButtonCSV.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.failedReport(true);
                    new ReportDownloaded();
                }
            });
            JLabel failedLabel = new JLabel("Failed Transactions"); // label
            reportDim = failedLabel.getPreferredSize(); // txt
            JButton failedButtonTXT = new JButton("text");
            failedButtonTXT.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReportsView.this.state.failedReport(false);
                    new ReportDownloaded();
                }

            });

            failedLabel.setBounds(80, report_stagger+20, reportDim.width, reportDim.height);
            failedButtonCSV.setBounds(reportDim.width + 110, report_stagger +20, buttonWidth, reportDim.height);
            failedButtonTXT.setBounds(reportDim.width + 120 + buttonWidth, report_stagger+20, buttonWidth, reportDim.height);
            p.add(failedLabel);
            p.add(failedButtonCSV);
            p.add(failedButtonTXT);
            report_stagger += reportDim.height;
        }

        // cancel
        JButton cancelButton = new JButton("Return");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportsView.this.state.clickedCancel();
            }
        });
        cancelButton.setBounds(550,220,100,40);
        //cancelButton.setFont(new Font("Arial", Font.PLAIN, 10));
        p.add(cancelButton);

        // finish
        window.updateWindow(p);
    }
}
