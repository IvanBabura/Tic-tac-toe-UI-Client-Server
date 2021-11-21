package Client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LaunchClient {
    public static void main(String[] ar) {
        ClientGUI clientGui = new ClientGUI();
        clientGui.printToFormLn("Loading settings...");
        try (InputStream fis = LaunchClient.class.getClassLoader().getResourceAsStream("serverConfig.properties")){
            Properties properties = new Properties();
            properties.load(fis);
            String serverAddress = properties.getProperty("address");
            int serverPort = Integer.parseInt(properties.getProperty("port"));
            clientGui.printToFormLn("Success.");
            //run
            ClientsLogic clientsLogic = new ClientsLogic(serverAddress, serverPort, clientGui);
            clientsLogic.connectToServer();
        } catch (FileNotFoundException e) {
            clientGui.printToFormLn("Failure. File Not Found. Create config file \"serverConfig.properties\" and rerun application.");
            //e.printStackTrace();
        } catch (IOException e) {
            clientGui.printToFormLn("Failure. Try to rerun application.");
            //e.printStackTrace();
        }
    }
}
