package comnickdchee.github.a3am.Backend;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import comnickdchee.github.a3am.Models.Book;
import comnickdchee.github.a3am.Models.Exchange;
import comnickdchee.github.a3am.Models.Status;
import comnickdchee.github.a3am.Models.User;

/**
 * Backend class that handles all the logic
 * required to access data and retrieve data from the Firebase
 * database.
 */
public class Backend {
    // Instance of the singleton
    private static Backend instance = new Backend();

    // Get the instance of our database
    private static final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    // References to static objects that we need over the lifetime of our singleton
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Have a direct reference to the user operating the app
    private static User mCurrentUser = new User();

    private static ArrayList<Book> mCurrentOwnedBooks = new ArrayList<>();

    // TODO: User should store an instance of his/her firebase user reference.
    private static final FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

    // Current books and requesters of the book. Note that this
    // doesn't get pushed into Firebase
    private static ArrayList<Book> mCurrentRequestedBooks = new ArrayList<>();

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
        mCurrentOwnedBooks.add(book);
        updateCurrentUserData();
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

    /** Getter for the instance of the user's FirebaseUser instance. */
    public FirebaseUser getFirebaseUser() {
        return mFirebaseUser;
    }

    /**
     * Class that loads all the current user into the user class referenced
     * by the singleton. Note that this is an asynchronous load, so we change the information
     * everytime the information in Firebase changes.
     */
    public static void loadCurrentUserData() {
        if (mFirebaseUser != null) {
            // Get the actual contents of the user class
            String uid = mFirebaseUser.getUid();
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

    /**
     * Update current user data by pushing the current user class to the table.
     * This allows for pushing requested books and owned books into the users table
     * if they ever get updated.
     */
    public void updateCurrentUserData() {
        String uid = mFirebaseUser.getUid();
        DatabaseReference userRef = mFirebaseDatabase.getReference("users").child(uid);
        userRef.setValue(mCurrentUser);
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
    public void updateExchange(Exchange exchange) {
    }

    /**
     * Overwrite the data of the original book with the new data
     * when a user requests a book, and updates this in Firebase.
     */
    public void updateRequests(Book book) {
        String uid = mFirebaseUser.getUid();

        DatabaseReference booksRef = mFirebaseDatabase.getReference("books");

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

    /**
     * Gets the current requested books of the user from the Firebase database.
     * Here, we use a callback object so that once all the data is loaded from the
     * database, we can start populating the recycler view in the front-end. Note
     * that this allows the app to both be asynchronous in terms of getting data
     * when a new change has been fired, as well as not be thread locked.
     */
    public void getRequestedBooks(final BookListCallback requestedBooksCallback) {
        // Get the current owned books of the user
        final ArrayList<String> ownedBookIDs = mCurrentUser.getOwnedBooks();
        final ArrayList<Book> requestedBooks = new ArrayList<>();

        DatabaseReference booksRef = mFirebaseDatabase.getReference("books");

        // Attach a listener on the books table, run through each entry in the
        // list and check if the keys are the same
        booksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestedBooks.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    for (String bookID : ownedBookIDs) {
                        if (data.getKey().equals(bookID)) {
                            // Fetch book object from Firebase
                            Book book = data.getValue(Book.class);
                            if (book != null) {
                                // Get the requester user data from the book requested list
                                if (book.getStatus() == Status.Requested) {
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
                setCurrentRequestedBooks(requestedBooks);
                requestedBooksCallback.onCallback(requestedBooks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /** Helper that sets the current requested books to the array list. */
    public void setCurrentRequestedBooks(ArrayList<Book> requestedBooks) {
        mCurrentRequestedBooks = requestedBooks;
    }

}
