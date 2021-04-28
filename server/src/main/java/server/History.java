package server;

import java.util.Vector;

class History {
      int length = 50;
      int cur = 0;
      int reached = 0;
      Vector<String> messages = new Vector<String>();
     synchronized String print() {
        String tmp = "";
        if (reached < length) {
            for (int i = 0; i < reached; i++) {
                tmp += messages.get(i) + "\n";
            }
        } else {
            for (int i = cur; i < length; i++) {
                tmp += messages.get(i) + "\n";
            }
            for (int i = 0; i < cur; i++) {
                tmp += messages.get(i) + "\n";
            }
        }
        return tmp;
    }
    synchronized void add_message(String msg) {
        if (reached < length) {
            messages.add(msg);
            reached++;
        } else {
            messages.set(cur, msg);
            cur = (cur + 1) % length;

        }
    }
}
