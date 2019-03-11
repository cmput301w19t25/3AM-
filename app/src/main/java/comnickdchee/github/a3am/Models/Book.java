package comnickdchee.github.a3am.Models;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author Tatenda & Zaheen
 */

public class Book implements Serializable {

    private String ISBN;
    private String title;
    private String author;
    private Image image;
    private User owner;
    private Status status;
    private ArrayList<User> requests;
    private User currentBorrower;
    //private final int bookID = 0;         // TODO: Have a way to generate a unique ID for each book.


    public Book(String ISBN, String title, String author) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.status = Status.Available;
        this.currentBorrower = null;
    }

    public Book(String ISBN, String title, String author, User owner) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.owner = owner;
        this.status = Status.Available;
        this.currentBorrower = null;
    }

    /**
     * Returns a string object
     * @return ISBN uniquely identifies the book
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * Takes a string object and sets the ISBN of the book
     * @param ISBN uniquely identifies the book
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * Returns a string object
     * @return title the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Takes a string object and sets the title of the book
     * @param title the title of the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns a string object
     * @return author the author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * takes a string author and sets the author of the book
     * @param author the author of the book
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns an image object
     * @return image coverphoto of the book
     * @see Image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Takes in an image object and sets the book cover to it
     * @param image cover photo of the book
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Takes in a Status object and sets the status
     * @param status status can be avilable, requested, borrowed or accepted
     * @see Status
     */
    public void setStatus(Status status) {this.status = status; }

    /**
     * Returns a status object
     * @return status status can be avilable, requested, borrowed or accepted
     * @see Status
     */
    public Status getStatus() { return status; }

    /**
     * Takes a User object and sets the owner of the book
     * @param owner the owner of the book
     * @see User
     */
    public void setOwner(User owner) { this.owner = owner; }

    /**
     * Returns a User object
     * @return user the owner of the book
     * @see User
     */
    public User getOwner() { return owner; }

    /**
     * Returns a User object
     * @return currentBorrower the current borrower of the book
     * @see User
     */
    public User getCurrentBorrower() { return currentBorrower; }

    /**
     * Takes User object; and sets the current borrower of the book
     * @param currentBorrower the current borrower of the book
     * @see User
     */
    public void setCurrentBorrower(User currentBorrower) {this.currentBorrower = currentBorrower; }

    /**
     * Takes a User object and adds it to the list of requests
     * @param requester potential borrower of the book
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

    //public int getBookID() { return bookID; }

}
