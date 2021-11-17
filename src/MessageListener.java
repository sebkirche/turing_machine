import java.util.EventListener;

/**
 * MessageListener a notify channel for messages from the machine
 *
 * @author Seki - 2012
 */
public interface MessageListener extends EventListener {
    public void addMessage(String msg);
}
