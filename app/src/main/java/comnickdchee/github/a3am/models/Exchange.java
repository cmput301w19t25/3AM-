package comnickdchee.github.a3am.models;

import android.location.Location;

import java.util.Date;

/** Exchange class which keeps track of exchanges between a specific book between 2 users.
 *  This is created whenever a request is accepted by an owner of a book. */
public class Exchange {
    private User owner;
    private User borrower;
    private String pickup;
    private Date date;
    private ExchangeType type;
    private Boolean completed = false;
    private Book book;                 // uniquely identify the book involved (there is only one)

    public Exchange(User owner, User borrower, String pickup, Date date, Boolean completed, Book bookID) {
        this.owner = owner;
        this.borrower = borrower;
        this.pickup = pickup;
        this.date = date;
        this.type = ExchangeType.Borrowing;
        this.completed = completed;
        this.book = bookID;
    }

    public Exchange(User owner, User borrower, String pickup, Boolean completed, Book bookID) {
        this.owner = owner;
        this.borrower = borrower;
        this.pickup = pickup;
        this.date = new Date();
        this.type = ExchangeType.Borrowing;
        this.completed = completed;
        this.book = bookID;
    }

    public String getLocation() {
        return pickup;
    }

}
