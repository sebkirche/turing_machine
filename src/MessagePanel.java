import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MessagePanel - display the messages from the engine
 */
class MessagePanel extends JPanel implements ActionListener, MessageListener {

    JButton clearMsgBtn;
    private JTextArea messages;
    private JScrollPane msgScroll;
    private TuringMachine controller;
    private SpecsPanel specs;
    private static final String CLEARMSG = "Clear Message Box";

    public MessagePanel(TuringMachine controller) {
        this.controller = controller;

        initGUI();
    }

    private void initGUI(){
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        messages = new JTextArea(5, 30);
        messages.setEditable(false);
        msgScroll = new JScrollPane(messages);
        clearMsgBtn = new JButton(CLEARMSG);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 0.9;
        gbl.setConstraints(msgScroll, gbc);
        add(msgScroll);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbl.setConstraints(clearMsgBtn, gbc);
        add(clearMsgBtn);

        this.setLayout(gbl);

        setBorder(BorderFactory.createLineBorder(Color.black));

        clearMsgBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StringBuffer strbuf = new StringBuffer(50);

        if(CLEARMSG.equals(e.getActionCommand()))
            messages.setText("");
    }

    public void addMessage(String s) {
        messages.append(s + "\n");
    }

}
