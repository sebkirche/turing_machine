
import javax.swing.*;
import java.awt.*;

public class TuringMachine extends JApplet {

    public static final int IDEAL_WIDTH = 600;
    public static final int IDEAL_HEIGHT = 500;

    //static final Color BGCOLOR = new Color(192, 192, 192);
    CommandPanel commandPanel;
    SpecsPanel specsPanel;
    ProgramPanel programPanel;
    MessagePanel messagePanel;
    private TMEngineModel machineModel;

    public TuringMachine() {
    }

    public void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //setBackground(Color.BLACK/*UIManager.getColor("Panel.background")*/);
        machineModel = new TMEngineModel();

        messagePanel = new MessagePanel(this);
        machineModel.addMessageListener(messagePanel);

        specsPanel = new SpecsPanel(this);

        commandPanel = new CommandPanel(this);
        machineModel.addStateListener(commandPanel);

        programPanel = new ProgramPanel(this);
        machineModel.addProgramListener(programPanel);
        machineModel.addStateListener(programPanel);

        initGUI();

        messagePanel.validate();

    }

    public void start() {
        commandPanel.tapeDisplay.initGraphics();
    }

    public static void main(String args[]) {
        JApplet applet = new TuringMachine();

        applet.init(); //this is automatic when in applet mode (called from browser)

        JFrame frame = new JFrame();
        frame.getContentPane().add(applet);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //frame.setSize(new Dimension(IDEAL_WIDTH,IDEAL_HEIGHT));
        frame.pack();
        applet.start(); //this is automatic when in applet mode
        frame.setVisible(true);
    }

    private void initGUI() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        Container c = getContentPane();

        c.setBackground(UIManager.getColor("Panel.background"));
        c.setLayout(gbl);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //gbc.insets = new Insets(0, 0, 0, 0);
        //gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        //gbc.weighty = 0;
        //gbl.setConstraints(commandPanel, gbc);
        c.add(commandPanel,gbc);
        //commandPanel.validate();

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        //gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        //gbl.setConstraints(specsPanel, gbc);
        c.add(specsPanel, gbc);
        //specsPanel.validate();

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        //gbc.anchor = GridBagConstraints.LAST_LINE_END;
        gbc.weighty = 1.0;
        //gbc.insets = new Insets(0, 0, 0, 15);
        //gbl.setConstraints(programPanel, gbc);
        c.add(programPanel, gbc);
        //programPanel.validate();

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        //gbc.insets = new Insets(0, 15, 0, 0);
        //gbl.setConstraints(messagePanel, gbc);
        c.add(messagePanel, gbc);
        validate();
    }

    public boolean isMachineRunning() {
        return machineModel.isRunning();
    }

    public boolean isMachineProgrammed(){
        return machineModel.isProgrammed();
    }

    public void program(TMPreset presettm) {
        StringBuffer strbuf = new StringBuffer(50);

        boolean flag = machineModel.program(presettm.initPos, presettm.initChars, presettm.program, strbuf);
        if(flag) {
//                msgPanel.addMessage("Machine programmed successfully.");
            if(presettm.comment.length() > 0){
                messagePanel.addMessage("\nProgram description:\n" + presettm.comment + "\n");
            }
//            } else {
//                msgPanel.addMessage("\nError:");
//                msgPanel.addMessage(strbuf.toString());
        }
    }

    public TMEngineModel getModel(){
        return machineModel;
    }

    public void notifyStep() {
        machineModel.step();
    }

    public void notifyStart() {
        machineModel.start(specsPanel.getIniPos(), specsPanel.getInitChars(), commandPanel.getSelectedSped());
    }

    public void notifyResume() {
        machineModel.resume(commandPanel.getSelectedSped());
    }

    public void notifyStop() {
        machineModel.stop();
    }

    public void notifyMessage(String txt){
        messagePanel.addMessage(txt);
    }

    public void notifyEnteringProgram(String pgm){
      StringBuffer errBuf = new StringBuffer(50);
            int i = specsPanel.getIniPos();
            String s1 = specsPanel.getInitChars();

            //engine.machine = new TMEngineModel(engine, engine.commandPanel.tapeDisplay);

            machineModel.program(i, s1, pgm, errBuf);

    }

    public void notifyClearProgram(){
       machineModel.clearProgram();
    }

    public boolean askScrolling(HeadDirection dir){
        return machineModel.scrollTape(dir);
    }

    public HeadDirection getMachineDirection(){
        return machineModel.getDirection();
    }

    public char[] getMachineMemory(int fromPos, int toPos) {
        return machineModel.getMemory(fromPos,  toPos);
    }

//    public int getCurrentMachinePos() {
//        return machineModel.getCurrentPos();
//    }
}
