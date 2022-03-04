import Interface.InterfaceServer.InterfaceServer;

public class Main {
    public static void main(String[] args) {
        final String IEMDB_URI = "http://138.197.181.131:5000/api";
        final int PORT = 8080;
        InterfaceServer interfaceServer = new InterfaceServer();
        interfaceServer.start(IEMDB_URI, PORT);
    }
}
