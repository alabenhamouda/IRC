package server;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {
    Socket s;

    ClientThread(Socket s) { this.s = s; }

    public void run() {
        try (var dis = new DataInputStream(s.getInputStream())) {
            String username = dis.readUTF();
            var client = new Client(s, username);
            Server.clients.add(client);
            sendToClients(username + " vient de se connecter");
            while (true) {
                try {
                    String msg = client.listen();
                    sendToClients(username + "> " + msg);
                } catch (Exception e) {
                    Server.clients.remove(client);
                    sendToClients(username + " s'est deconnecte");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    private void sendToClients(String msg) {
        for (var client : Server.clients) {
            try {
                client.send(msg);
            } catch (Exception e) {
                String username = client.username;
                Server.clients.remove(client);
                sendToClients(username + " s'est deconnecte");
            }
        }
    }
}
