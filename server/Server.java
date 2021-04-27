package server;

import java.net.*;
import java.util.Vector;

public class Server {

    static Vector<Client> clients;

    static { clients = new Vector<Client>(); }

    static  Vector<String> history;
    static  int history_length = 50;
    static  int history_cur = 0;
    static  int history_reached = 0;

    public static void main(String[] args) {
        try {
            var ss = new ServerSocket(8000);
            while (true) {
                Socket s = ss.accept();
                var client = new ClientThread(s);
                client.start();
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
