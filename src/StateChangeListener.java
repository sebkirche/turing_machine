import java.util.EventListener;

/**
 * StateChangeListener a notify channel for machine state changes
 *
 * Seki - 2012
 */
public interface StateChangeListener extends EventListener {
    public void stateChanged(StateChangedEvent changeEvt);
}
