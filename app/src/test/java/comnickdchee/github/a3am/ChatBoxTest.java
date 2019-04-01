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
    public void test_sendMessage() {
        Date date =  new Date();
        User user1 = new User("username1", "email.yahoo.com", "2132-52Ave","780-111-2222" );
        User user2 = new User("userName2",  "email@gmailcom",  "1111-25Ave","780-111-4444");
        ChatBox chatbox = new ChatBox(user1,user2);
        Date ddate = new Date();
        User fromUser = user1;
        User toUser = user2;
        Message messageText = new Message("hello", fromUser,toUser,ddate);
        list.add(messageText);
        chatbox.sendMessage("hello",  user1,user2, date );
        ArrayList<Message> messageList = chatbox.getMessages();
        assertTrue(list.contains(messageText));
    }
}
