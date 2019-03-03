package comnickdchee.github.a3am;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import comnickdchee.github.a3am.Models.ChatBox;
import comnickdchee.github.a3am.Models.Message;
import comnickdchee.github.a3am.Models.User;

import static org.junit.Assert.*;

public class ChatBoxTest {

    private ArrayList<Message> list = new ArrayList<Message>();

    @Test
    public void test_sendMessage(String messageText, Date date, User sender, User receive) {

        User user1 = new User("username1", "780-111-2222", "email.yahoo.com", "name", "2132-52Ave");
        User user2 = new User("userName2", "780-111-4444", "email@gmailcom", "name2", "1111-25Ave");
        ChatBox chatbox = new ChatBox(user1,user2);
        messageText = "hello";
        Date ddate = new Date();
        User fromUser = user1;
        User toUser = user2;

        Message message1 = new Message(messageText,  user1,user2, ddate);
        chatbox.sendMessage(messageText, date, user1,user2);
        ArrayList<Message> messageList = chatbox.getMessages();

        Message message2 = messageList.get(2);
        assertEquals(message2, message1);
    }
}