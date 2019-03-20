package comnickdchee.github.a3am.Backend;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import comnickdchee.github.a3am.Models.User;

/**
 * Backend class that handles all the logic
 * required to access data and retrieve data from the Firebase
 * database.
 */
public class Backend {
    // Actual Firebase database reference of the root node
    private static DatabaseReference
            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

    // Have a direct reference to the user operating the app.
    private User mCurrentUser;

    /** Static method that returns an instance of the database reference. */
    public static DatabaseReference getInstance() {
        return mFirebaseDatabaseReference;
    }

}
