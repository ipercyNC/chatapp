/*
 * MessageFrame Class for Client Side
 * Ian Percy
 * 6/7/2017
 * 
 * Handles the private message frame between two clients
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MessageFrame extends JPanel implements MessageListener {

    private final Client client;
    private final String login;

    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> messageList = new JList<>(listModel);
    private JTextField inputField = new JTextField();

    public MessageFrame(Client client, String login) {
        this.client = client;
        this.login = login;

        client.addMessageListener(this);

        setLayout(new BorderLayout());
        add(new JScrollPane(messageList), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
        updateHistory("Private Chat: " + login);

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = inputField.getText();
                    client.msg(login, text);
                    String line = "You: "+text;
                    updateHistory(line);
                    listModel.addElement("You: " + text);
                    inputField.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Override
    public void privMessage(String fromLogin, String msgBody) {
        if (login.equalsIgnoreCase(fromLogin)) {
            String line = fromLogin + ": " + msgBody;
            listModel.addElement(line);
            updateHistory(line);
        }
    }

	@Override
	public void onMessage(String fromLogin, String msgBody) {	
	}
	public void updateHistory(String line){
		try {
			
			String path = client.getUser()+".txt";
			Date today = Calendar.getInstance().getTime(); 
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
			bufferedWriter.write(line);
			bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
