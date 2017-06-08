/*
 * UserStatusListener Interface
 * Ian Percy
 * 6/7/2017
 * 
 * Helps create the user list listeners (updates for PrimaryFrame in client GUI)
 * 
 */
public interface UserStatusListener {
    public void online(String login);
    public void offline(String login);
}
