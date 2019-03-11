package comnickdchee.github.a3am.Models;

import android.media.Image;

import java.security.acl.Owner;

public interface IOwner {
    /** Owner can accept a request.
     * implemeted by User
     * Takes a book and user objects
     * Owner accepts request placed by borrower
     * @param book requested book
     * @param user book borrower
     * @see Book
     * @see User
     */
    void acceptRequest(Book book, User user);

    /** Owner can reject a request.
     * implemented by User
     * Takes a book object and a user object
     * Rjects request placed by the borrower
     * @param book requested book
     * @param user book borrower
     * @see Book
     * @see User
     */
    void rejectRequest(Book book, User user);
    
    /** Owner adds book to MyBooks.
     * Takes a book object
     * adds the book to the list of owned books
     * @param book book
     * @see Book
     */
    void addOwnedBook(Book book);

    /** Owner can remove books from MyBooks.
     * Takes a book object
     * removes a book from the list of owned books
     * @param book book
     * @see Book
     */
    void removeOwnedBook(Book book);

    /** Sets the location of the specific exchange.
     * Takes an exchange object and a string object
     * @param exchange
     * @param location location of exchange
     * @see Exchange
     */
    void setLocation(Exchange exchange, String location);

    /** Sets the image of the specific user's owned book.
     * Takes an image object and book object
     * sets the cover photo of the given book
     * @param bookID uniquely identifies the book
     * @param image cover photo of the image
     * @see Book
     * @see Image
     */
    void setImage(int bookID, Image image);

    /** Removes the image attached to the user's owned book
     *  based on the actual book.
     * @param bookID uniquely identifies the book
     */
    void removeImage(int bookID);
}
