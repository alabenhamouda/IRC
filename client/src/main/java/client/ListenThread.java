package client;

import java.io.*;

public class ListenThread extends Thread {
    DataInputStream dis;

    ListenThread(DataInputStream dis) { this.dis = dis; }
    public void run() {
        while (true) {
            try {
                String notif = dis.readUTF();
                Client.clearLine();
                System.out.print(notif);
                System.out.println();
                System.out.print(Client.prompt + Client.buffer);
                Client.promptWritten = true;
            } catch (Exception e) {
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
