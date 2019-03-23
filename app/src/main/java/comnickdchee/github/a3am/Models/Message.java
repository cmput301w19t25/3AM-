package comnickdchee.github.a3am.Models;

import java.util.Date;

/**
 * @author Asma
 */

public class Message {

    private Date date;
    private User from;
    private User to;
    private String message;

    public Message(String message, User from, User to, Date date){
        this.message = message;
        this.from = from;
        this.to = to;
        this.date = new Date();
    }

    /**
     * Returns a string objest
     * @return message message
     */
    public String getMessageText() {
        return this.message;
    }

    /**
     * Returns a User object
     * @return to; the user whose receiving the messsage
     * @see User to; the user whose receiving the messsage
     */
    public User getFrom() {
        return this.to;
    }

    /**
     * Returns a User object
     * @return from; the user who's sending the message
     * @see User
     */
    public User getTo() {
        return this.from;
    }

    /**
     * Returns a Date object
     * @return date the date of sending/receiving the message
     * @see Date
     */
    public Date getDate() {
        return this.date;
    }
}

