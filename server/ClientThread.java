package server;

import java.io.*;
import java.net.*;
import java.util.Vector;

public class ClientThread extends Thread {
    Socket s;
    Color c;
    Vector<String> history;
    int history_length = 50;
    int history_cur = 0;
    int history_reached = 0;


    ClientThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try (var dis = new DataInputStream(s.getInputStream())) {
            String username = dis.readUTF();
            var client = new Client(s, username);
            Server.clients.add(client);
            c = Palette.getColor();
            sendToClients(username + " hopped in the conversation!");
            // displays connected users only to a newly connected user
            client.send("connected users: ");
            client.send(show_connected());
            client.send(history_print());

            //lisetener
            while (true) {
                try {
                    String msg = client.listen();
                    sendToClients("[" + username + "] " + msg);
                } catch (Exception e) {
                    Server.clients.remove(client);
                    sendToClients(username + " has disconnected");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
    private synchronized String history_print() {
        String tmp = "";
        for (var x : history) {
            tmp += x + "\n";
        }
        return tmp;
    }
    private synchronized void history_add_message(String msg) {
        if (history_reached < history_length) {
            history.add(msg);
            history_reached++;
        } else {
            history.set(history_cur, msg);
            history_cur = (history_cur + 1) % history_length;
        }
    }
    private synchronized String show_connected() {
        String tmp = "";
        for (var tmp_client : Server.clients) {
            tmp += tmp_client.username + " ";
        }
        return tmp;
    }

    private synchronized void sendToClients(String msg) {
        for (var client : Server.clients) {
            try {
                client.send(c.getCode() + msg + Color.RESET.getCode());
                history_add_message(msg);
            } catch (Exception e) {
                String username = client.username;
                Server.clients.remove(client);
                sendToClients(username + " has disconnected");
            }
        }
    }
}
