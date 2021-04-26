package client;

import java.io.*;

public class ListenThread extends Thread {
    DataInputStream dis;

    ListenThread(DataInputStream dis) { this.dis = dis; }
    public void run() {
        boolean promptWritten = false;
        while (true) {
            try {
                String notif = dis.readUTF();
                String str = Client.prompt + Client.buffer.toString();
                if (promptWritten) {
                    for (int i = 0; i < str.length(); i++) {
                        System.out.print('\b');
                    }
                }
                System.out.print(notif);
                int len = notif.length();
                while (len < str.length()) {
                    System.out.print(' ');
                    len++;
                }
                System.out.println();
                System.out.print(Client.prompt + Client.buffer);
                promptWritten = true;
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
