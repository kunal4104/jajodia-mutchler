
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

    // public boolean updateCurrentState(){
    //    //TODO 
    // }

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
            System.out.println("Server received " + outputString);

            if (outputString.equals("WRITE")) {
                System.out.println("Server neighbours " + getNeighbours());
                serverState.numberMessageRec += 1;
                String neigh = getNeighbours();
                sendReqToNeigh(neigh);
                while (neigh.length() != serverState.neighStateList.size() + 1) {
                    Thread.sleep(2000);
                }

                System.out.println("received for all neighbours");


                // updateCurrentState();
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