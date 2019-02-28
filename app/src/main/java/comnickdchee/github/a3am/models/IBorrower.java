package comnickdchee.github.a3am.models;

public interface IBorrower {

    /** Adds a book that the borrower requested. */
    void addRequestedBook(Book book);

    /** Changes the status of the book to available via finding
     *  the actual copy of the book in the db using its key. */
    void returnBook(int bookID);
}