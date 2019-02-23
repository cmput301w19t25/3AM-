package comnickdchee.github.a3am;

import android.media.Image;

public class Book {
    private String ISBN;
    private String title;
    private String author;
    private Image image;
    private User owner;
    private Status status;
    private ArrayList<User> requests;
    private User currentBorrower;
    private int bookID;


    public Book(String ISBN, String title, String author) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setStatus(Status status) {this.status = status; }

    public Status getStatus() { return status; }

    public void setOwner(User owner) { this.owner = owner; }

    public User getOwner() { return owner; }

    public User getCurrentBorrower() { return currentBorrower; }

    public void setCurrentBorrower(User currentBorrower) {this.currentBorrower = currentBorrower; }

    public void addRequest(User requester) {requests.add(requester); }


}