
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

    public boolean sendMessage(Socket socket, Message message) {
        try {
            PrintWriter outputGrant = new PrintWriter(socket.getOutputStream(),true);
            outputGrant.println(message);
            // outputGrant.close();
        } catch (Exception e) {
            System.out.println("Error occured send grant: " +e);
        }
        return true;
    }

    public boolean updateCurrentState(){
       //TODO 
    }

    public void sendReqToNeigh() {
        //TODO

    }

    public String getNeighbours(){
        //TODO
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

            if (outputString == "WRITE") {
                serverState.numberMessageRec += 1;
                String neigh = getNeighbours()
                sendReqToNeigh(neigh);
                while (len(neigh) != len(neighObjlist)) {
                }
                updateCurrentState();
                sendResClient();

                if (serverState.numberMessageRec % 2 == 0) {
                    serverState.currentState += 1;
                }
            } else {

            }

            //if write request from client 
                //send current server ru, vn and ds to all the neighbours
                    //while message received from neighbour != number neighbour wait
                        //update my current state 
                            //is write possible
            //else
                //append ru, vn and ds to neighobjlist
            


            sendMessage(socket);

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
