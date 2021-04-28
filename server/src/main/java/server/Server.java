package server;

import java.net.*;
import java.util.Vector;

public class Server {

    static Vector<Client> clients;
    static History history=new History();

    static {
        clients = new Vector<Client>();
    }


    static String show_connected() {
        String tmp = "";
        for (var tmp_client : Server.clients) {
            tmp += tmp_client.username + " ";
        }
        return tmp;
    }
    public static void main(String[] args) {
        try (var ss = new ServerSocket(8000);) {
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
