import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

class SpecsPanel extends JPanel implements ActionListener, AdjustmentListener {

    private TuringMachine controller;
    private JButton loadNewBtn;
    private JComboBox loader;
    private JTextField machineName;
    private JLabel initPos;
    private JScrollBar initPosBar;
    private JTextField initChars;
    private JLabel nameLbl;
    private JLabel initPosLbl;
    private JLabel initCharsLbl;

    private static final String LOADNEWPGM = "Load new program:";

    public SpecsPanel(TuringMachine controller) {
        super();
        this.controller = controller;
        nameLbl = new JLabel("Machine name");
        initPosLbl = new JLabel("Initial tape position");
        initCharsLbl = new JLabel("Initial characters on tape");
        loadNewBtn = new JButton(LOADNEWPGM);
        loadNewBtn.addActionListener(this);

        loader = new JComboBox();
        for(int i = 0; i < TMPreset.names.length; i++) {
            loader.addItem(TMPreset.names[i]);
        }

        machineName = new JTextField(30);
        initPosBar = new JScrollBar(Adjustable.HORIZONTAL, 15000, 50, 0, 30000);
        initPosBar.addAdjustmentListener(this);

        initPos = new JLabel("15000", SwingConstants.RIGHT);
        initChars = new JTextField(30);
        initGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(!(LOADNEWPGM.equals(e.getActionCommand())) || controller.isMachineRunning())
            return;

        String s = (String)loader.getSelectedItem();
        TMPreset presettm = new TMPreset(s);
        if(presettm.initPos < 0) {
            controller.notifyMessage("Machine unimplemented");
            return;
        }
        //engine.machine = new TMEngineModel(engine, engine.commandPanel.tapeDisplay);
        if(s.equals("<New>"))
            machineName.setText("");
        else
            machineName.setText(s);
        initPos.setText(String.valueOf(presettm.initPos));
        initPosBar.setValue(presettm.initPos);
        initChars.setText(presettm.initChars);
        if(!s.equals("<New>")) {
            controller.program(presettm);
        }
    }

    public String getSelectedProgram(){
        return (String)loader.getSelectedItem();
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if(initPosBar.equals(e.getAdjustable()))
            initPos.setText(String.valueOf(initPosBar.getValue()));
    }

    int getIniPos(){
        return initPosBar.getValue();
    }

    String getInitChars(){
        return initChars.getText();
    }

    String getMachineName(){
        return machineName.getText();
    }

    private void initGUI() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.anchor = 18;
        gbl.setConstraints(loadNewBtn, gbc);
        add(loadNewBtn);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbl.setConstraints(loader, gbc);
        add(loader);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbl.setConstraints(nameLbl, gbc);
        add(nameLbl);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(machineName, gbc);
        add(machineName);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbl.setConstraints(initPosLbl, gbc);
        add(initPosLbl);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.7;
        gbl.setConstraints(initPosBar, gbc);
        add(initPosBar);

        gbc.weightx = 0;
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbl.setConstraints(initPos, gbc);
        add(initPos);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbl.setConstraints(initCharsLbl, gbc);
        add(initCharsLbl);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(initChars, gbc);
        add(initChars);

        setBorder(BorderFactory.createLineBorder(Color.black));
    }

}
