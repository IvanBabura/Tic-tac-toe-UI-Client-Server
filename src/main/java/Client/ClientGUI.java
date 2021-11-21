package Client;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI {
    private final JFrame jframe;
    private int rank;
    private JButton[][] cellButtons;
    private boolean[][] maskForButtons;
    private final JPanel buttonsPanel;
    private final JPanel rightPanel;
    private final JPanel intoPanel;
    private final JLabel labelWhatWySymbol;
    private final JButton sendButton;
    private final JTextArea textArea;
    private final JTextField textFieldForSendingMessages;
    ActionListener buttonsActionListener;
    private static final int CELL_SIZE = 60;
    private static final int WINDOWS_SIZE = 220;

    public ClientGUI() {
        jframe = new JFrame();
        cellButtons = new JButton[rank][rank];
        buttonsPanel = new JPanel();
        rightPanel = new JPanel();
        intoPanel = new JPanel();
        sendButton = new JButton("send");
        textArea = new JTextArea(10, 10);
        labelWhatWySymbol = new JLabel("_");
        textFieldForSendingMessages = new JTextField(6);
        buttonsActionListener = new CellButtonsActionListener();
        initComponents();
    }

    public void initComponents() {
        jframe.setTitle("Tic-Tac-Toe");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setMinimumSize(new Dimension(WINDOWS_SIZE,WINDOWS_SIZE));

        textArea.setAutoscrolls(true);
        textArea.setCaretPosition(textArea.getDocument().getLength());
        ((DefaultCaret) textArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setViewportView(textArea);
        scrollPane.setAutoscrolls(true);

        sendButton.setEnabled(false);
        intoPanel.add(labelWhatWySymbol);
        intoPanel.add(textFieldForSendingMessages);
        intoPanel.add(sendButton);

        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(intoPanel, BorderLayout.SOUTH);

        jframe.add(rightPanel, BorderLayout.EAST);
        jframe.pack();
        jframe.setVisible(true);
    }

    public void createFieldFromButtons(int newRank, String whatMySymbol) {
        jframe.remove(buttonsPanel);
        labelWhatWySymbol.setText(whatMySymbol);
        buttonsPanel.removeAll();
        rank = newRank;
        cellButtons = new JButton[rank][rank];
        maskForButtons = new boolean[rank][rank];
        buttonsPanel.setLayout(new GridLayout(rank, rank));
        for (int row = 0; row < rank; row++) {
            for (int column = 0; column < rank; column++) {
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.setActionCommand(row + ":" + column);
                button.addActionListener(buttonsActionListener);
                button.setFont(new Font("Default", Font.PLAIN, 32));
                cellButtons[row][column] = button;
                maskForButtons[row][column] = true;
                buttonsPanel.add(cellButtons[row][column]);
            }
        }
        buttonsPanel.updateUI();
        int buttons_panel_size_rank = CELL_SIZE * rank;
        buttonsPanel.setPreferredSize(new Dimension(buttons_panel_size_rank, buttons_panel_size_rank));
        jframe.add(buttonsPanel, BorderLayout.CENTER);
        jframe.pack();
        jframe.revalidate();
    }

    public class CellButtonsActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String[] coordinates = e.getActionCommand().split(":");
            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);
            textArea.append("Cell: " + (row) + " : " + (column) + "\n");
            removeActionListenerFromButton(row,column, labelWhatWySymbol.getText());
        }
    }

    public void removeActionListenerFromButton(int row, int column, String symbol) {
        cellButtons[row][column].setEnabled(false);
        maskForButtons[row][column] = false;
        cellButtons[row][column].setBackground(Color.DARK_GRAY);
        cellButtons[row][column].setText(symbol);
        for(ActionListener al : cellButtons[row][column].getActionListeners())
            cellButtons[row][column].removeActionListener(al);
    }

    public void addButtonsActionListener(ActionListener actionListener) {
        for (int row = 0; row < rank; row++) {
            for (int column = 0; column < rank; column++) {
                cellButtons[row][column].addActionListener(actionListener);
            }
        }
    }

    public void setActionListenerSendButton(ActionListener actionListener) {
        sendButton.addActionListener(actionListener);
    }

    public void changeEnableSendButton(boolean state) {
        sendButton.setEnabled(state);
    }

    public void changeTextOnSendButton(String text) {
        sendButton.setText(text);
    }

    public String getTextFromTextField() {
        return textFieldForSendingMessages.getText();
    }

    public void setTextInTextField(String text) {
        textFieldForSendingMessages.setText(text);
    }

    public void printToFormLn(String text) {
        this.textArea.append(text + "\n");
    }

    public void changeEnableAllButtons(boolean state) {
        for (int row = 0; row < this.rank; row++) {
            for (int column = 0; column < this.rank; column++) {
                if(maskForButtons[row][column])
                    cellButtons[row][column].setEnabled(state);
            }
        }
    }
}