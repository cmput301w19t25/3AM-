package comnickdchee.github.a3am.Models;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Asma
 */
public class ChatBox {

    private ArrayList<Message> messages = new ArrayList<Message>();

    private User user1;
    private User user2;


    public ChatBox(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    /**
     *
     * @param messageText string message
     * @param date current date
     * @param user1 can be sender or receiver
     * @param user2 can be sender or receiver
     */
    public void sendMessage(String messageText, Date date, User user1, User user2) {

    }

    /**
     *
     * @return an ArrayList of messages
     * @see ArrayList
     */
    public ArrayList<Message> getMessages(){
        return messages;
    }

}
