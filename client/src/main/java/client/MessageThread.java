package client;

import java.io.*;
import java.util.Scanner;

public class MessageThread extends Thread {
    ObjectOutputStream dos;
    Scanner in;

    MessageThread(ObjectOutputStream dos, Scanner in) {
        this.dos = dos;
        this.in = in;
    }

    public void run() {
        try {
            Client.setTerminalToCBreak();
            while (true) {
                Client.read();
                Message tmp=new Message(Client.buffer.toString());
                dos.writeObject(tmp);
                Client.buffer.delete(0, Client.buffer.length());
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            try {
                Client.stty(Client.ttyConfig.trim());
            } catch (Exception e) {
                System.err.println("Exception restoring tty config");
            }
        }
    }
}
