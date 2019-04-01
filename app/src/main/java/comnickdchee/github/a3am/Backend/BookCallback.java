package comnickdchee.github.a3am.Backend;

import comnickdchee.github.a3am.Models.Book;

/**
 * Interface callback class that retrieves the book data
 * when something in the database changes. These callbacks are
 * found in the getters methods of the backend class, and
 * are used to perform a sequence of actions when something
 * relevant happens.
 */
public interface BookCallback {
    void onCallback(Book book);
}
