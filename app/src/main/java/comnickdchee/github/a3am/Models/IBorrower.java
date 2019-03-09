package comnickdchee.github.a3am.Models;

public interface IBorrower {

    /**
     * Takes a book object
     * adds the book to the list of requested books
     * @param book book
     * @see Book
     */
    void addRequestedBook(Book book);

    /** Changes the status of the book to available via finding
     *  the actual copy of the book in the db using its key. */
    void returnBook(int bookID);
}