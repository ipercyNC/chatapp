
/*
 * ServerThread Class for the server side
 * Ian Percy
 * 6/7/2017
 * 
 * Server thread object class. Each client will communicate with one of these server threads. 
 * Delivers messages and handles input/output buffered stream. 
 * 
 */
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ServerThread extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;
    private HashSet<String> topicSet = new HashSet<>();
    private UserStore us = new UserStore();
    private History hist = new History();

    public ServerThread(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ( (line = reader.readLine()) != null) {
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else if("register".equalsIgnoreCase(cmd)){
                	handleRegister(outputStream,tokens);
                }else if ("msg".equalsIgnoreCase(cmd)) {
                
                    String[] tokensMsg = StringUtils.split(line, null, 3);
                    handleMessage(tokensMsg);
                }else if("history".equalsIgnoreCase(cmd)){
                	handleHistory(outputStream);
                }else if("all".equalsIgnoreCase(cmd)){
                	String[] tokensMsg = StringUtils.split(line,null,3);
                	handleMessage(tokensMsg);
                }else{
                	
                    String msg = "unknown " + cmd + "\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }

        clientSocket.close();
    }



    private void handleHistory(OutputStream outputStream2) throws IOException {
    	String fileIn = null;
    
    	if(hist.exists(fileIn, login)){
    		  String msg = "ok history\n";
              outputStream.write(msg.getBytes());
    	}	
    	else{
    		String msg = "error history\n";
    		outputStream.write(msg.getBytes());
    	}
	}

	private void handleMessage(String[] tokens) throws IOException {
		String cat = tokens[0];
        String sendTo = tokens[1];
        String body = tokens[2];


        List<ServerThread> workerList = server.getWorkerList();
       
        if(cat.equalsIgnoreCase("all")){
            for(ServerThread worker : workerList) {
            	if(!login.equals(worker.getLogin())){
            	String outMsg = "all " + login + " " + body + "\n";
            	worker.send(outMsg);
            	}
            }
    	}else if(sendTo.equalsIgnoreCase("log")){
    		  for(ServerThread worker : workerList) {
              	if(!login.equals(worker.getLogin())){
              	String outMsg = "msg " + body + "\n";
              	worker.send(outMsg);
              	}
              }
    	}else{
    		for(ServerThread worker : workerList) {
                if (sendTo.equalsIgnoreCase(worker.getLogin())) {
                    String outMsg = "msg " + login + " " + body + "\n";
                    worker.send(outMsg);
                }
            }
    	}
    }

    private void handleLogoff() throws IOException {
        server.removeWorker(this);
        List<ServerThread> workerList = server.getWorkerList();

        // send other online users current user's status
        String onlineMsg = "offline " + login + "\n";
        for(ServerThread worker : workerList) {
            if (!login.equals(worker.getLogin())) {
                worker.send(onlineMsg);
            }
        }
        clientSocket.close();
    }

    public String getLogin() {
        return login;
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];

            if (us.verfifyUser(login, password))  {
                String msg = "ok login\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println("User logged in succesfully: " + login);

                List<ServerThread> workerList = server.getWorkerList();

                // send current user all other online logins
                for(ServerThread worker : workerList) {
                    if (worker.getLogin() != null) {
                        if (!login.equals(worker.getLogin())) {
                            String msg2 = "online " + worker.getLogin() + "\n";
                            send(msg2);
                        }
                    }
                }

                // send other online users current user's status
                String onlineMsg = "online " + login + "\n";
                for(ServerThread worker : workerList) {
                    if (!login.equals(worker.getLogin())) {
                        worker.send(onlineMsg);
                    }
                }
            } else {
                String msg = "error login\n";
                outputStream.write(msg.getBytes());
                System.err.println("Login failed for " + login);
            }
        }
    }
    private void handleRegister(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];

            if (us.registerNewUser(login, password))  {
                String msg = "ok register\n";
                outputStream.write(msg.getBytes());
            } else {
                String msg = "error Register\n";
                outputStream.write(msg.getBytes());
                System.err.println("Register failed for " + login);
            }
        }
    }
    private void send(String msg) throws IOException {
        if (login != null) {
            try {
                outputStream.write(msg.getBytes());
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
