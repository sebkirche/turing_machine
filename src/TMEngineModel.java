import javax.swing.event.EventListenerList;


class TMEngineModel implements Runnable {

    public static final int TAPESIZE = 30000;
    private boolean programmed;

    private Speed speed;
    TMAction transTable[];
    int tableSize;
    char tape[];
    private int state;
    private int tapePos;
    private int ip;
    int initPos;
    String initChars = "";
    int initNonBlanks;
    private int nonBlanks;
    private HeadDirection headDir;
    private long totalTransitions;
    private volatile Thread execution;
    private EventListenerList stateChangeListeners;
    private EventListenerList programChangeListeners;
    private EventListenerList messageListeners;
    private String currentProgram;

    public TMEngineModel() {
        stateChangeListeners = new EventListenerList();
        programChangeListeners = new EventListenerList();
        messageListeners = new EventListenerList();
        initMachine();
        execution = new Thread(this);
    }

    public void initMachine() {
        programmed = false;
        currentProgram = "";
        speed = Speed.slow;
        tapePos = 10;//000;
        initNonBlanks = 0;
        headDir = HeadDirection.stay;
        tape = new char[TAPESIZE];

        int initLen = initChars.length();
        for(int j = 0; j < TAPESIZE; j++)
            tape[j] = ' ';

        tapePos = initPos;

        if(initPos + initLen >= TAPESIZE)
            initLen = TAPESIZE - initPos - 1;

        for(int k = 0; k < initLen; k++) {
            char c = initChars.charAt(k);
            tape[initPos + k] = c;
        }

        nonBlanks = initNonBlanks;
        totalTransitions = 0L;
        //TODO : état initial non programmé ? setState(-3); //NA
        setState(HALTED);
    }

    public boolean initMachine(int iPos, String iChars, StringBuffer errResult) {
        initPos = iPos;
        initChars = iChars;
        int j = iChars.length();
        for(int k = 0; k < TAPESIZE; k++)
            tape[k] = ' ';

        tapePos = iPos;
        if(iPos + j >= TAPESIZE)
            j = TAPESIZE - iPos - 1;
        initNonBlanks = 0;
        for(int l = 0; l < j; l++) {
            char c = iChars.charAt(l);
            if(c == '_')
                c = ' ';
            if(!validTapeChar(c)) {
                errResult.append("Invalid tape character '").append(c).append("'");
                return false;
            }
            tape[iPos + l] = c;
            if(c != ' ')
                initNonBlanks++;
        }

        nonBlanks = initNonBlanks;
        totalTransitions = 0;
        setState(1);
        return true;
    }

