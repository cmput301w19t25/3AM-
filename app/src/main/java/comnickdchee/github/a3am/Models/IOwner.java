package comnickdchee.github.a3am.Models;

import android.media.Image;

public interface IOwner {
    /** Owner can accept a request. */
    void acceptRequest(Book book, User user);

    /** Owner can reject a request. */
    void rejectRequest(Book book, User user);
    
    /** Owner adds book to MyBooks. */
    void addOwnedBook(Book book);

    /** Owner can remove books from MyBooks. */
    void removeOwnedBook(Book book);

    /** Sets the location of the specific exchange. */
    void setLocation(Exchange exchange, String location);

    /** Sets the image of the specific user's owned book. */
    void setImage(int bookID, Image image);

    /** Removes the image attached to the user's owned book
     *  based on the actual book. */
    void removeImage(int bookID);
}
