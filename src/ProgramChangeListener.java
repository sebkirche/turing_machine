import java.util.EventListener;

/**
 * PACKAGE_NAME
 * <p/>
 * User: sebastien - 2012
 */
public interface ProgramChangeListener extends EventListener {
    public void programChanged(ProgramChangedEvent evt);
}
