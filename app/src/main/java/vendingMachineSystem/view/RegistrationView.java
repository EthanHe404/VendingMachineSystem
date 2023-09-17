package vendingMachineSystem.view;

        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;

        import javax.swing.*;

        import vendingMachineSystem.controller.*;

public class RegistrationView extends AbstractView {

    RegistrationState state;
    Dimension size;
    JTextField username;
    JPasswordField password;


    public RegistrationView(RegistrationState state) {
        this.state = state;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void display() {
        // TODO Auto-generated method stub
        Window window = Window.getInstance();

        Panel p = new Panel();
        p.setLayout(null);
        JLabel pageLabel = new JLabel("Registration");
        size = pageLabel.getPreferredSize();
        pageLabel.setBounds(170, 30, size.width, size.height);
        p.add(pageLabel);

        JLabel usernameLabel = new JLabel("Username:");
        size = usernameLabel.getPreferredSize();
        usernameLabel.setBounds(70, 70, size.width, size.height);
        p.add(usernameLabel);

        username = new JTextField(18);
        username.setBounds(140, 65, 150, 26);
        p.add(username);

        JLabel passwordLabel = new JLabel("Password:");
        size = passwordLabel.getPreferredSize();
        passwordLabel.setBounds(70, 100, size.width, size.height);
        p.add(passwordLabel);

        password = new JPasswordField(18);
        password.setEchoChar('*');
        password.setBounds(140, 95, 150, 26);
        p.add(password);


        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean timedout = RegistrationView.this.state.checkTransactionTimeout();
                if (timedout) {
                    return;
                }
                String usernameEntered = username.getText().toLowerCase();
                String passwordEntered = new String(password.getPassword());
                Boolean registrationStatus = RegistrationView.this.state.createAccount(usernameEntered,passwordEntered,"CUSTOMER");
                if (registrationStatus){
                    RegistrationView.this.state.changeToLoggedInPage(usernameEntered);
                }else{
                    new FailRegistration();
                }

            }

        });

        size = registerButton.getPreferredSize();
        registerButton.setBounds(150, 120, size.width, size.height);
        p.add(registerButton);

        JButton CancelButton = new JButton("Cancel");
        CancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationView.this.state.cancelTransaction();
            }

        });

        size = CancelButton.getPreferredSize();
        CancelButton.setBounds(300, 200, size.width, size.height);
        p.add(CancelButton);

        window.updateWindow(p);
    }

}
