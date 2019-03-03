package comnickdchee.github.a3am.Models;

import java.util.Date;

public class Message {

    private Date date;
    private User from;
    private User to;
    private String message;

    public Message(String message, User from, User to, Date date){
        this.message = "message";
        this.from = from;
        this.to = to;
        this.date = new Date();
    }

    public String getMessageText() {
        return this.message;
    }

    public User getFrom() {
        return this.to;
    }

    public User getTo() {
        return this.from;
    }

    public Date getDate() {
        return this.date;
    }
}

