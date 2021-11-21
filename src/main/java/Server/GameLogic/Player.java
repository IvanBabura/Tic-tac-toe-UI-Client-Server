package Server.GameLogic;

import Connection.Connection;

import java.io.IOException;
import java.net.Socket;

public class Player {
    private static int id_counter = 0;
    private final int id;
    private Connection connection;
    private WhoseTurn whatMySymbol;

    public Player(Socket socket) {
        try {
            this.connection = new Connection(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        id_counter++;
        id = id_counter;
        whatMySymbol = WhoseTurn.Empty;
    }

    public int getId() {
        return id;
    }

    public void setWhatMySymbol(WhoseTurn whatMySymbol) {
        this.whatMySymbol = whatMySymbol;
    }

    public WhoseTurn getWhatMySymbol() {
        return whatMySymbol;
    }

    public void sendMessage(String message) throws IOException {
        connection.sendMessage(message);
    }

    public String getMessage() throws IOException {
        return connection.getMessage();
    }

    public void closeConnections() throws IOException {
        connection.closeConnection();
    }
}