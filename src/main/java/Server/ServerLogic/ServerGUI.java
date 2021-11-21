package Server.ServerLogic;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ServerGUI {
    private static JFrame jframe;
    private final JTextArea textAreaForLogging;
    private static PrintWriter pw;
    private final JButton buttonStart;
    private final JButton buttonStop;
    ServerGUI() {
        jframe = new JFrame();
        textAreaForLogging = new JTextArea();
        buttonStart = new JButton("Server start");
        buttonStop = new JButton("Stopping the server");
        initGUIComponents();
        initLoggingInFile();
    }

    private void initGUIComponents(){
        jframe.setTitle("Server");
        jframe.setLocationByPlatform(true);

        textAreaForLogging.setAutoscrolls(true);
        textAreaForLogging.setCaretPosition(textAreaForLogging.getDocument().getLength());
        ((DefaultCaret) textAreaForLogging.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textAreaForLogging.setFont(new Font("Dialog", Font.PLAIN, 18));
        textAreaForLogging.setTabSize(2);
        textAreaForLogging.setForeground(Color.WHITE);
        textAreaForLogging.setBackground(Color.darkGray);
        textAreaForLogging.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textAreaForLogging);
        scrollPane.setViewportView(textAreaForLogging);
        scrollPane.setAutoscrolls(true);

        buttonStart.setBackground(Color.WHITE);
        buttonStop.setBackground(Color.WHITE);
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY);
        panel.add(buttonStart);
        panel.add(buttonStop);

        jframe.add(scrollPane, BorderLayout.CENTER);
        jframe.add(panel, BorderLayout.SOUTH);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.pack();
        final int defaultWidth = 610;
        final int defaultHeight = 377;
        //These incomprehensible numbers are just beautiful numbers from the Fibonacci series,
        // because why not?))
        //They do not affect anything except the size of the window.
        jframe.setSize(defaultWidth, defaultHeight);
        jframe.setVisible(true);
    }

    public void printToFormLn(String text) {
        synchronized (textAreaForLogging) {
            textAreaForLogging.append(text + "\n");
        }
        try {
            pw.println(text);
            pw.flush();
        } catch (Exception ignore) {
        }
    }

    private void initLoggingInFile() {
        GregorianCalendar calendar = new GregorianCalendar();
        String nameOfLog = "log_Start_Server_on_" + calendar.get(Calendar.HOUR_OF_DAY) + "h-" +
                calendar.get(Calendar.MINUTE) + "m-" +
                calendar.get(Calendar.SECOND) + "s.txt";
        File logFile = new File(nameOfLog);
        try {
            pw = new PrintWriter(logFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setButtonStartActionListener(ActionListener actionListener) {
        buttonStart.addActionListener(actionListener);
    }

    public void setButtonStopActionListener(ActionListener actionListener) {
        buttonStop.addActionListener(actionListener);
    }

    public void changeEnableStartButton(boolean state) {
        buttonStart.setEnabled(state);
    }

    public void changeEnableStopButton(boolean state) {
        buttonStop.setEnabled(state);
    }

}