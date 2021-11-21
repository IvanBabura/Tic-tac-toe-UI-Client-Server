package Server.ServerLogic;

//
//The main class for starting the server
//
public class LaunchServer {
    public static void main(String[] ar){
        ServerGUI serverGUI = new ServerGUI();
        CreateServerConnectionAndRun createServerConnectionAndRun = new CreateServerConnectionAndRun(serverGUI);
        //Can be uncommented to start the server along with starting the GUI
        // without having to click a button

        //connectionServer.startServer();
    }
}
