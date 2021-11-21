package Connection;

import java.io.*;
import java.net.Socket;

public class Connection {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(String message) throws IOException {
        synchronized (this.out) {
            out.writeUTF(message);
        }
    }

    public String getMessage() throws IOException {
        synchronized (this.in) {
            return in.readUTF();
        }
    }

    public boolean isConnection() {
        return !socket.isClosed();
    }

    public void closeConnection() throws IOException {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (NullPointerException ignore){
        }
    }
}
