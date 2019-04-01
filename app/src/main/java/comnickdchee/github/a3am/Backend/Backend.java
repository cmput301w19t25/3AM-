package comnickdchee.github.a3am.Backend;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Exchange;
import comnickdchee.github.a3am.Models.ExchangeType;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;

/**
 * Backend class that handles most of the logic
 * required to access data and retrieve data from the Firebase
 * database.
 */
public class Backend {
    // Instance of the singleton
    private static Backend instance = new Backend();

    // Get the instance of our database
    private static final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    // Have a direct reference to the user operating the app
    private static User mCurrentUser = new User();

    /** Empty constructor for singleton. Prevents direct instantiate outside of class. */
    private Backend() {}

    /** Get the actual instance of the backend class. */
    public static Backend getBackendInstance() {
        loadCurrentUserData();
        return instance;
    }

    /**
     * Add book to the current user model as well as the table.
     * We get the id of the currently added book, and pass it to
     * the actual
     */
    public void addBook(Book book) {
        // Push book to "books" table in database
        DatabaseReference booksRef = mFirebaseDatabase.getReference("books");
        DatabaseReference bookRef = booksRef.push();

        // Get the key, push the book object to the table, and add it to the book object
        String bookID = bookRef.getKey();
        book.setBookID(bookID);
        bookRef.setValue(book);

        // Add book to user data, which we can push to the table again
        mCurrentUser.addOwnedBook(book);
        updateCurrentUserData();
    }

    /**
     * Delete the book from both the books table and every requesting user's
     * requested books list.
     */
    public Boolean deleteBook(Book book) {
        // Don't let the user delete a book currently being interacted with
        if (book.getStatus() != Status.Available && book.getStatus() != Status.Requested) {
            return false;
        }

        // Iterate through all the requested books list
        // and delete the bookIDs from each user's requested list
        for (String requesterID : book.getRequests()) {

            // Get the current user, update his requested books, and then
            // push it back to the table
            getUser(requesterID, new UserCallback() {
                @Override
                public void onCallback(User user) {
                    User requester = user;
                    requester.getRequestedBooks().remove(book.getBookID());
                    updateUserData(requester);
                }
            });
        }

        // Delete from the current user my books list
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();
        getCurrentUserData(new UserCallback() {
            @Override
            public void onCallback(User user) {
                mCurrentUser = user;
                mCurrentUser.getOwnedBooks().remove(book.getBookID());
                updateCurrentUserData();
            }
        });

        // Delete the actual book afterwards
        DatabaseReference bookRef = mFirebaseDatabase.getReference("books").child(book.getBookID());
        bookRef.removeValue();

        return true;
    }

    /** Returns the current user of the model class. */
    public User getCurrentUser() {
        if (mCurrentUser == null) {
            loadCurrentUserData();
        }

        return mCurrentUser;
    }

    /**
     * Setter for when we need to change the current user class.
     * Essentially, we want to be able to change the actual user model
     * outside of the backend class if needed, and call update to
     * update the table (see updateCurrentUserData).
     */
    public void setCurrentUser(User user) {
        mCurrentUser = user;
    }

    /**
     * Class that loads all the current user into the user class referenced
     * by the singleton. Note that this is an asynchronous load, so we change the information
     * everytime the information in Firebase changes.
     */
    public static void loadCurrentUserData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // Get the actual contents of the user class
            String uid = firebaseUser.getUid();
            DatabaseReference usersRef = mFirebaseDatabase.getReference("users");
            DatabaseReference userRef = usersRef.child(uid);

