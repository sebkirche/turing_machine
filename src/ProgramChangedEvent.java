import java.util.EventObject;

/**
 * PACKAGE_NAME
 * <p/>
 * User: sebastien - 2012
 */
public class ProgramChangedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    private String pgm;

    public ProgramChangedEvent(Object source, String program) {
        super(source);
        pgm = program;
    }

    public String getPgm(){
        return pgm;
    }
}
