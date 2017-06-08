/*
 * Server Class for the server side
 * Ian Percy
 * 6/7/2017
 * 
 * Main server object, created from ServerStart. Will handle creating the server threads and continually looping
 * 
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

public class Server extends Thread {
    private final int serverPort;
    private ArrayList<ServerThread> workerList = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ServerThread> getWorkerList() {
        return workerList;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while(true) {
                System.out.println("Waiting for connection->");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted from " + clientSocket);
                ServerThread worker = new ServerThread(this, clientSocket);
                workerList.add(worker);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWorker(ServerThread serverThread) {
        workerList.remove(serverThread);
    }
   
}
