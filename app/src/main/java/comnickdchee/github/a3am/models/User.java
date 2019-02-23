package comnickdchee.github.a3am.models;

import android.location.Location;
import android.media.Image;
import android.media.Rating;

import java.util.ArrayList;

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
    public User(String username, String phoneNumber, String email, String name,
         String address) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.address = address;

        ownedBooks = new ArrayList<Book>();
        requestedBooks = new ArrayList<Book>();
    }

    /** No address specified */
    User(String username, String phoneNumber, String email, String name) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;

        ownedBooks = new ArrayList<Book>();
        requestedBooks = new ArrayList<Book>();
    }

    @Override
    public void acceptRequest(Book book, User user) {
    }

    @Override
    public void rejectRequest(Book book, User user) {
    }

    @Override
    public void addOwnedBook(Book book) {
        ownedBooks.add(book);
    }

    @Override
    public void removeOwnedBook(Book book) {
        for (int i = 0; i < ownedBooks.size(); ++i) {
            if (ownedBooks.get(i) == book) {
                ownedBooks.remove(i);
                break;
            }
        }
    }

    @Override
    public void addRequestedBook(Book book) {
        requestedBooks.add(book);
    }

    public void returnBook(Book book) {
    }

    public Rating getRating() {
        return rating;
    }

    @Override
    public void setLocation(Exchange exchange, String location) {
    }

    public String getLocation(Exchange exchange) {
        return exchange.getLocation();
    }

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

    @Override
    public void removeImage(int bookID) {
    }


}
