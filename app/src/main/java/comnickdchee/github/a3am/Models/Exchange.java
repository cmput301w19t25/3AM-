package comnickdchee.github.a3am.Models;

import java.util.Date;

/** Exchange class which keeps track of exchanges between a specific book between 2 users.
 *  This is created whenever a request is accepted by an owner of a book.
 * @author Nicholas & Ismaeel
  */
public class Exchange {
    private User owner;
    private User borrower;
    private String pickup;
    private Date date;
    private ExchangeType type;
    private Boolean completed = false;
    private int bookID;                 // uniquely identify the book involved (there is only one)

    public Exchange(User owner, User borrower, String pickup, Date date, Boolean completed, Book book) {
        this.owner = owner;
        this.borrower = borrower;
        this.pickup = pickup;
        this.date = date;
        this.type = ExchangeType.Borrowing;
        this.completed = completed;
        this.bookID = book.getBookID();
    }

    /**
     *
     * @param owner
     * @param borrower
     * @param pickup
     * @param completed
     * @param book
     * @see User
     */
    public Exchange(User owner, User borrower, String pickup, Boolean completed, Book book) {
        this.owner = owner;
        this.borrower = borrower;
        this.pickup = pickup;
        this.date = new Date();
        this.type = ExchangeType.Borrowing;
        this.completed = completed;
        this.bookID = book.getBookID();
    }

    /**
     *
     * @param owner
     * @param borrower
     * @param pickup
     * @param completed
     * @param bookID
     * @see User
     */
    public Exchange(User owner, User borrower, String pickup, Boolean completed, int bookID) {
        this.owner = owner;
        this.borrower = borrower;
        this.pickup = pickup;
        this.date = new Date();
        this.type = ExchangeType.Borrowing;
        this.completed = completed;
        this.bookID = bookID;
    }

    /**
     * Returns a string object
     * @return pickup; location where the book can be picked up from
     */
    public String getLocation() {
        return pickup;
    }

}