            // Whenever the user reference is loaded, load the data of the user
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mCurrentUser = dataSnapshot.getValue(User.class);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            // If we got to the main screen without logging in
        } else {
            Log.e("Sign-in error!", "Could not get current user from database!");
        }
    }

    public void getCurrentUserData(final UserCallback userCallback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            // Get the actual contents of the user class
            String uid = firebaseUser.getUid();
            DatabaseReference usersRef = mFirebaseDatabase.getReference("users");
            DatabaseReference userRef = usersRef.child(uid);

            // Whenever the user reference is loaded, load the data of the user
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    userCallback.onCallback(user);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            // If we got to the main screen without logging in
        } else {
            Log.e("Sign-in error!", "Could not get current user from database!");
        }
    }

    /**
     * Update current user data by pushing the current user class to the table.
     * This allows for pushing requested books and owned books into the users table
     * if they ever get updated.
     */
    public void updateCurrentUserData() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();
        DatabaseReference userRef = mFirebaseDatabase.getReference("users").child(uid);
        userRef.setValue(mCurrentUser);
    }

    /** Updates the data of the user in the "users" table in Firebase. */
    public void updateUserData(User user) {
        String uid = user.getUserID();
        DatabaseReference userRef = mFirebaseDatabase.getReference("users").child(uid);
        userRef.setValue(user);
    }

    /** Updates the book data in the books table. Used in the addBook helper method. */
    public void updateBookData(Book book) {
        String bookID = book.getBookID();
        DatabaseReference booksRef = mFirebaseDatabase.getReference("books");
        DatabaseReference bookRef = booksRef.child(bookID);
        bookRef.setValue(book);
    }

    /**
     * Updates the current exchange in the Firebase database table when a transaction
     * has been completed (one of borrowing and returning.
     */
    public void updateExchange(Book book, ExchangeType exchangeType) {
        String bookID = book.getBookID();
        Exchange exchange = new Exchange(exchangeType);
        DatabaseReference exchangesRef = mFirebaseDatabase.getReference("exchanges");
        DatabaseReference exchangeRef = exchangesRef.child(bookID);
        exchangeRef.setValue(exchange);
    }

    public void updateExchange(Book book, Exchange exchange) {
        String bookID = book.getBookID();
        DatabaseReference exchangesRef = mFirebaseDatabase.getReference("exchanges");
        DatabaseReference exchangeRef = exchangesRef.child(bookID);
        exchangeRef.setValue(exchange);
    }

    public void deleteExchange(Book book) {
        String bookID = book.getBookID();
        DatabaseReference exchangeRef = mFirebaseDatabase.getReference("exchanges").child(bookID);
        exchangeRef.removeValue();

    }

    /**
     * Synchronous method that takes in the book
     * and uses the book id to retain the exchange information.
     */
    public void getExchange(Book book, final ExchangeCallback exchangeCallback) {
        DatabaseReference exchangesRef = mFirebaseDatabase.getReference("exchanges").child(book.getBookID());
        exchangesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Exchange exchange = dataSnapshot.getValue(Exchange.class);
                exchangeCallback.onCallback(exchange);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Accepts a request from the requester on the activity. This method is
     * implemented in the IOwner interface, and has the following logic:
     * The book gets its requesters list cleared, and the current borrower
     * of the book is set to the user id. Then, the status is updated to be
     * Accepted, and both the book and the requester user data is updated in
     * Firebase.
     */
    public void acceptRequest(User user, Book book) {
        book.getRequests().clear();
        book.setCurrentBorrowerID(user.getUserID());
        book.setStatus(Status.Accepted);

        updateExchange(book, ExchangeType.OwnerHandover);
        updateBookData(book);
        updateUserData(user);
    }

    /** Reject request that follows similar logic to accept request. */
    public void rejectRequest(User user, Book book) {
        book.getRequests().remove(user.getUserID());

        /** Empty */
        if (book.getRequests().size() == 0) {
            book.setStatus(Status.Available);
        }

        user.getRequestedBooks().remove(book.getBookID());

        updateBookData(book);
        updateUserData(user);
    }

    /**
     * Overwrite the data of the original book with the new data
     * when a user requests a book, and updates this in Firebase.
     */
    public void updateRequests(Book book) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

        // Change this if its status is available
        if (book.getStatus().equals(Status.Available)) {
            book.setStatus(Status.Requested);
        }

        // If the book doesn't contain the uid, we add it
        if (!book.getRequests().contains(uid)) {
            book.addRequest(uid);
        }

        // Add the bookID to the current user's request list
        mCurrentUser.addRequestedBook(book);

        // Update actual data in Firebase
        updateCurrentUserData();
        updateBookData(book);
    }

    /** Fetches a user's information from Firebase, given their uid. */
    public void getUser(String uid, final UserCallback userCallback) {
        DatabaseReference usersRef = mFirebaseDatabase.getReference("users");
        usersRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userCallback.onCallback(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /** Fetches a book's information from Firebase, given their book ID. */
    public void getSingleBook(String bookID, final BookCallback bookCallback) {
        DatabaseReference usersRef = mFirebaseDatabase.getReference("books");
        usersRef.child(bookID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                bookCallback.onCallback(book);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /** Fetches a book's information from Firebase, given their book ID. */
    public void getBook(String bookID, final BookCallback bookCallback) {
        DatabaseReference usersRef = mFirebaseDatabase.getReference("books");
        usersRef.child(bookID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                bookCallback.onCallback(book);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /** Gets the current requesters for a given book by retrieving data from firebase
     * using a callback/listener. This is used for the requester activity. */
    public void getRequesters(Book book, final UserListCallback requestersCallback) {
        final ArrayList<User> requesters = new ArrayList<>();

        DatabaseReference usersRef = mFirebaseDatabase.getReference("users");

        if (book.getRequests() != null) {
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    requesters.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        for (String requestersID : book.getRequests()) {

                            // Same user, so we add to list
                            if (data.getKey().equals(requestersID)) {
                                User requester = data.getValue(User.class);
                                requesters.add(requester);
                            }
                        }
                    }
                    // Notify synchronization complete
                    requestersCallback.onCallback(requesters);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    /** Retrieves books that the current user is borrowing at the moment.
     * This is retrieved from the user's requested list. We decided that all
     * the books that the current user is interacting with can be derived from
     * the requested books list (a little bit confusing), but it requires the
     * least amount of work.
     */
    public void getBorrowedBooks(final BookListCallback borrowedBooksCallback) {
        // Get the current owned books of the user
        final ArrayList<String> ownedBookIDs = mCurrentUser.getRequestedBooks();
        final ArrayList<Book> borrowedBooks = new ArrayList<>();

        DatabaseReference booksRef = mFirebaseDatabase.getReference("books");

        // Attach a listener on the books table, run through each entry in the
        // list and check if the keys are the same
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                borrowedBooks.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    for (String bookID : ownedBookIDs) {
                        if (data.getKey().equals(bookID)) {
                            // Fetch book object from Firebase
                            Book book = data.getValue(Book.class);
                            if (book != null) {
                                // Get the requester user data from the book requested list
                                if (book.getStatus() == Status.Borrowed) {
                                    // Add to the book
                                    borrowedBooks.add(book);
                                }
                            }

                        }
                    }
                }

                // Creates a callback on the front-end UI context, where
                // it can retrieve the data and start populating the recycler
                // view.
                borrowedBooksCallback.onCallback(borrowedBooks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /** Retrieves the books that the user is currently lending out to someone else.
     * The books are collected from the current user's ownedBooks list, and the
     * statuses of these books should only be "Borrowed"
     */
    public void getLendingBooks(final BookListCallback lendingBooksCallback) {
        // Get the current owned books of the user
        final ArrayList<String> ownedBookIDs = mCurrentUser.getOwnedBooks();
        final ArrayList<Book> lendingBooks = new ArrayList<>();

        DatabaseReference booksRef = mFirebaseDatabase.getReference("books");

        // Attach a listener on the books table, run through each entry in the
        // list and check if the keys are the same
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lendingBooks.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    for (String bookID : ownedBookIDs) {
                        if (data.getKey().equals(bookID)) {
                            // Fetch book object from Firebase
                            Book book = data.getValue(Book.class);
                            if (book != null) {
                                // Get the requester user data from the book requested list
                                if (book.getStatus() == Status.Borrowed) {
                                    // Add to the book
                                    lendingBooks.add(book);
                                }
                            }

                        }
                    }
                }

                // Creates a callback on the front-end UI context, where
                // it can retrieve the data and start populating the recycler
                // view.
                lendingBooksCallback.onCallback(lendingBooks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    /**
     * Gets the current requested books of the user from the Firebase database.
     * Here, we use a callback object so that once all the data is loaded from the
     * database, we can start populating the recycler view in the front-end. Note
     * that this allows the app to both be asynchronous in terms of getting data
     * when a new change has been fired, as well as not be thread locked.
     */
    public void getActionsBooks(final BookListCallback actionBooksCallback) {
        // Get the current owned books of the user
        final ArrayList<String> ownedBookIDs = mCurrentUser.getOwnedBooks();
        final ArrayList<Book> actionBooks = new ArrayList<>();

        DatabaseReference booksRef = mFirebaseDatabase.getReference("books");

        // Attach a listener on the books table, run through each entry in the
        // list and check if the keys are the same
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                actionBooks.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    for (String bookID : ownedBookIDs) {
                        if (data.getKey().equals(bookID)) {
                            // Fetch book object from Firebase
                            Book book = data.getValue(Book.class);
                            if (book != null) {
                                // Get the requester user data from the book requested list
                                if (book.getStatus() == Status.Requested || book.getStatus() == Status.Accepted) {
                                    // Add to the book
                                    actionBooks.add(book);
                                }
                            }

                        }
                    }
                }

                // Creates a callback on the front-end UI context, where
                // it can retrieve the data and start populating the recycler
                // view.
                actionBooksCallback.onCallback(actionBooks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /** Gets the books that a user is currently requesting. */
    public void getRequestedBooks(final BookListCallback requestedBooksCallback) {
        // Get the current owned books of the user
        ArrayList<String> requestedBooksID = mCurrentUser.getRequestedBooks();
        final ArrayList<Book> requestedBooks = new ArrayList<>();

        DatabaseReference booksRef = mFirebaseDatabase.getReference("books");

        // Attach a listener on the books table, run through each entry in the
        // list and check if the keys are the same
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestedBooks.clear();
                getCurrentUserData(new UserCallback() {
                    @Override
                    public void onCallback(User user) {
                        mCurrentUser = user;

                        ArrayList<String> requestedBooksID = mCurrentUser.getRequestedBooks();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            for (String bookID : requestedBooksID) {
                                if (data.getKey().equals(bookID)) {
                                    // Fetch book object from Firebase
                                    Book book = data.getValue(Book.class);
                                    if (book != null) {
                                        // Get the requester user data from the book requested list
                                        if (book.getStatus() == Status.Requested || book.getStatus() == Status.Accepted) {
                                            // Add to the book
                                            requestedBooks.add(book);
                                        }
                                    }

                                }
                            }
                        }

                        // Creates a callback on the front-end UI context, where
                        // it can retrieve the data and start populating the recycler
                        // view.
                        requestedBooksCallback.onCallback(requestedBooks);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
