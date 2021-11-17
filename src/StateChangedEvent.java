import java.util.EventObject;

/**
 *
 */
public class StateChangedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */

	private int curTrans;
    private int state;
    private int pos;
    private HeadDirection dir;
    private Speed speed;

    public StateChangedEvent(Object source, int ip, int newState, int newPos, HeadDirection newDir, Speed curSpeed) {
        super(source);
        curTrans = ip;
        state = newState;
        pos = newPos;
        dir = newDir;
        speed = curSpeed;
    }

	public int getCurrentTransition(){
	   return curTrans;
	}

    public int getState(){
        return state;
    }

    public int getPos(){
        return pos;
    }

    public HeadDirection getDir(){
        return dir;
    }

    public Speed getSpeed(){
        return speed;
    }
}
