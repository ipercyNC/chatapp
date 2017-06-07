
public interface MessageListener {
    public void onMessage(String fromLogin, String msgBody);
    public void privMessage(String fromLogin, String msgBody);
}
