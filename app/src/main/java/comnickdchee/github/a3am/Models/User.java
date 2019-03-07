package comnickdchee.github.a3am.Models;

import android.media.Image;
import android.media.Rating;

import java.util.ArrayList;

/**
 * @author Ismaeel & Nicholas
 */

public class User implements IOwner, IBorrower {

    private String username;            // unique username
    private String phoneNumber;
    private String email;
    private String name;
    private String address;
    private ArrayList<Book> ownedBooks;     // list of books owned by user
    private ArrayList<Book> requestedBooks; // list of books that user is requesting
    private ArrayList<Exchange> exchanges;  // list of exchanges involving the user
    private Rating rating;                  // wow feature: user rating

    /** Ctor */
    public User(String username, String email, String address,
         String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;

        ownedBooks = new ArrayList<Book>();
        requestedBooks = new ArrayList<Book>();
    }

    /** No address specified
    User(String username, String phoneNumber, String email) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;

        ownedBooks = new ArrayList<Book>();
        requestedBooks = new ArrayList<Book>();
    }
     */

    /**
     * Takes a book and user objects
     * Owner accepts request placed by borrower
     * @param book
     * @param user
     * @see Book
     * @see User
     */
    @Override
    public void acceptRequest(Book book, User user) {
    }

    /**
     * Takes a book object and a user object
     * Rjects request placed by the borrower
     * @param book
     * @param user
     * @see Book
     * @see User
     */
    @Override
    public void rejectRequest(Book book, User user) {
    }

    /**
     * Takes a book object
     * adds the book to the list of owned books
     * @param book
     * @see Book
     */
    @Override
    public void addOwnedBook(Book book) {
        ownedBooks.add(book);
    }

    /**
     * Takes a book object
     * removes a book from the list of owned books
     * @param book
     * @see Book
     */
    @Override
    public void removeOwnedBook(Book book) {
        for (int i = 0; i < ownedBooks.size(); ++i) {
            if (ownedBooks.get(i) == book) {
                ownedBooks.remove(i);
                break;
            }
        }
    }

    /**
     * Takes a book object
     * adds the book to the list of requested books
     * @param book
     * @see Book
     */
    @Override
    public void addRequestedBook(Book book) {
        requestedBooks.add(book);
    }

    /**
     * Returns a rating object
     * @return rating of the book
     * @see Rating
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * Takes an exchange object and a string object
     * @param exchange
     * @param location of exchange
     * @see Exchange
     */
    @Override
    public void setLocation(Exchange exchange, String location) {
    }

    /**
     * Returns a string object
     * Takes am exchange object
     * @param exchange
     * @return location of exchange
     * @see Exchange
     */
    public String getLocation(Exchange exchange) {
        return exchange.getLocation();
    }

    /**
     * Takes an image object and book object
     * sets the cover photo of the given book
     * @param bookID
     * @param image
     * @see Book
     * @see Image
     */
    public void setImage(Book bookID, Image image) {
    }

    public ArrayList<Exchange> getExchangeList() {
        return exchanges;
    }
    public ArrayList<Book> getOwnedBookList() {
        return ownedBooks;
    }
    public ArrayList<Book> getRequestedBooksList() {
        return requestedBooks;
    }

    /**
     * Takes an integer ID of a book and removes the cover photo associated with that book
     * @param bookID
     * @see IOwner
     */
    @Override
    public void removeImage(int bookID) {
    }

    /**
     * Implemets method from IBorrower
     * removes the book from the list of borrowed books
     * @param bookID is a unique identifier of the book being returned by the borrower
     * @see IBorrower
     */
    @Override
    public void returnBook(int bookID) {
    }

    /**
     * implemets method from interface Owner
     *  sets the image of the book
     * @param bookID
     * @param image
     * @see IOwner
     */
    @Override
    public void setImage(int bookID, Image image) {
    }

    /**
     * Takes a string object and stes the username of the user
     * @param username
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * Takes a string object and sets the phoneNumber of the userr
     * @param phoneNum
     */
    public void setPhoneNumber(String phoneNum) {
        this.phoneNumber = phoneNum;
    }

    /**
     * Takes a string object and sets the email of the user
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retirns a string object
     * @return email of the user
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Returns a string object
     * @return username of the user
     */
    public String getUserName() {
        return this.username;
    }

    /**
     * Returns a string object
     * @return phoneNumber of the user
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Returns a string object
     * @return address of the user
     */
    public String getAddress() {
        return address;
    }

    /**
     * Takes a string object address and sets the address
     * @param address; the address of the user
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
