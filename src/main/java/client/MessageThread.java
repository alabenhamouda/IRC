package client;

import java.io.*;
import java.util.Scanner;

public class MessageThread extends Thread {
    DataOutputStream dos;
    Scanner in;

    MessageThread(DataOutputStream dos, Scanner in) {
        this.dos = dos;
        this.in = in;
    }

    public void run() {
        try {
            Client.setTerminalToCBreak();
            while (true) {
                Client.read();
                dos.writeUTF(Client.buffer.toString());
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
