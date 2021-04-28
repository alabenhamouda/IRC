package server;

import java.net.*;
import java.util.Vector;

public class Server {

    static Vector<Client> clients;

    static {
        clients = new Vector<Client>();
    }

    static  Vector<String> history = new Vector<String>();
    static  int history_length = 50;
    static  int history_cur = 0;
    static  int history_reached = 0;
    static synchronized String history_print() {
        String tmp = "";
        if (Server.history_reached < Server.history_length) {
            for (int i = 0; i < history_reached; i++) {
                tmp += history.get(i) + "\n";
            }
        } else {
            for (int i = history_cur; i < history_length; i++) {
                tmp += history.get(i) + "\n";
            }
            for (int i = 0; i < history_cur; i++) {
                tmp += history.get(i) + "\n";
            }
        }
        return tmp;
    }
    static synchronized void history_add_message(String msg) {
        if (Server.history_reached < Server.history_length) {
            Server.history.add(msg);
            Server.history_reached++;
        } else {
            Server.history.set(Server.history_cur, msg);
            Server.history_cur = (Server.history_cur + 1) % Server.history_length;

        }
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
