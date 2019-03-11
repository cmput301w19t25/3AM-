package comnickdchee.github.a3am.Models;

/**
 * @author Nicholas
 * ExchangeType is used to determine the status of an exchange, where
 * Returning is set when the exchange expects a return of the book involved,
 * and Borrowing is set when the exchange expects one of the users to borrow
 * the book.
 * @see Exchange
 */
public enum ExchangeType {
    Returning,
    Borrowing
}
