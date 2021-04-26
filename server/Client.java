package server;

import java.io.*;
import java.net.*;

public class Client {
    Socket s;
    String username;
    DataOutputStream dos;
    DataInputStream dis;

    Client(Socket s, String username) throws IOException {
        this.s = s;
        this.username = username;
        dos = new DataOutputStream(s.getOutputStream());
        dis = new DataInputStream(s.getInputStream());
    }

    void send(String notif) throws IOException { dos.writeUTF(notif); }

    String listen() throws IOException { return dis.readUTF(); }
}
