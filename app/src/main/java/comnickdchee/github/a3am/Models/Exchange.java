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


    public Exchange(ExchangeType type) {
        this.type = type;
    }

    /**
     *
     * @param owner owner of the book
     * @param borrower borrower of the book
     * @param pickup pickup location
     * @param date expected return date of book
     * @param completed tells whether exchange is complete
     * @param book uniquely identifies book
     * @see User
     */

    public Exchange(User owner, User borrower, String pickup, Date date, Boolean completed, Book book) {
        this.owner = owner;
        this.borrower = borrower;
        this.pickup = pickup;
        this.date = date;
        this.type = ExchangeType.OwnerHandover;

    }


    /**
     *
     * @param owner owner of the book
     * @param borrower borrower of the book
     * @param pickup pickup location
     * @param completed tells whether exchange is complete
     * @param book book being borrowed or lend
     * @see User
     */
    public Exchange(User owner, User borrower, String pickup, Boolean completed, Book book) {
        this.owner = owner;
        this.borrower = borrower;
        this.pickup = pickup;
        this.date = new Date();
        this.type = ExchangeType.OwnerHandover;

    }

    /**
     * Returns a string object
     * @return pickup location where the book can be picked up from
     */
    public String getLocation() {
        return pickup;
    }

    public User getOwner() {
        return owner;
    }

    public User getBorrower() {
        return borrower;
    }

    public String getPickup() {
        return pickup;
    }

    public Date getDate() {
        return date;
    }

    public ExchangeType getType() { return type; }

}
