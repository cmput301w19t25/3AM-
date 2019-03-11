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

    /**
     * Takes a book and user objects
     * Owner accepts request placed by borrower
     * @param book requested book
     * @param user book borrower
     * @see Book
     * @see User
     */
    @Override
    public void acceptRequest(Book book, User user) {
    }

    /**
     * rejectRequest implemets IOwner
     * Takes a book object and a user object
     * Rjects request placed by the borrower
     * @param book requested book
     * @param user book borrower
     * @see Book
     * @see IOwner
     * @see User
     */
    @Override
    public void rejectRequest(Book book, User user) {
    }

    /**
     * Implements method from IOwner
     * Takes a book object
     * adds the book to the list of owned books
     * @param book book
     * @see Book
     * @see IOwner
     */
    @Override
    public void addOwnedBook(Book book) {
        ownedBooks.add(book);
    }

    /**
     * Implements method from IOwner
     * Takes a book object
     * removes a book from the list of owned books
     * @param book book
     * @see Book
     * @see IOwner
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
     * Implements IBorrower
     * Takes a book object
     * adds the book to the list of requested books
     * @param book book
     * @see Book
     * @see IBorrower
     */
    @Override
    public void addRequestedBook(Book book) {
        requestedBooks.add(book);
    }

    /**
     * Implements IBorrower
     * Returns a rating object
     * @return rating rating of the book
     * @see Rating
     * @see IBorrower
     * Returns a rating object
     * @return rating rating of the book
     * @see Rating
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * Implements method form IOwner
     * Takes an exchange object and a string object
     * @param exchange
     * @param location location of exchange
     * @see Exchange
     * @see IOwner
     */
    @Override
    public void setLocation(Exchange exchange, String location) {
    }

    /**
     * Returns a string object
     * Takes am exchange object
     * @param exchange
     * @return location location of exchange
     * @see Exchange
     */
    public String getLocation(Exchange exchange) {
        return exchange.getLocation();
    }

    /**
     * Implements method from IOwner
     * Takes an image object and book object
     * sets the cover photo of the given book
     * @param bookID uniquely identifies the book
     * @param image cover photo of the image
     * @see Book
     * @see Image
     * @see IOwner
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
     * Implements method from IOnwer
     * Takes an integer ID of a book and removes the cover photo associated with that book
     * @param bookID uniquely identifies the book
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
     * @param bookID uniquely identifies the book
     * @param image cover photo of the image
     * @see IOwner
     */
    @Override
    public void setImage(int bookID, Image image) {
    }

    /**
     * Takes a string object and stes the username of the user
     * @param username name user
     */
    public void setUserName(String username) {
        this.username = username;
    }

    /**
     * Takes a string object and sets the phoneNumber of the userr
     * @param phoneNum phone number of the user
     */
    public void setPhoneNumber(String phoneNum) {
        this.phoneNumber = phoneNum;
    }

    /**
     * Takes a string object and sets the email of the user
     * @param email email of the user
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
