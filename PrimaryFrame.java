
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class PrimaryFrame extends JPanel implements UserStatusListener, MessageListener {


    private final Client client;
    private JList<String> userListUI;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> messageList = new JList<>(listModel);
    private DefaultListModel<String> userListModel;
    private JTextField inputField = new JTextField();
    JButton logoutButton = new JButton("Logout");
    JButton historyButton = new JButton("Chat History");

    public PrimaryFrame(Client client) {
        this.client = client;
        this.client.addUserStatusListener(this);
        client.addMessageListener(this);
        userListModel = new DefaultListModel<>();
        userListUI = new JList<>(userListModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(userListUI), BorderLayout.EAST);
        add(new JScrollPane(messageList),BorderLayout.WEST);
        add(inputField,BorderLayout.SOUTH);
        add(logoutButton,BorderLayout.NORTH);
        add(historyButton,BorderLayout.CENTER);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogout();
                setVisible(false);
                
            }
        });
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //  try {
                	String name =null;
                	//boolean result = client.getHistory(name);
					if(true){
						HistoryFrame historyFrame = new HistoryFrame(client,name);
						JFrame f = new JFrame("History");
	                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	                    f.setSize(500, 500);
	                    f.getContentPane().add(historyFrame, BorderLayout.CENTER);
	                    f.setVisible(true);
					}//else{
						 
					//}
							
				//} //catch (IOException e1) {
					//e1.printStackTrace();
				//}
                
            }
        });
        inputField.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		try{
        			String text = inputField.getText();
        			client.broadcast(text);
        			listModel.addElement("Me: " + text);
        			inputField.setText("");
        		}catch(IOException e1){
        			e1.printStackTrace();
        		}
        	}
        });
        userListUI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    String login = userListUI.getSelectedValue();
                    MessageFrame messageFrame = new MessageFrame(client, login);

                    JFrame f = new JFrame("Message: " + login);
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    f.setSize(500, 500);
                    f.getContentPane().add(messageFrame, BorderLayout.CENTER);
                    f.setVisible(true);
                }
            }
        });
       
    }
    private void doLogout() {
        try {
          client.logoff();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
    public void online(String login) {
    	String line = "ONLINE: " + login + "\n";
    	try {
			client.broadcastEvent(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
        userListModel.addElement(login);
    }

    @Override
    public void offline(String login) {
        userListModel.removeElement(login);
    }

	@Override
	 public void onMessage(String fromLogin, String msgBody) {
  
            String line = fromLogin + ": " + msgBody;
            listModel.addElement(line);
         
       
    }
}
