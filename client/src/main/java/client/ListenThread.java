package client;

import java.io.*;

public class ListenThread extends Thread {
    ObjectInputStream dis;

    ListenThread(ObjectInputStream dis) { this.dis = dis; }
    public void run() {
        while (true) {
            try {
                Message msg = (Message)dis.readObject();
                Client.clearLine();
                System.out.print(msg.msg);
                System.out.println();
                System.out.print(Client.prompt + Client.buffer);
                Client.promptWritten = true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Lost");
                break;
            } finally {
                try {
                    Client.stty(Client.ttyConfig.trim());
                } catch (Exception e) {
                    System.err.println("Exception restoring tty config");
                }
            }
        }
    }
}
