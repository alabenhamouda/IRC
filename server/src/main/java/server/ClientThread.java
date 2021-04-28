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
            //prints history
            // client.send(Server.s);
            client.send(Server.history.print());
            sendToClients(username + " hopped in the conversation!");
            // displays connected users only to a newly connected user
            client.send("connected users: ");
            client.send(Server.show_connected());

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
        boolean done = true;
        for (var client : Server.clients) {
            try {
                client.send(c.getCode() + msg + Color.RESET.getCode());
            } catch (Exception e) {
                String username = client.username;
                Server.clients.remove(client);
                sendToClients(username + " has disconnected");
            } finally {
                if (done) {
                    // Server.app(msg) ;
                    Server.history.add_message(msg);
                }
                done = false;

            }
        }
    }
}
