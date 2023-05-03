import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Main {


    public static void main(String[] args) {

        StateObj clientState = new StateObj();
        // try {
        // clientNum = args[0];
        // } catch (InterruptedException e) {
        //     System.out.println(e);
        // }
        try {

            while (clientState.currentState < 5) {

                ArrayList<SendMessageWriteReq> servConn = new ArrayList<SendMessageWriteReq>(); 

                

                for (int i = 0; i < clientState.servList.length; i++) {
                    servConn.add(new SendMessageWriteReq(clientState.servList[i], clientState));
                    // servConn.add(new SendMessageRG(socketList.get(i), clientState.servTree[i], clientState));
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

                // for (int i = 0; i < servConn.size(); i++) {
                //     try {
                //         servConn.get(i).join();
                //     } catch (InterruptedException e) {
                //         e.printStackTrace();
                //     }
                // }

                // boolean quorumAvail = false;

                // do {
                //     quorumAvail = checkQuorum(1, clientState.servTree);

                //     if (quorumAvail) {
                //         if (!clientState.inCriticalSec) {
                //             clientState.inCriticalSec = true;
                //             clientState.criticalExecs = executeCriticalSec(clientState);
                //         }

                //     }

                // }while(!quorumAvail);



                // ArrayList<SendMessageRelease> servConnRel = new ArrayList<SendMessageRelease>(); 

                // for (int i = 0; i < clientState.servTree.length; i++) {
                //     // servConnRel.add(new SendMessageRelease(socketList.get(i), clientState.servTree[i], clientState));
                //     servConnRel.add(new SendMessageRelease(clientState.servTree[i], clientState));
                // }

                // for (int i = 0; i < servConnRel.size(); i++) {
                //     servConnRel.get(i).start();
                // }

                // for (int i = 0; i < servConnRel.size(); i++) {
                //     try {
                //         servConnRel.get(i).join();
                //     } catch (InterruptedException e) {
                //         e.printStackTrace();
                //     }
                // }

                // for (int i = 0; i < clientState.servTree.length; i++) {
                //     clientState.servTree[i].grant = false;
                // }

                // clientState.inCriticalSec = false;

            }
        } catch (Exception e) {
                System.out.println("Exception occured in client main: " + e);
        }


    }
}

// class SendMessageRelease extends Thread {

//     // private Socket socketObj;
//     private StateObj client;
//     private Node server;


//     // public SendMessageRelease(Socket socket, Node server, StateObj client) {
//     public SendMessageRelease(Node server, StateObj client) {
//         // this.socketObj = socket;
//         this.server = server;
//         this.client = client;
//     }

//     private void startClient() {
        
//         //reading the input from server


//         try (Socket socket = new Socket(server.ip, server.port)) {
//         // try (Socket socket = socketObj) {
            
//             BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
//             PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

//             Calendar calendar = Calendar.getInstance();
//             long timeMilli = calendar.getTimeInMillis();
//             String timeStmp = String.valueOf(timeMilli);
//             boolean reqSent = false;

//             String message = "RELEASE"+ " " + timeStmp + " " + client.clientNum;

//             output.println(message);
//             Thread.sleep(1000);

//             server.grant = false;

//             // input.close();
//             // output.close();
//             // socket.close();
            
//         } catch (Exception e) {
//             System.out.println("Exception occured in client main: " + e);
//         }
//     }

//     @Override
//     public void run() {

//         startClient();
//         super.run();
//     }

// }

class SendMessageWriteReq extends Thread {

    private Node server;
    // private Socket socketObj;
    private StateObj client;


    public SendMessageWriteReq(Node server, StateObj client) {
        this.server = server;
        // this.socketObj = socketObj;
        this.client = client;
    }

    private void startClient() {
        
        try (Socket socket = new Socket(server.ip, server.port)){
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            // Random r = new Random();
            // int randWait= (r.nextInt(5) + 5)*1000;
            // System.out.println(randWait);
            // Thread.currentThread().sleep(randWait);

            // Calendar calendar = Calendar.getInstance();
            // long timeMilli = calendar.getTimeInMillis();
            // String timeStmp = String.valueOf(timeMilli);
            // boolean reqSent = false;

       

            String message = "hellow from client";

            String inputStr;
            
            do {
                
                // if (!reqSent && !client.inCriticalSec && client.criticalExecs < 20) {
                output.println(message);
                    // reqSent = true;
                // }
                inputStr = input.readLine();
                // if (!client.inCriticalSec)
                System.out.println(inputStr);

            } while (true);

            // if (!client.inCriticalSec)
            //     server.grant = true;

            // input.close();
            // output.close();
            // socket.close();   // socket.close();
            
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
    // private Socket socketObj;
    private StateObj client;


    public SendMessageRG(Node server, StateObj client) {
        this.server = server;
        // this.socketObj = socketObj;
        this.client = client;
    }

    private void startClient() {
        
        try (Socket socket = new Socket(server.ip, server.port)){
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            // Random r = new Random();
            // int randWait= (r.nextInt(5) + 5)*1000;
            // System.out.println(randWait);
            // Thread.currentThread().sleep(randWait);

            // Calendar calendar = Calendar.getInstance();
            // long timeMilli = calendar.getTimeInMillis();
            // String timeStmp = String.valueOf(timeMilli);
            // boolean reqSent = false;

       

            String message = "hellow from client";

            String inputStr;
            
            do {
                
                // if (!reqSent && !client.inCriticalSec && client.criticalExecs < 20) {
                output.println(message);
                    // reqSent = true;
                // }
                inputStr = input.readLine();
                // if (!client.inCriticalSec)
                System.out.println(inputStr);

            } while (true);

            // if (!client.inCriticalSec)
            //     server.grant = true;

            // input.close();
            // output.close();
            // socket.close();   // socket.close();
            
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

    // public void updateGrant(boolean newGrant) {
    //     grant = newGrant;
    // }
}

class StateObj {

    static Node[] servList = { new Node("A", "localhost", 8000), new Node("B", "localhost", 8001), new Node("C", "localhost", 8002), new Node("D", "localhost", 8003), new Node("E", "localhost", 8004), new Node("F", "localhost", 8005), new Node("G", "localhost", 8006), new Node("H", "localhost", 8007)}; 
    int currentState;
    // volatile static Node[] servTree = { new Node(1, "10.176.69.32", 8000), new Node(2, "10.176.69.33", 8000), new Node(3, "10.176.69.34", 8000), new Node(4, "10.176.69.35", 8000), new Node(5, "10.176.69.36", 8000), new Node(6, "10.176.69.37", 8000), new Node(7, "10.176.69.38", 8000)}; 
    // volatile static Node[] servTree = { new Node("A", "localhost", 8000), new Node("B", "localhost", 8001), new Node("C", "localhost", 8002), new Node("A", "localhost", 8000)}; 
    // volatile static Node[] servTree = { new Node(1, "localhost", 8000)}; 

    public StateObj() {
        this.currentState = 1;
    }
}
