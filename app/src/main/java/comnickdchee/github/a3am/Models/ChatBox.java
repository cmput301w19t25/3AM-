package comnickdchee.github.a3am.Models;

import java.util.ArrayList;
import java.util.Date;

public class ChatBox {

    private ArrayList<Message> messages = new ArrayList<Message>();

    private User user1;
    private User user2;


    public ChatBox(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public void sendMessage(String messageText, Date date, User user1, User user2) {

    }
    public ArrayList<Message> getMessages(){
        return messages;
    }

}
