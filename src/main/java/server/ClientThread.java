package server;
import java.io.*;
import java.net.*;

public class ClientThread extends Thread {
    Socket s;
    Color c;


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
            client.send(Server.show_connected());
            //prints history 
            // client.send(Server.history_print());
            System.out.println(Server.s);

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

    private void sendToClients(String msg) {
        boolean write = true;
        for (var client : Server.clients) {
            try {
                client.send(c.getCode() + msg + Color.RESET.getCode());
                if (write) {
                    // Server.history_add_message(msg);
                    Server.app(msg);
                }
                write = false;
            } catch (Exception e) {
                String username = client.username;
                Server.clients.remove(client);
                sendToClients(username + " has disconnected");
            }
        }
    }
}
