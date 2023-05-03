import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    public static void main(String[] args) {



        try (ServerSocket serversocket = new ServerSocket(Integer.parseInt(args[0]))){
            while(true) {
                Socket socket = serversocket.accept();
                ServerThread serverThread = new ServerThread(socket, new StateObj(args[1]));
                //starting the thread
                // threadList.add(serverThread); 
                serverThread.start();

                //get all the list of currently running thread

            }
        } catch (Exception e) {
            System.out.println("Error occured in main: " + e);
        }
    }
}

class StateObj {

    String serverName;
    volatile static int currentState;
    volatile static int numberMessageRec;
    volatile static int VN;
    volatile static int RU;
    volatile static String DS;
    volatile static NeighObj[] neighStateList;

    static Node[] servList = { new Node("A", "localhost", 8000), new Node("B", "localhost", 8001), new Node("C", "localhost", 8002), new Node("D", "localhost", 8003), new Node("E", "localhost", 8004), new Node("F", "localhost", 8005), new Node("G", "localhost", 8006), new Node("H", "localhost", 8007)}; 

    
    HashMap<Integer, String> neighbours= new HashMap<>();

    public StateObj(String serverName) {

        this.currentState = 1;
        this.numberMessageRec= 0;
        this.serverName = serverName;
        this.neighbours.put(1, "ABCDEFGH");
        this.neighbours.put(2, "ABCD EFGH");
        this.neighbours.put(3, "A BCD EFG H");
        this.neighbours.put(4, "A BCDEFG H");
    }
}

class NeighObj{

    public String serverName;
    public int RU;
    public int VN;
    public String DS;

    NeighObj(String serverName, int RU, int VN, String DS) {
        this.serverName = serverName;
        this.RU = RU;
        this.VN = VN;
        this.DS = DS;

    }

}

class Node {

    public String serverName;
    public String ip;
    public int port;

    Node(String serverName, String ip, int port) {
        this.serverName = serverName;
        this.ip = ip;
        this. port = port;
    }

    // public void updateGrant(boolean newGrant) {
    //     grant = newGrant;
    // }
}

