package server;
import client.*;
import java.io.*;
import java.net.*;

public class Client {
    Socket s;
    String username;
    ObjectOutputStream dos;
    ObjectInputStream dis;

    Client(Socket s,ObjectOutputStream dos,ObjectInputStream dis, String username) throws IOException {
        this.s = s;
        this.username = username;
        this.dos = dos;
        this.dis = dis;
    }

    void send(String notif) throws IOException { dos.writeObject(new Message(notif)); }
    void send(Message notif) throws IOException { dos.writeObject(notif); }

    Message  listen() throws IOException,ClassNotFoundException { return (Message)dis.readObject(); }
}
