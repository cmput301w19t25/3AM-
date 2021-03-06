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
    public void sendMessage(String messageText, User user1, User user2, Date date) {
        Message message = new Message(messageText,user1,user2,date);
        messages.add(message);
    }

    /**
     *
     * @return an ArrayList of messages
     * @see ArrayList
     */
    public ArrayList<Message> getMessages(){
        return messages;
    }

    public ArrayList<User> getUser() {
        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        return users;
    }

    public String getLastMessage() {
        if (messages.size() > 0){
            return messages.get(messages.size()-1).getMessageText();
        }
        else
        {
            return "No Message history";
        }

    }

}