    public boolean program(int initPos, String initChars, String program, StringBuffer errFeedback) {

        sendMessage("\nEntering program...");

        if(!initMachine(initPos, initChars, errFeedback)) {
            setState(NA);
            return false;
        }

        String as[] = MiscUtil.split(program, "\n");
        if(as.length < 1) {
            errFeedback.append("No programming entered");
            setState(NA);
            return false;
        }
        tableSize = as.length;
        transTable = new TMAction[tableSize];
        for(int l = 1; l <= tableSize; l++) {
            String as1[] = MiscUtil.split(as[l - 1], ", ");
            if(as1.length < 4 || as1.length > 5) {
                errFeedback.append("In line ").append(l).append(": bad format");
                setState(NA);
                return false;
            }
            if(as1[0].equals("H")) {
                errFeedback.append("In line ").append(l).append(": cannot transition from halt state");
                setState(NA);
                return false;
            }
            int state;
            try {
                state = Integer.parseInt(as1[0]);
            } catch(NumberFormatException _ex) {
                errFeedback.append("In line ").append(l).append(": invalid state '").append(as1[0]).append("'");
                setState(NA);
                return false;
            }
            if(state < 1 || state > 99) {
                errFeedback.append("In line ").append(l).append(": invalid state '").append(state).append("'");
                setState(NA);
                return false;
            }
            if(as1[1].length() > 1) {
                errFeedback.append("In line ").append(l).append(": bad format");
                setState(NA);
                return false;
            }
            char curChar = as1[1].charAt(0);
            if(curChar == '_')
                curChar = ' ';
            if(!validTapeChar(curChar)) {
                errFeedback.append("In line ").append(l).append(": Invalid tape character '").append(curChar).append("'");
                setState(NA);
                return false;
            }
            int newState;
            if(as1[2].equals("H")) {
                newState = -1;
            } else {
                try {
                    newState = Integer.parseInt(as1[2]);
                } catch(NumberFormatException _ex) {
                    errFeedback.append("In line ").append(l).append(": invalid state '").append(as1[2]).append("'");
                    setState(NA);
                    return false;
                }
                if(newState < 1 || newState > 99) {
                    errFeedback.append("In line ").append(l).append(": invalid state '").append(newState).append("'");
                    setState(NA);
                    return false;
                }
            }
            if(as1[3].length() > 1) {
                errFeedback.append("In line ").append(l).append(": bad format");
                setState(NA);
                return false;
            }
            if(as1.length == 5 && as1[4].length() > 1) {
                errFeedback.append("In line ").append(l).append(": bad format");
                setState(NA);
                return false;
            }
            char c = as1[3].charAt(0);
            char newChar;
            HeadDirection dir;
            if(c == '<' || c == '>') {
                dir = c != '<' ? HeadDirection.right : HeadDirection.left;
                if(as1.length == 4) {
                    newChar = '\0';
                } else {
                    newChar = as1[4].charAt(0);
                    if(newChar == '_')
                        newChar = ' ';
                    if(!validTapeChar(newChar)) {
                        errFeedback.append("In line ").append(l).append(": Invalid tape character '").append(newChar).append("'");
                        setState(NA);
                        return false;
                    }
                }
            } else {
                newChar = as1[3].charAt(0);
                if(newChar == '_')
                    newChar = ' ';
                if(!validTapeChar(newChar)) {
                    errFeedback.append("In line ").append(l).append(": Invalid tape character '").append(newChar).append("'");
                    setState(NA);
                    return false;
                }
                if(as1.length == 4) {
                    dir = HeadDirection.stay;
                } else {
                    char c1 = as1[4].charAt(0);
                    if(c1 != '<' && c1 != '>') {
                        errFeedback.append("In line ").append(l).append(": bad format");
                        setState(NA);
                        return false;
                    }
                    dir = c1 != '<' ? HeadDirection.right : HeadDirection.left;
                }
            }
            transTable[l - 1] = new TMAction(state, curChar, newState, newChar, dir);
        }
        sendMessage("Machine programmed successfully.");
        programmed = true;
        this.currentProgram = program;
        fireProgramChanged();
        setState(1);
        fireStateChanged();

        return true;
    }

    public void run() {
       Thread thisThread = Thread.currentThread();
        ExecResult r;
        if(speed != Speed.compute)
            //display.repaint();
            fireStateChanged();
        r = transition();
        while((execution == thisThread) && (r == ExecResult.SUCCESS)){
            if(speed == Speed.slow)
                try {
                    thisThread.sleep(1000);
                } catch(InterruptedException _ex) { _ex.printStackTrace();}
            else if(speed == Speed.fast)
                try {
                    thisThread.sleep(200);
                } catch(InterruptedException _ex) { _ex.printStackTrace();}
            else if(speed == Speed.vfast)
                try {
                    thisThread.sleep(50);
                } catch(InterruptedException _ex) { _ex.printStackTrace();}
            r = transition();
        }
        headDir = HeadDirection.stay;
        //display.repaint();
        showResults(r);
    }

    public boolean scrollTape(HeadDirection dir) {
        //TODO : refactorer ce truc
        boolean ret = true;
//        int oldPos = tapePos;
//        HeadDirection oldDir = headDir;
			System.out.println("Engine::scrollTape "+dir);

        if(dir == HeadDirection.left) {
            if(tapePos == 0){
                headDir = HeadDirection.stay;
                ret = false;
            } else {
                headDir = HeadDirection.left;
                tapePos--;
            }
        } else if(dir == HeadDirection.right) {
            if(tapePos == TAPESIZE - 1){
                headDir = HeadDirection.stay;
                ret = false;
            } else {
                headDir = HeadDirection.right;
                tapePos++;
            }
        } else {
            headDir = HeadDirection.stay;
        }
        //if (oldPos != tapePos )
        if (speed != Speed.compute)//todo: ne pas notifier en run mais si on est en scroll manu
            fireStateChanged();
        return ret;
    }

