import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    public static void main(String[] args) {


        StateObj serverState = new StateObj(args[1]);
        try (ServerSocket serversocket = new ServerSocket(Integer.parseInt(args[0]))){
            while(true) {
                Socket socket = serversocket.accept();
                ServerThread serverThread = new ServerThread(socket, serverState);
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
    volatile static ArrayList<NeighObj> neighStateList = new ArrayList<NeighObj>();;

    static HashMap<String, Node> servList = new HashMap<>();  

    
    HashMap<Integer, String> neighbours= new HashMap<>();

    public StateObj(String serverName) {

        this.currentState = 1;
        this.numberMessageRec= 0;
        this.serverName = serverName;
        this.VN = 1;
        this.RU = 8;
        this.DS = "A";

        this.neighbours.put(1, "ABCDEFGH");
        this.neighbours.put(2, "ABCD EFGH");
        this.neighbours.put(3, "A BCD EFG H");
        this.neighbours.put(4, "A BCDEFG H");

        this.servList.put("A", new Node("A", "10.176.69.32", 8000)); 
        this.servList.put("B", new Node("B", "10.176.69.33", 8001)); 
        this.servList.put("C", new Node("C", "10.176.69.34", 8002)); 
        this.servList.put("D", new Node("D", "10.176.69.35", 8003));
        this.servList.put("E", new Node("E", "10.176.69.36", 8004)); 
        this.servList.put("F", new Node("F", "10.176.69.37", 8005)); 
        this.servList.put("G", new Node("G", "10.176.69.38", 8006)); 
        this.servList.put("H", new Node("H", "10.176.69.39", 8007));

    }
}

class NeighObj{

    public int RU;
    public int VN;
    public String DS;

    NeighObj(int VN, int RU, String DS) {
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

}

