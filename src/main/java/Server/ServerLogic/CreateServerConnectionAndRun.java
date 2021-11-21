package Server.ServerLogic;

import Server.GameLogic.Game;
import Server.GameLogic.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

public class CreateServerConnectionAndRun {
    private static final int SERVER_PORT = 3456;
    private ServerSocket serverSocket;
    private final ServerGUI serverGUI;
    private boolean permissionGettingClients = true;
    private static final LinkedList<Game> listGames = new LinkedList<>();
    // here is a question: the list of games and streams of games are not connected. not good
    // the list of games can be moved to a separate class and synchronized
    private static final LinkedList<Player> listPlayers = new LinkedList<>();

    public CreateServerConnectionAndRun(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
        serverGUI.setButtonStartActionListener(actionListener -> startServer());
        serverGUI.setButtonStopActionListener(actionListener -> stopServer());
    }

    public void startServer() {
        serverGUI.changeEnableStartButton(false);
        serverGUI.printToFormLn("Server start...");
        permissionGettingClients = true;
        try {
            serverGUI.printToFormLn("Trying to create a connection...");
            serverSocket = new ServerSocket(SERVER_PORT);
            if (!serverSocket.isClosed()){
                serverGUI.printToFormLn("Success. Port = " + serverSocket.getLocalPort());

                serverGUI.changeEnableStopButton(true);
            }
            else{
                serverGUI.printToFormLn("Failure.");
                serverGUI.changeEnableStartButton(true);
            }

            Runnable gettingNewPlayer = () -> {
                serverGUI.printToFormLn("Initializing the flow of accepting users...");
                while (permissionGettingClients) {
                    try {
                        serverGUI.printToFormLn("Waiting for a new user... ");
                        Socket socketAccept = serverSocket.accept();
                        Player player = new Player(socketAccept);

                        listPlayers.add(player);
                        serverGUI.printToFormLn("<New Client> New User Connected. №" + player.getId());

                        AddingNewPlayerRunnable addingNewPlayerRunnable = new AddingNewPlayerRunnable(serverGUI,listGames,player);
                        Thread addingNewPlayerThread = new Thread(addingNewPlayerRunnable, "addingNewPlayer");
                        addingNewPlayerThread.start();
                    } catch (SocketException e) {
                        //e.printStackTrace();
                        permissionGettingClients = false;
                        serverGUI.printToFormLn("Connection closed.");
                        break;
                    } catch (IOException e) {
                        permissionGettingClients = false;
                        e.printStackTrace();
                        serverGUI.printToFormLn("Something went wrong.");
                    }
                }
                serverGUI.printToFormLn("End of stream " + Thread.currentThread().getName());
            };
            Thread thread = new Thread(gettingNewPlayer, "gettingNewPlayer");
            thread.start();

        } catch (Exception e) {
            serverGUI.printToFormLn("Failed to start server.\n");
            serverGUI.changeEnableStartButton(true);
            permissionGettingClients = false;
        }
    }

    protected void stopServer() {
        serverGUI.changeEnableStopButton(false);
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                permissionGettingClients = false;
                serverSocket.close();
                //need to clear the connections and the list of users!!! i didn't do it.
                //But on the other hand, on the other hand, it saves everyone and this can be used
                serverGUI.printToFormLn("Server stopped.");
                serverGUI.changeEnableStartButton(true);
            } else serverGUI.printToFormLn("The server has not been started.");
        } catch (Exception e) {
            serverGUI.changeEnableStopButton(true);
            serverGUI.printToFormLn("Server could not be stopped.");
        }
    }
}