    public void setSpeed(Speed s) {
        speed = s;
    }

    public void setState(int i) {
        state = i;
        fireStateChanged();
    }

//    private void setPos(int newPos){
//        tapePos = newPos;
//        fireStateChanged();
//    }

    public ExecResult transition() {
        if(!programmed)
            return ExecResult.NOPROG;
        if(state == HALTED)
            return ExecResult.HALT;
        for(int i = 0; i < tableSize; i++) {
            TMAction fetched = transTable[i];
            if(fetched.currentState == state && fetched.currentChar == tape[tapePos]) {
                if(fetched.newChar != 0) {
                    if(fetched.newChar == ' ' && tape[tapePos] != ' ') {
                        nonBlanks--;
                    }else if(fetched.newChar != ' ' && tape[tapePos] == ' ') {
                        nonBlanks++;
                    }
                    tape[tapePos] = fetched.newChar;
                }

                boolean flag = scrollTape(fetched.newDirection);
                if(!flag) {
                    setState(HALTED);
                    return ExecResult.OFFTAPE;
                }
                if(speed == Speed.compute) {
                    state = fetched.newState;
                }else {
                    setState(fetched.newState);
                }
                ip = i;
                totalTransitions++;
                if(headDir != fetched.newDirection) {
                    headDir = fetched.newDirection;
                }
                if(speed != Speed.compute){
                    //display.repaint();
                    fireStateChanged();
                }
                return ExecResult.SUCCESS;
            }
        }

        setState(HALTED);
        return ExecResult.NOTRANS;
    }

    public boolean validTapeChar(char c) {
        return MiscUtil.isLetterOrDigit(c) || " +/*-!@#$%^&()=,.".indexOf(c) > -1;
    }

    public Speed getSpeed() {
        return speed;
    }

    public int getState(){
        return state;
    }

    public HeadDirection getDirection(){
        return headDir;
    }

    public boolean isProgrammed(){
        return programmed;
    }

    public boolean isRunning(){
       return execution != null ? execution.isAlive() : false;
    }

    public int getCurrentPos(){
        return tapePos;
    }

    public int getNonBlanks(){
        return nonBlanks;
    }

    public long getTotalTransitions(){
        return totalTransitions;
    }

    public char[] getMemory(int firstCell, int lastCell){
        char m[] = new char[lastCell - firstCell + 1];

        try{
        for(int c = 0; c <= lastCell - firstCell; c++)
            m[c] = tape[c + firstCell];
        } catch (NullPointerException e){
            System.out.printf("TMEngineModel::getMemory : NPE for %d..%d\n", firstCell, lastCell);
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.printf("TMEngineModel::getMemory : out of bounds for %d..%d\n", firstCell, lastCell);
        }

        return m;
    }

    public String getProgram(){
        if (isProgrammed())
            return currentProgram;
        else
            return "";
    }

    public void clearProgram(){
        //TODO : clear memory
//        currentProgram="";
//        fireProgramChanged();
//        setState(HALTED);
        initMachine();
    }

    private boolean canStartOrResume() {
        boolean ret = true;

        if(!isProgrammed()){
            sendMessage("Cannot start: No program entered");
            ret = false;
        } else if(isRunning()) {
            sendMessage("Already running");
            ret = false;
        }
        return ret;
    }

    public boolean start(int initPos, String initChars, Speed speed){
        boolean initOk = false;
        StringBuffer feedback = new StringBuffer();

        if (canStartOrResume()) {
            initOk = initMachine(initPos, initChars, feedback);
            if (initOk) {
                setSpeed(speed);
                execution = new Thread(this);
                execution.start();

            } else {
                sendMessage("Error initializing machine:\n" + feedback);
            }
        }
        return initOk;
    }

