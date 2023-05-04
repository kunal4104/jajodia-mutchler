import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        StateObj clientState = new StateObj();

        try {
            while (clientState.currentState < 9) {
                ArrayList<SendMessageWriteReq> servConn = new ArrayList<SendMessageWriteReq>(); 
                for (int i = 0; i < clientState.servList.length; i++) {
                    servConn.add(new SendMessageWriteReq(clientState.servList[i], clientState));
                }

                for (int i = 0; i < servConn.size(); i++) {
                    servConn.get(i).start();
                }

                for (int i = 0; i < servConn.size(); i++) {
                    try {
                        servConn.get(i).join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                clientState.currentState += 1;
                System.out.println("current state: "+clientState.currentState);
            }
        } catch (Exception e) {
                System.out.println("Exception occured in client main: " + e);
        }
    }
}

class SendMessageWriteReq extends Thread {
    private Node server;
    private StateObj client;
    public SendMessageWriteReq(Node server, StateObj client) {
        this.server = server;
        this.client = client;
    }

    private void startClient() {
        try (Socket socket = new Socket(server.ip, server.port)){
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            String message = "WRITE";
            String inputStr;
            output.println(message);
            inputStr = input.readLine();
            System.out.println(inputStr);            
        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e);
        } 
    }

    @Override
    public void run() {

        startClient();
        super.run();
    }

}

class SendMessageRG extends Thread {
    private Node server;
    private StateObj client;

    public SendMessageRG(Node server, StateObj client) {
        this.server = server;
        this.client = client;
    }

    private void startClient() {        
        try (Socket socket = new Socket(server.ip, server.port)){
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            String message = "hellow from client";
            String inputStr;

            output.println(message);            
        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e);
        } 
    }

    @Override
    public void run() {

        startClient();
        super.run();
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

class StateObj {
    static Node[] servList = { new Node("A", "10.176.69.32", 8000), new Node("B", "10.176.69.33", 8000), new Node("C", "10.176.69.34", 8000), new Node("D", "10.176.69.35", 8000), new Node("E", "10.176.69.36", 8000), new Node("F", "10.176.69.37", 8000), new Node("G", "10.176.69.38", 8000), new Node("H", "10.176.69.39", 8000)}; 
    int currentState;

    public StateObj() {
        this.currentState = 1;
    }
}
