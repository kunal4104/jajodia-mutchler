import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        //using serversocket as argument to automatically close the socket
        //the port number is unique for each server

        //list to add all the clients thread
        ArrayList<ServerThread> threadList = new ArrayList<>();



        try (ServerSocket serversocket = new ServerSocket(Integer.parseInt(args[0]))){
            while(true) {
                Socket socket = serversocket.accept();
                ServerThread serverThread = new ServerThread(socket, threadList, new StateObj());
                //starting the thread
                threadList.add(serverThread); 
                serverThread.start();

                //get all the list of currently running thread

            }
        } catch (Exception e) {
            System.out.println("Error occured in main: " + e);
        }
    }
}

class QueueObject {
    public long timeStamp;
    public int clientNum;
    public Socket socket;
    public QueueObject (long timestamp, int clientNum, Socket socket) {
        this.timeStamp = timestamp;
        this.clientNum = clientNum;
        this.socket = socket;
    }
}

class StateObj {

    volatile static PriorityQueue<QueueObject> pQueue = new PriorityQueue<QueueObject>(10, new Comparator<QueueObject>() {
        public int compare(QueueObject n1, QueueObject n2) {
            return Long.compare(n1.timeStamp, n2.timeStamp);
        }
    });
    volatile static boolean isLocked = false;
    volatile static QueueObject topClient = null;


    public StateObj() {
    }
}