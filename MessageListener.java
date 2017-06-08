/*
 * MessageListener Interface
 * Ian Percy
 * 6/7/2017
 * 
 * Used for adding the listeners for each client (assists with sending the messages) 
 */
public interface MessageListener {
    public void onMessage(String fromLogin, String msgBody);
    public void privMessage(String fromLogin, String msgBody);
}
