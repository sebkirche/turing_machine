import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The CommandPanel holds the buttons to Start / Stop / Step /Resume the execution
 */
class CommandPanel extends JPanel implements ActionListener, StateChangeListener {

    JButton startBtn;
    JButton stopBtn;
    JButton resumeBtn;
    JButton stepBtn;
    JLabel speedLbl;
    JLabel curStateLbl;
    JComboBox speed;
    TapeDisplay tapeDisplay;
    private TuringMachine controller;
    private final static String START = "Start";
    private final static String STOP = "Stop";
    private final static String RESUME = "Resume";
    private final static String STEP = "Step";

    public CommandPanel(TuringMachine controller) {
        this.controller = controller;
        speedLbl = new JLabel("Speed:", SwingConstants.RIGHT);
        curStateLbl = new JLabel("State:", SwingConstants.LEFT);

        startBtn = new JButton(START);
        startBtn.addActionListener(this);

        stopBtn = new JButton(STOP);
        stopBtn.addActionListener(this);

        resumeBtn = new JButton(RESUME);
        resumeBtn.addActionListener(this);

        stepBtn = new JButton(STEP);
        stepBtn.addActionListener(this);

        speed = new JComboBox();
        speed.addItem(Speed.slow);
        speed.addItem(Speed.fast);
        speed.addItem(Speed.vfast);
        speed.addItem(Speed.compute);

        tapeDisplay = new TapeDisplay(controller);
        controller.getModel().addStateListener(tapeDisplay);

        initGUI();
    }

    public TapeDisplay getTapeDisplay(){
        return tapeDisplay;
    }

    public Speed getSelectedSped(){
        return (Speed) speed.getSelectedItem();
    }

    private void initGUI(){
        GridBagLayout gridbaglayout = new GridBagLayout();
        GridBagConstraints gridbagconstraints = new GridBagConstraints();
        setLayout(gridbaglayout);

        setBorder(BorderFactory.createLineBorder(Color.black));
        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 0;
        gridbagconstraints.gridwidth = 6;
        gridbagconstraints.anchor = GridBagConstraints.CENTER;
        gridbagconstraints.weighty = 0.1;
        gridbaglayout.setConstraints(curStateLbl, gridbagconstraints);
        add(curStateLbl);

        gridbagconstraints.gridy = 1;
        gridbagconstraints.gridwidth = 1;
        gridbagconstraints.insets = new Insets(0, 0, 0, 40);
        gridbagconstraints.weightx = 0.2;
        gridbagconstraints.weighty = 0.1;
        gridbaglayout.setConstraints(startBtn, gridbagconstraints);
        add(startBtn);

        gridbagconstraints.gridx = 1;
        gridbaglayout.setConstraints(stopBtn, gridbagconstraints);
        add(stopBtn);
        gridbagconstraints.gridx = 2;
        gridbaglayout.setConstraints(resumeBtn, gridbagconstraints);
        add(resumeBtn);
        gridbagconstraints.gridx = 3;
        gridbaglayout.setConstraints(stepBtn, gridbagconstraints);
        add(stepBtn);
        gridbagconstraints.gridx = 4;
        gridbagconstraints.insets = new Insets(0, 0, 0, 5);
        gridbaglayout.setConstraints(speedLbl, gridbagconstraints);
        add(speedLbl);
        gridbagconstraints.gridx = 5;
        gridbaglayout.setConstraints(speed, gridbagconstraints);
        add(speed);

        gridbagconstraints.gridx = 0;
        gridbagconstraints.gridy = 2;
        gridbagconstraints.insets = new Insets(0, 0, 5, 0);
        gridbagconstraints.gridwidth = 6;
        gridbagconstraints.fill = GridBagConstraints.BOTH;
        gridbagconstraints.anchor = GridBagConstraints.SOUTH;
        gridbagconstraints.weightx = 0;
        gridbagconstraints.weighty = 0.8;
        gridbaglayout.setConstraints(tapeDisplay, gridbagconstraints);
        add(tapeDisplay);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //MessagePanel controlpanel = machine.messagePanel;
        //SpecsPanel specspanel = machine.specsPanel;

        if(STEP.equals(e.getActionCommand())){
            controller.notifyStep();
        } else if(START.equals(e.getActionCommand())){
            controller.notifyStart();
        } else if(RESUME.equals(e.getActionCommand())){
            controller.notifyResume();
        } else if(STOP.equals(e.getActionCommand())){
            controller.notifyStop();
        }
    }

    @Override
    public void stateChanged(StateChangedEvent evt) {
        int state = evt.getState();

        if(state == TMEngineModel.HALTED)
            curStateLbl.setText("State: H");
        else if(state == TMEngineModel.RUNNING)
            curStateLbl.setText("State: R");
        else if(state == TMEngineModel.NA)
            curStateLbl.setText("State: N");
        else
            curStateLbl.setText("State: " + state);
        tapeDisplay.repaint();
    }
}
