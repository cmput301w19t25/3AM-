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
            System.exit(1);
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
                                    requestedBooks.add(book);
                                }
                            }

                        }
                    }
                }
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

    /** Getter that returns the requested books with user information. */
    public ArrayList<Book> getCurrentRequestedBooks() {
        return mCurrentRequestedBooks;
    }

}
