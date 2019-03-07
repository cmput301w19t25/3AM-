package comnickdchee.github.a3am.Models;

import android.media.Image;

import java.util.ArrayList;

/**
 * @author Tatenda & Zaheen
 */

public class Book {
    private String ISBN;
    private String title;
    private String author;
    private Image image;
    private User owner;
    private Status status;
    private ArrayList<User> requests;
    private User currentBorrower;
    private final int bookID = 0;         // TODO: Have a way to generate a unique ID for each book.


    public Book(String ISBN, String title, String author) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
    }

    public Book(String ISBN, String title, String author, User owner) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.owner = owner;
    }

    /**
     * Returns a string object
     * @return ISBN which uniquely identifies the book
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Takes a string object and sets the ISBN of the book
     * @param ISBN which uniquely identifies the book
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * @return a String object; the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Takes a string object and sets the title of the book
     * @param title; the name of the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return stirng object; the author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * takes a string author and sets the author of the book
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return an image object; coverphoto of the book
     * @see Image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Takes in an image object and sets the book cover to it
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Takes in a Status object and sets the status
     * @param status
     * @see Status
     */
    public void setStatus(Status status) {this.status = status; }

    /**
     * Returns a status object
     * @return status
     * @see Status
     */
    public Status getStatus() { return status; }

    /**
     * Takes a User object and sets the owner of the book
     * @param owner
     * @see User
     */
    public void setOwner(User owner) { this.owner = owner; }

    /**
     * Returns a User object
     * @return user; the owner of the book
     * @see User
     */
    public User getOwner() { return owner; }

    /**
     * Returns a User object
     * @return user; the current borrower of the book
     * @see User
     */
    public User getCurrentBorrower() { return currentBorrower; }

    /**
     * Takes User object; and sets the current borrower of the book
     * @param currentBorrower
     * @see User
     */
    public void setCurrentBorrower(User currentBorrower) {this.currentBorrower = currentBorrower; }

    /**
     * Takes a User object and adds it to the list of requests
     * @param requester
     * @see User
     */
    public void addRequest(User requester) {requests.add(requester); }

    /**
     * Returns an arrayList of requests placed for the book
     * @return requests
     * @see ArrayList
     */
    public ArrayList<User> getRequests() {
        return requests;
    }

    /**
     *  Returns an integer object; the ID of the book
     * @return bookID
     */
    public int getBookID() { return bookID; }

}
