
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.BadLocationException;

/**
 * ProgramPanel - Show / Edit the program Listing
 */
class ProgramPanel extends JPanel implements ActionListener, ProgramChangeListener, StateChangeListener {

    JButton clearPgmBtn;
    JButton installBtn;
    private JTextArea programTxt;
    JScrollPane listing;
    private TuringMachine controller;
    private static final String CLEARPGM = "Clear Program";
    private static final String INSTALLPGM = "Install Program";
    private Highlighter.HighlightPainter bluePainter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);

    public ProgramPanel(TuringMachine controller) {
        this.controller = controller;
        bluePainter = new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
        initGUI();
    }

    private void initGUI() {
        clearPgmBtn = new JButton(CLEARPGM);
        installBtn = new JButton(INSTALLPGM);

        clearPgmBtn.addActionListener(this);
        installBtn.addActionListener(this);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("Programming");
        programTxt = new JTextArea(10, 20);
        listing = new JScrollPane(programTxt,
                                  ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        setLayout(gbl);
        setBorder(BorderFactory.createLineBorder(Color.black));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        //gbc.insets = new Insets(0, 0, 5, 5);
        //gbc.weighty = 0.1;
        //gbl.setConstraints(label, gbc);
        add(label, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(clearPgmBtn, gbc);

        gbc.gridx = 1;
        add(installBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.weighty = 0.9;
        gbc.gridwidth = 2;
        //gbl.setConstraints(listing, gbc);
        add(listing, gbc);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //StringBuffer errBuf = new StringBuffer(50);

        if(CLEARPGM.equals(e.getActionCommand()))
            controller.notifyClearProgram();
        else if(INSTALLPGM.equals(e.getActionCommand())) {
            controller.notifyEnteringProgram(programTxt.getText());
        }
    }

    public String getProgram() {
        return programTxt.getText();
    }

    public void fillProgram(String pgm) {
        programTxt.setText(pgm);
    }

    @Override
    public void stateChanged(StateChangedEvent evt) {
//        if(evt.getSpeed() != Speed.compute)
            highlightPgmTrans(evt.getCurrentTransition());
    }

    public void programChanged(ProgramChangedEvent evt) {
        programTxt.setText(evt.getPgm());
    }

    public void highlightPgmTrans(int pos) {
        int start;
        int end;
        //int lines = programTxt.getLineCount();

        //if(pos > lines)
        //return;

        try {

            start = programTxt.getLineStartOffset(pos);
            end = programTxt.getLineEndOffset(pos);

            Highlighter h = programTxt.getHighlighter();
            h.removeAllHighlights();
            h.addHighlight(start, end, bluePainter);

        } catch (BadLocationException ex) {
            //System.out.println("out of text: "+pos+"/"+lines);
            ex.printStackTrace();
        }
    }
}
