

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Client {
    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream servIn;
    private OutputStream servOut;
    private BufferedReader bufferedIn;
    private String user=null;

    private ArrayList<UserStatusListener> userList = new ArrayList<>();
    private ArrayList<MessageListener> messageList = new ArrayList<>();

    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void msg(String sendTo, String msgBody) throws IOException {
        String cmd = "msg " + sendTo + " " + msgBody + "\n";
        servOut.write(cmd.getBytes());
    }
    public void broadcast(String msg) throws IOException{
    	String cmd = "msg all " + msg+"\n";
    	servOut.write(cmd.getBytes());
    }
    public void broadcastEvent(String line) throws IOException {
		String cmd = "msg log " + line + "\n";
		servOut.write(cmd.getBytes());
		
	}
    public boolean login(String login, String password) throws IOException {
        String cmd = "login " + login + " " + password + "\n";
        servOut.write(cmd.getBytes());

        String response = bufferedIn.readLine();
        System.out.println("Response Line:" + response);

        if ("ok login".equalsIgnoreCase(response)) {
        	user = login;
            startMessageReader();
            return true;
        } else {
            return false;
        }
    }

	public boolean register(String login, String password) throws IOException {
	      String cmd = "register " + login + " " + password + "\n";
	       servOut.write(cmd.getBytes());

	        String response = bufferedIn.readLine();
	        System.out.println("Response Line:" + response);

	       if ("ok register".equalsIgnoreCase(response)) {
	            return true;
	        } else {
	            return false;
	        }
	}
    public void logoff() throws IOException {
        String cmd = "logoff\n";
        servOut.write(cmd.getBytes());
    }

    private void startMessageReader() {
        Thread t = new Thread() {
            @Override
            public void run() {
                readMessage();
            }
        };
        t.start();
    }

    private void readMessage() {
        try {
            String line;
            while ((line = bufferedIn.readLine()) != null) {
                String[] tokens = StringUtils.split(line);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];
                    if ("online".equalsIgnoreCase(cmd)) {
                        displayOnlineUsers(tokens);
                        deliverLogin(tokens);
                    } else if ("offline".equalsIgnoreCase(cmd)) {
                        displayOfflineUsers(tokens);
                        deliverLogoff(tokens);
                    } else if ("msg".equalsIgnoreCase(cmd)) {
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        deliverMessage(tokensMsg);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deliverMessage(String[] tokensMsg) {
        String login = tokensMsg[1];
        String msgBody = tokensMsg[2];
       if(login.equalsIgnoreCase("all")){
    	   for(MessageListener listener : messageList) {
               listener.onMessage(login, msgBody);
           }
       }
       else{
       for(MessageListener listener : messageList) {
            listener.privMessage(login, msgBody);
        }
       }
    }
    private void deliverLogin(String[] tokensMsg) {
        String login = "ONLINE";
        String msgBody = tokensMsg[1];
        
        for(MessageListener listener : messageList) {
            listener.onMessage(login, msgBody);
        }
    }
    private void deliverLogoff(String[] tokensMsg) {
        String login = "OFFLINE";
        String msgBody = tokensMsg[1];
        
        for(MessageListener listener : messageList) {
            listener.onMessage(login, msgBody);
        }
    }
    private void displayOfflineUsers(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userList) {
            listener.offline(login);
        }
    }

    private void displayOnlineUsers(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userList) {
            listener.online(login);
        }
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverName, serverPort);
            System.out.println("Client port is " + socket.getLocalPort());
            this.servOut = socket.getOutputStream();
            this.servIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(servIn));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void disconnect() {
            try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

    public void addUserStatusListener(UserStatusListener listener) {
        userList.add(listener);
    }

    public void removeUserStatusListener(UserStatusListener listener) {
        userList.remove(listener);
    }

    public void addMessageListener(MessageListener listener) {
        messageList.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageList.remove(listener);
    }

	public boolean getHistory(String getUser) throws IOException {
		
	    String cmd = "history\n";
        servOut.write(cmd.getBytes());

        String response = bufferedIn.readLine();

        if ("ok history".equalsIgnoreCase(response)) {
        	getUser = user;
            return true;
        } else {
            return false;
        }
	}
	public String getUser(){return user;}

	

}
