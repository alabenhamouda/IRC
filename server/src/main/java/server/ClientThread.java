package server;
import client.Message;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    Socket s;
    Color c;


    ClientThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try {
            InputStream inputStream = this.s.getInputStream();
            ObjectInputStream dis = new ObjectInputStream(inputStream);

            OutputStream outputStream = this.s.getOutputStream();
            ObjectOutputStream dos = new ObjectOutputStream(outputStream);

            Message username = (Message)dis.readObject();
            var client = new Client(s, dos, dis, username.msg);
            Server.clients.add(client);
            c = Palette.getColor();
            //prints history
            // client.send(Server.s);
            client.send(Server.history.print());
            sendToClients(username.msg + " hopped in the conversation!");
            // displays connected users only to a newly connected user
            client.send("connected users: ");
            client.send(Server.show_connected());

            //lisetener
            while (true) {
                try {
                    Message  msg = client.listen();
                    sendToClients("[" + username.msg + "] " + msg.msg);
                } catch (Exception e) {
                    Server.clients.remove(client);
                    sendToClients(username.msg + " has disconnected");
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
                System.out.println(client.s + " has disconnected");
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