    public void resume(Speed speed){
        if(canStartOrResume()){
            setSpeed(speed);
            resumeProcess();
        }
    }

    private void resumeProcess(){
        sendMessage("Running...");
        execution = new Thread(this);
        execution.start();

    }

    public void stop(){

        if (isRunning()) {
            execution = null; //replace execution.stop()
            if (speed == Speed.compute) {
                setState(state);
            }
            headDir = HeadDirection.stay;
            fireStateChanged();
            showResults(ExecResult.USERINTERRUPT);
        } else {
            sendMessage("Machine is not running");
        }

    }
    public void showResults(ExecResult r) {
        sendMessage("\nMachine halted:");
        switch (r) {
            case HALT:
                sendMessage("Halt state reached");
                break;
            case OFFTAPE:
                sendMessage("The machine ran off the tape");
                break;
            case NOTRANS:
                sendMessage("No applicable transition found");
                break;
            case USERINTERRUPT:
                sendMessage("User interrupt");
                break;
//            default:
//                System.out.println("MessagePanel::showResults() : unknown for " + i);
        }
        sendMessage(getTotalTransitions() + " total transitions");
        sendMessage(getNonBlanks() + " non-blank characters on tape");
    }

    public void step(){

        if(state == HALTED) {
            sendMessage("Restarting...");
            initMachine();
        }
        ExecResult r = transition();
        headDir = HeadDirection.stay;
        fireStateChanged();
        if(r == ExecResult.HALT)
            sendMessage("Machine is halted");
        else if(r == ExecResult.OFFTAPE)
            sendMessage("The machine has run\noff the end of the tape");
        else if(r == ExecResult.NOTRANS)
            sendMessage("No applicable transition found");
        else if(r == ExecResult.NOPROG)
            sendMessage("No program entered");
        else if(state == HALTED)
            sendMessage("Halt state reached");
    }

    public void addStateListener(StateChangeListener l){
        stateChangeListeners.add(StateChangeListener.class, l);
    }

    public void removeStateListener(StateChangeListener l){
        stateChangeListeners.remove(StateChangeListener.class, l);
    }

    public void fireStateChanged(){
        StateChangeListener[] listenerList = stateChangeListeners.getListeners(StateChangeListener.class);
        for (StateChangeListener l : listenerList)
            l.stateChanged(new StateChangedEvent(this, ip, state, tapePos, headDir, speed));
    }

    public void addProgramListener(ProgramChangeListener l){
        programChangeListeners.add(ProgramChangeListener.class, l);
    }

    public void removeProgramListener(ProgramChangeListener l){
        programChangeListeners.remove(ProgramChangeListener.class, l);
    }

    public void fireProgramChanged(){
        ProgramChangeListener[] listeners = programChangeListeners.getListeners(ProgramChangeListener.class);
        for (ProgramChangeListener l : listeners)
            l.programChanged(new ProgramChangedEvent(this, currentProgram));
    }

    public void addMessageListener(MessageListener l){
        messageListeners.add(MessageListener.class, l);
    }

    public void removeMessageListener(MessageListener l){
        messageListeners.remove(MessageListener.class, l);
    }

    public void sendMessage(String msg){
        MessageListener[] listeners = messageListeners.getListeners(MessageListener.class);
        for(MessageListener l : listeners)
            l.addMessage(msg);
    }

    //final state f(execution)
//    public static final int SUCCESS = 0;
//    public static final int HALT = -1;
//    public static final int NOTFOUND = -2;
//    public static final int ABNORMAL = -3;
//    public static final int NOPROG = -4;
//    public static final int USERINT = -5;


    public enum ExecResult{
        SUCCESS,
        HALT,
        NOTRANS,
        OFFTAPE,
        NOPROG,
        USERINTERRUPT
    }


    public static final int HALTED = -1;
    public static final int RUNNING = -2;
    public static final int NA = -3;


//    public enum State{
//        halt,
//        running,
//        na
//    }

    public static final char NUL = 0;


}
