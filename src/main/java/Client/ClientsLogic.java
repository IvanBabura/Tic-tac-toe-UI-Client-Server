package Client;

import Connection.Connection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;

//
//Threads of receiving and sending messages for the client
//
public class ClientsLogic {
    private final String SERVER_ADDRESS;
    private final int SERVER_PORT;
    private final ClientGUI clientGui;
    private Connection connection;

    ClientsLogic(String serverAddress, int serverPortFromClient, ClientGUI clientGui) {
        this.SERVER_ADDRESS = serverAddress;
        this.SERVER_PORT = serverPortFromClient;
        this.clientGui = clientGui;
        ActionListener buttonActionListener = new sendButtonActionListener();
        clientGui.setActionListenerSendButton(buttonActionListener);
    }

    public void sendTextToGUI(String message) {
        clientGui.printToFormLn(message);
    }

    public void connectToServer() {
        sendTextToGUI("Connection...");
        sendTextToGUI("IP: " + SERVER_ADDRESS);
        sendTextToGUI("Port: " + SERVER_PORT + "\n...");
        try {
            Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
            connection = new Connection(socket);
            clientGui.changeTextOnSendButton("Send");
            clientGui.changeEnableSendButton(true);
            gettingMessagesThread.setDaemon(true);
            gettingMessagesThread.start();
        } catch (ConnectException e) {
            exceptionHandler("Server connection error.");
        } catch (SocketException e) {
            exceptionHandler("Lost connection.");
        } catch (UnknownHostException e) {
            exceptionHandler("Unknown host.");
        } catch (IOException e) {
            exceptionHandler("I/O Exception");
        } catch (IllegalThreadStateException e){
            e.printStackTrace();
        }
    }

    public void exceptionHandler(String exceptionName) {
        clientGui.changeEnableSendButton(false);
        sendTextToGUI(exceptionName);
        try {
            connection.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException ignore) {
        } finally {
            clientGui.changeTextOnSendButton("Reconnection");
            clientGui.changeEnableSendButton(true);
        }
    }

    public final Thread gettingMessagesThread = new Thread("gettingMessages") {
        @Override
        public void run() {
            try {
                String message = "";
                String mySymbol = "";
                String enemySymbol = "";
                if (connection.isConnection()) {
                    while (!message.equals("rank")) {
                        message = connection.getMessage();
                        sendTextToGUI("server: " + message);
                    }
                    message = connection.getMessage();
                    mySymbol = connection.getMessage();
                    enemySymbol = mySymbol.equals("X") ? "0" : "X";
                    clientGui.createFieldFromButtons(Integer.parseInt(message), mySymbol);
                    sendButtonActionListener buttonActionListener = new sendButtonActionListener();
                    clientGui.addButtonsActionListener(buttonActionListener);
                }
                while (connection.isConnection() || !message.equals("exit")) {
                    message = connection.getMessage();
                    if (message.equals("winner")) {
                        clientGui.changeEnableAllButtons(false);
                    }

                    if (message.equals("!wait")) {
                        clientGui.changeEnableAllButtons(true);
                        message = connection.getMessage();

                    } else if (message.equals("wait")) {
                        clientGui.changeEnableAllButtons(false);
                        message = connection.getMessage();
                        sendTextToGUI("server: " + message + "\n");
                        message = connection.getMessage();
                        String[] coordinates = message.split(":");
                        int row = Integer.parseInt(coordinates[0]);
                        int column = Integer.parseInt(coordinates[1]);
                        clientGui.removeActionListenerFromButton(row, column, enemySymbol);
                    }
                    sendTextToGUI("server: " + message + "\n");
                }
            } catch (SocketException | EOFException e) {
                exceptionHandler("Lost connection.");
            } catch (Exception e) {
                try {
                    connection.closeConnection();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    };

    public class sendButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            try {
                String coordinates = event.getActionCommand();
                if (coordinates.equals("Send"))
                    coordinates = clientGui.getTextFromTextField();
                try {
                    if (connection.isConnection()) {
                        if (coordinates == null) {
                            sendTextToGUI("Empty board");
                        } else {
                            sendTextToGUI("Send \"" + coordinates + "\".");
                            connection.sendMessage(coordinates);
                        }
                    } else {
                        sendTextToGUI("Nowhere to send \"" + coordinates + "\".");
                        connectToServer();
                    }
                } catch (NullPointerException ignored) {
                    connectToServer();
                }
            } catch (Exception e) {
                sendTextToGUI("Lost connection");
                e.printStackTrace();
            }
            clientGui.setTextInTextField("");
        }
    }
}


