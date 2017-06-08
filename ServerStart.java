/*
 * ServerStart Class for the server side
 * Ian Percy
 * 6/7/2017
 * 
 * Main class for server object. Creates Server. Server must be started from this class
 * 
 */
 public class ServerStart {
        public static void main(String[] args) {
            int port = 8818;
            Server server = new Server(port);
            server.start();
        }
    }
