/*
 * PrimaryFrame Class for Client Side
 * Ian Percy
 * 6/7/2017
 * 
 * Main frame after login with client. Displays the chat, user list, ability to view chat history and logout.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PrimaryFrame extends JPanel implements UserStatusListener, MessageListener {


    private final Client client;
    private JList<String> userListUI;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> messageList = new JList<>(listModel);
    private DefaultListModel<String> userListModel;
    private JTextField inputField = new JTextField();
    JButton logoutButton = new JButton("Logout");
    JButton historyButton = new JButton("Chat History");
    DateFormat df;
    public PrimaryFrame(Client client) {
    

        this.client = client;
        this.client.addUserStatusListener(this);
        client.addMessageListener(this);
        userListModel = new DefaultListModel<>();
        userListUI = new JList<>(userListModel);
        setLayout(new BorderLayout());
        JPanel subPanel = new JPanel();
        add(new JScrollPane(userListUI), BorderLayout.EAST);
        add(new JScrollPane(messageList),BorderLayout.CENTER);
        add(inputField,BorderLayout.SOUTH);
        subPanel.add(logoutButton);
        subPanel.add(historyButton);
        add(subPanel,BorderLayout.WEST);
       
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogout();
                setVisible(false);
                JComponent comp =(JComponent) e.getSource();
                Window win = SwingUtilities.getWindowAncestor(comp);
                win.dispose();
                
            }
        });
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //  try {
                	String name =null;
                	//boolean result = client.getHistory(name);
					if(true){
						HistoryFrame historyFrame = new HistoryFrame(client,client.getUser());
						JFrame f = new JFrame("History");
	                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	                    f.setSize(500, 500);
	                    f.getContentPane().add(historyFrame, BorderLayout.CENTER);
	                    f.setVisible(true);
					}
                
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
        	updateHistory();
        	String line = "ONLINE " + client.getUser() + "\n";
        	try {
    			client.broadcastEvent(line);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
          client.logoff();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
    public void online(String login) {
    	String line = "ONLINE " + login + "\n";
    	try {
			client.broadcastEvent(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
        userListModel.addElement(login);
    }

    @Override
    public void offline(String login) {
    	String line = "OFFLINE " + login + "\n";
    	try {
			client.broadcastEvent(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
        userListModel.removeElement(login);
    }

	@Override
	 public void onMessage(String fromLogin, String msgBody) {
  
            String line = fromLogin + ": " + msgBody;
            listModel.addElement(line);
         
       
    }
	public void updateHistory(){
		df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			
			String path = client.getUser()+".txt";
			Date today = Calendar.getInstance().getTime(); 
			String reportDate = df.format(today);
			String name =client.getUser();
        	File fp = new File(path);
        	if(!fp.exists() && !fp.isDirectory()){
        		try{
        			fp.createNewFile();
        			
        		}catch(FileAlreadyExistsException ex){
        		} catch (IOException e1) {
        		}
        	}
			FileWriter fileWriter = new FileWriter(path,true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(reportDate);
			bufferedWriter.newLine();
			bufferedWriter.write("group chat");
			bufferedWriter.newLine();
			for(int i=0;i<listModel.size();i++){
				bufferedWriter.write(listModel.get(i).toString());
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void privMessage(String fromLogin, String msgBody) {
		// TODO Auto-generated method stub
		
	}

}
