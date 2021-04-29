package client;
import java.io.Serializable;

public class Message implements Serializable{
    public String msg;
    public Message(String tmp) {
        msg = tmp;
    }
}
