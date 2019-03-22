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
    private static User mCurrentUser = null;

    // TODO: User should store an instance of his/her firebase user reference.
    private static final FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

    // Current books and requesters of the book. Note that this
    // doesn't get pushed into Firebase
    private static ArrayList<Book> mCurrentRequestedBooks = new ArrayList<>();

    /** Empty constructor for singleton. Prevents direct instantiate outside of class. */
    private Backend() {}

    /** Get the actual instance of the backend class. */
    public static Backend getBackendInstance() {
        return instance;
    }

    /** Returns the current user of the model class. */
    public User getCurrentUser() {
        if (mCurrentUser == null) {
            loadCurrentUserData();
        }

        return mCurrentUser;
    }

    /**
     * Class that loads all the current user into the user class referenced
     * by the singleton. Note that this is an asynchronous load, so we change the information
     * everytime the information in Firebase changes.
     */
    public void loadCurrentUserData() {
        if (mFirebaseUser != null) {
            // Get the actual contents of the user class
            String uid = mFirebaseUser.getUid();
            DatabaseReference userRef = mFirebaseDatabase.getReference("users").child(uid);

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
    public void updateUserData(User editedUser) {
        String uid = mFirebaseUser.getUid();
        DatabaseReference usersRef = mFirebaseDatabase.getReference("users").child(uid);
        usersRef.setValue(editedUser);
    }

    /**
     * Load the current user's requested books data by
     * checking the requested book ids stored in the
     * current user's array list, and find the actual references
     * in the book table.
     */
    public void loadRequestedBooks() {
        // This contains the requested book ids
        ArrayList<String> currentRequestedBooks = mCurrentUser.getRequestedBooksList();

        final ArrayList<Book> requestedBooks = new ArrayList<>();
        DatabaseReference booksRef = mFirebaseDatabase.getReference("books");

        // Iterate through current requested book ids to get
        // actual book objects stored in the table
        for (String bookID : currentRequestedBooks) {
            DatabaseReference currentBookRef = booksRef.child(bookID);

            // Here, we attach a single value event listener to get the value of
            // the requested book
            currentBookRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Book book = dataSnapshot.getValue(Book.class);

                    // Get the requester information here
                    for (String requesterID : book.getRequests()) {

                    }

                    requestedBooks.add(book);
                    setCurrentRequested(requestedBooks);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    /** Sets the current requested books to the arrayList */
    public void setCurrentRequested(ArrayList<Book> requestedBooks) {
        mCurrentRequestedBooks = requestedBooks;
    }

    /** Loads requester information into the member data. */
    public void loadRequesters() {
        DatabaseReference usersRef = mFirebaseDatabase.getReference("users");

        for (Book requestedBook : mCurrentRequestedBooks) {

            // Get all request users for each book and add them to the actual book
            ArrayList<String> requestIDs = requestedBook.getRequests();

            // For every requestID, we want to fetch their information and add it to the book
            // class for the adapter to use
            for (String requestID : requestIDs) {
                DatabaseReference userRef = usersRef.child(requestID);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User requester = dataSnapshot.getValue(User.class);
                        requestedBook.getRequestUsers().add(requester);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }

        // At the end, set the requesters list here
        setCurrentRequested(mCurrentRequestedBooks);
    }
}
