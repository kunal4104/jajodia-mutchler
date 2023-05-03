
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.*;
import java.sql.Timestamp;

public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private StateObj serverState;    


    public ServerThread(Socket socket, StateObj ServerState) {
        this.socket = socket;
        this.serverState = ServerState;
    }

    // public boolean removeItem(int clientNum) {
    //     // QueueObject head = pQueue.poll();
    //     if (serverState.topClient != null)
    //        System.out.println("head "+serverState.topClient.clientNum);
    //     boolean isHead = false;
    //     if (serverState.topClient != null && serverState.topClient.clientNum == clientNum) {
    //         isHead = true;
    //     }

    //     for (QueueObject obj : serverState.pQueue) {
    //         System.out.println(obj.clientNum+ " "+obj.timeStamp);
    //         if (obj.clientNum == clientNum) {
    //             serverState.pQueue.remove(obj);
    //         }
    //     }

    //     return isHead;
    // }

    // public boolean sendMessage(Socket socket) {
    //     try {
    //         PrintWriter outputGrant = new PrintWriter(socket.getOutputStream(),true);
    //         outputGrant.println("hellow from server");
    //         // outputGrant.close();
    //     } catch (Exception e) {
    //         System.out.println("Error occured send grant: " +e);
    //     }
    //     return true;
    // }

    public void updateCurrentState(StateObj serverState, PrintWriter output){
        int currVotes = 0;
        int replicaCount = serverState.RU;
        int maxVersion = serverState.VN;
        boolean hasDS = false;
        String smalledDS = serverState.DS;
        String neighbours = getNeighbours();
        for (int i = 0; i < serverState.neighStateList.size(); i++) {
            if (serverState.neighStateList.get(i).VN >= maxVersion) { 
                maxVersion = serverState.neighStateList.get(i).VN;
                replicaCount = serverState.neighStateList.get(i).RU;
            }
            if (serverState.neighStateList.get(i).DS.compareTo(serverState.DS) < 0){
                smalledDS = serverState.neighStateList.get(i).DS;
            }
        }
        smalledDS =  Character.toString(neighbours.charAt(0));
        
        if (neighbours.indexOf(serverState.DS) >= 0 ) {
            hasDS = true;
        }

        for (int i = 0; i < serverState.neighStateList.size(); i++) {
            if (serverState.neighStateList.get(i).VN == maxVersion) { 
                currVotes += 1;
            }
        }
        if (maxVersion == serverState.VN) {
            currVotes += 1;
        }

        if (currVotes > replicaCount/2 ) {
            serverState.VN = maxVersion + 1;
            serverState.RU = serverState.neighStateList.size() + 1;
            serverState.DS = smalledDS;
            System.out.println("Updated VN to " + serverState.VN + " and RU to " + serverState.RU + " and DS to " + serverState.DS);
        } else if (currVotes == replicaCount/2 && hasDS) { 
            serverState.VN += 1;
            serverState.RU = serverState.neighStateList.size() + 1;
            System.out.println("Updated VN to " + serverState.VN + " and RU to " + serverState.RU + " and DS to " + serverState.DS);
        } else {
            System.out.println("Can't update the document: Current VN is " + serverState.VN + " current RU is "+ serverState.RU + " and current DS is "+ serverState.DS );
        }
        serverState.neighStateList.clear();
        output.println("acknowledgement from "+ serverState.serverName);
    }

    // public void sendCurrentState(Node server) {

    // }

    public void sendReqToNeigh(String neighbours) {
        String[] neighbourChars = neighbours.split("");
        ArrayList<SendMessage> servConn = new ArrayList<SendMessage>(); 

        for (int i = 0; i < neighbourChars.length; i++) {
            if (!neighbourChars[i].equals(serverState.serverName)) {
                servConn.add(new SendMessage(serverState.servList.get(neighbourChars[i]), serverState));
            }
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
    }

    public String getNeighbours(){
        String servers = serverState.neighbours.get(serverState.currentState);
        String[] serverList = servers.split("\\s+");

        for (int i = 0; i < serverList.length; i++) {
            if (serverList[i].contains(serverState.serverName))
                return serverList[i];
        }

        return "";
    } 



    @Override
    public void run() {
        try {
            //Reading the input from Client
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            //returning the output to the client : true statement is to flush the buffer otherwise
            //we have to do it manuallyy
            output = new PrintWriter(socket.getOutputStream(),true);

            String outputString = input.readLine();
            // System.out.println("Server received " + outputString);

            if (outputString.equals("WRITE")) {
                System.out.println("Server neighbours " + getNeighbours());
                System.out.println("VN " + serverState.VN + " and RU " + serverState.RU + " and DS " + serverState.DS);
                serverState.numberMessageRec += 1;
                String neigh = getNeighbours();
                sendReqToNeigh(neigh);
                while (neigh.length() != serverState.neighStateList.size() + 1) {
                    Thread.sleep(2000);
                }

                // System.out.println("received for all neighbours");


                updateCurrentState(serverState, output);

                // sendResClient();

                if (serverState.numberMessageRec % 2 == 0) {
                    serverState.currentState += 1;
                }
            } else {

               String[] opt = outputString.split("\\s+");
                serverState.neighStateList.add(new NeighObj(Integer.parseInt(opt[0]), Integer.parseInt(opt[1]), opt[2]));
            }

            //if write request from client 
                //send current server ru, vn and ds to all the neighbours
                    //while message received from neighbour != number neighbour wait
                        //update my current state 
                            //is write possible
            //else
                //append ru, vn and ds to neighobjlist
            


            // sendMessage(socket);

        }
        catch (Exception e) {
            System.out.println("Error occured run: " +e);
        }
    }
}
// }

class Message{
    public long timeStamp;
    public int clientNum;
    public Socket socket;
    public Message(String type) {
        this.timeStamp = timeStamp;
        this.clientNum = clientNum;
        this.socket = socket;
    }
}


class SendMessage extends Thread {

    private Node server;
    private StateObj serverState;


    public SendMessage(Node server, StateObj ServerState) {
        this.server = server;
        this.serverState = ServerState;
    }

    private void startClient() {
        
        try (Socket socket = new Socket(server.ip, server.port)){
            // BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            String message = Integer.toString(serverState.VN)+" "+Integer.toString(serverState.RU)+" "+serverState.DS;

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