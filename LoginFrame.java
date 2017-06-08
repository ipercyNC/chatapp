/*
 * LoginFrame Class for Client Side
 * Ian Percy
 * 6/7/2017
 * 
 * Start the client from this program. Handles login/register
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class LoginFrame extends JFrame {
    private final Client client;
    JTextField loginBox = new JTextField();
    JPasswordField passwordBox = new JPasswordField();
    JButton loginButton = new JButton("Login");
    JButton registerButton = new JButton("Register");
    JButton logoutButton = new JButton("Logout");
    JLabel userLabel = new JLabel("Username:");
    JLabel passLabel = new JLabel("Password:");
    public LoginFrame() {
        super("Login");

        this.client = new Client("localhost", 8818);
        client.connect();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(userLabel);
        p.add(loginBox);
        p.add(passLabel);
        p.add(passwordBox);
        p.add(loginButton);
        p.add(registerButton);
        p.add(logoutButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRegister();
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	client.disconnect();
                dispose();
            }
        });
        getContentPane().add(p, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void doLogin() {
    	String login = null;
    	String password=null;
    	if(loginBox.getText().isEmpty() || loginBox.getText().isEmpty()){
    		login=password=null;
    	}else{
    	login = loginBox.getText();
         password = passwordBox.getText();
    	}
        try {
        	if(login!=null && password!=null){
            if (client.login(login, password)) {
                // bring up the user list window
                PrimaryFrame primaryFrame = new PrimaryFrame(client);
                JFrame frame = new JFrame("User: "+ login);
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                frame.setSize(1200, 1800);

                frame.getContentPane().add(primaryFrame, BorderLayout.CENTER);
                frame.setVisible(true);

                setVisible(false);
            } else {
                // show error message
                JOptionPane.showMessageDialog(this, "Invalid login/password.");
            }}else{
            	JOptionPane.showMessageDialog(this,  "Please enter a valid username and password");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doRegister() {
    	String login = null;
    	String password=null;
    	if(loginBox.getText().isEmpty() || loginBox.getText().isEmpty()){
    		login=password=null;
    	}else{
    	login = loginBox.getText();
         password = passwordBox.getText();
    	}
        try {
        	if(login!=null && password!=null){
        	boolean result = client.register(login, password);
            if (result) {
            	
                // bring up the user list window
            	JOptionPane.showMessageDialog(this, "Sucess. Now please login");
                 registerButton.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to register user. Try again.");
            } } else{
            	JOptionPane.showMessageDialog(this, "Please enter a valid username and password");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginFrame loginWin = new LoginFrame();
        loginWin.setVisible(true);
    }
}
