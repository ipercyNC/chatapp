
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

    public LoginFrame() {
        super("Login");

        this.client = new Client("localhost", 8818);
        client.connect();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(loginBox);
        p.add(passwordBox);
        p.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        getContentPane().add(p, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    private void doLogin() {
        String login = loginBox.getText();
        String password = passwordBox.getText();

        try {
            if (client.login(login, password)) {
                // bring up the user list window
                PrimaryFrame primaryFrame = new PrimaryFrame(client);
                JFrame frame = new JFrame("User List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 1800);

                frame.getContentPane().add(primaryFrame, BorderLayout.CENTER);
                frame.setVisible(true);

                setVisible(false);
            } else {
                // show error message
                JOptionPane.showMessageDialog(this, "Invalid login/password.");
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
